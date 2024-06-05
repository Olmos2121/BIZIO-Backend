package com.example.uade.tpo.service;

import com.example.uade.tpo.Utils.Mapper;
import com.example.uade.tpo.dtos.request.OrderDetailRequestDto;
import com.example.uade.tpo.dtos.response.OrderDetailResponseDto;
import com.example.uade.tpo.entity.OrderDetail;
import com.example.uade.tpo.repository.IOrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {

    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    public Optional<OrderDetailResponseDto> getOrderDetailById(Long orderId) {
        return orderDetailRepository.findById(orderId).map(Mapper::convertToOrderDetailResponseDto);
    }

    public OrderDetailResponseDto updateOrderDetail(Long orderDetailId, OrderDetailRequestDto orderDetail){
        Optional<OrderDetail> optionalOrderDetail = orderDetailRepository.findById(orderDetailId);
        if(optionalOrderDetail.isPresent()){
            OrderDetail orderDetailToUpdate = optionalOrderDetail.get();
            orderDetailToUpdate.setQuantity(orderDetail.getQuantity());
            orderDetailToUpdate.setPrice(orderDetail.getPrice());
            orderDetailToUpdate.setTotal(orderDetail.getTotal());
            return Mapper.convertToOrderDetailResponseDto(orderDetailRepository.save(orderDetailToUpdate));
        }
        return null;
    }

}
