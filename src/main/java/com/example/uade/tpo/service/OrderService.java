package com.example.uade.tpo.service;

import com.example.uade.tpo.Utils.Mapper;
import com.example.uade.tpo.dtos.response.OrderResponseDto;
import com.example.uade.tpo.dtos.response.ProductResponseDto;
import com.example.uade.tpo.entity.*;
import com.example.uade.tpo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Autowired
    private IDiscountRepository discountRepository;

    public Optional<OrderResponseDto> getOrderById(Long orderId) {
        return orderRepository.findById(orderId).map(Mapper::convertToOrderResponseDto);
    }

    public List<OrderResponseDto> getOrderByUserId(Long userId) {
        List<OrderResponseDto> orders = new ArrayList<>();
        for(Order order : orderRepository.findAll()) {
            if(order.getUserId().equals(userId)) {
                orders.add(Mapper.convertToOrderResponseDto(order));
            }
        }
        return orders;
    }

    @Transactional
    public OrderResponseDto createOrderFromCart(Long cartId){
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            Order order = new Order();
            order.setUserId(cart.getUserId());
            order.setOrderDate(new Date());
            order.setStatus("PENDING");

            List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
            if(cartItems.isEmpty()){
                return null;
            }

            orderRepository.save(order);
            List<OrderDetail> orderDetails = new ArrayList<>();

            for(CartItem cartItem : cartItems){
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(order.getId());
                orderDetail.setProductId(cartItem.getProductId());
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setPrice(productRepository.findById(cartItem.getProductId()).get().getPrice());
                orderDetail.setTotal(orderDetail.getQuantity() * orderDetail.getPrice());
                orderDetails.add(orderDetail);
            }

            BigDecimal bd = BigDecimal.valueOf(orderDetails.stream().mapToDouble
                    (detail -> detail.getQuantity() * detail.getPrice()).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
            double totalAmount = bd.doubleValue();
            order.setTotalAmount(totalAmount);

            orderRepository.save(order);
            orderDetailRepository.saveAll(orderDetails);

            cartRepository.delete(cart);
            cartItemRepository.deleteAll(cartItems);

            return Mapper.convertToOrderResponseDto(order);
        }
        return null;
    }

    public Boolean deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
            orderDetailRepository.deleteByOrderId(orderId);
            return true;
        }
        return false;
    }

    public OrderResponseDto applyDiscountToOrder(Long orderId, String code) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if(order.getStatus().equals("PENDING")) {
                Optional<Discount> optionalDiscount = discountRepository.findByCode(code);
                if(!order.getDiscountCodeApplied()) {
                    if (optionalDiscount.isPresent()) {
                        Discount discount = optionalDiscount.get();
                        if (discount.getStartDate().before(new Date()) || discount.getEndDate().after(new Date())) {
                            double discountInPercentage = (double) discount.getDiscountValue() / 100;
                            order.setTotalAmount(order.getTotalAmount() - (order.getTotalAmount() * discountInPercentage));
                            order.setDiscountCodeApplied(true);
                            orderRepository.save(order);
                            return Mapper.convertToOrderResponseDto(order);
                        }
                    }
                }
            }
        }
        return null;
    }
}
