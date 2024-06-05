package com.example.uade.tpo.controller;

import com.example.uade.tpo.dtos.request.*;
import com.example.uade.tpo.dtos.response.*;
import com.example.uade.tpo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CartService cartService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SellerService sellerService;

    // User management
    @GetMapping("/users") //Get all users
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}") //Get user by id
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        Optional<UserResponseDto> user = userService.getUserById(userId);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/users") //Create user
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userDto) {
        UserResponseDto newUser = userService.createUser(userDto);
        if (newUser == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}") //Update user
    public ResponseEntity<UserResponseDto> updateUser
            (@PathVariable Long userId, @RequestBody UserRequestDto userDetails) {
        UserResponseDto updatedUser = userService.updateUser(userId, userDetails);
        if (updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}") //Delete user
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        Boolean deleted = userService.deleteUser(userId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Category management
    @GetMapping("/categories")//Get all categories
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        List<CategoryResponseDto> categories = categoryService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")//Get category by id
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long categoryId) {
        Optional<CategoryResponseDto> category = categoryService.getCategoryById(categoryId);
        return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/categories")//Create category
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto categoryDto) {
        CategoryResponseDto newCategory = categoryService.createCategory(categoryDto);
        if (newCategory == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @PutMapping("/categories/{categoryId}")//Update category
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long categoryId,
                                                              @RequestBody CategoryRequestDto categoryDetails) {
        CategoryResponseDto updatedCategory = categoryService.updateCategory(categoryId, categoryDetails);
        if (updatedCategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}")//Delete category
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        Boolean deleted = categoryService.deleteCategory(categoryId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/categories/{categoryId}/product/{productId}")//Add product to category
    public ResponseEntity<Void> addProductToCategory(@PathVariable Long categoryId, @PathVariable Long productId) {
        Boolean add = categoryService.addProductToCategory(categoryId, productId);
        if (!add) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Seller management
    @GetMapping("/sellers") //Get all sellers
    public ResponseEntity<List<SellerResponseDto>> getAllSellers() {
        List<SellerResponseDto> sellers = sellerService.getSellers();
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }

    @GetMapping("/sellers/{SellerId}") //Get seller by id
    public ResponseEntity<SellerResponseDto> getSellersById(@PathVariable Long SellerId) {
        Optional<SellerResponseDto> seller = sellerService.getSellerById(SellerId);
        return seller.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/sellers") //Create seller
    public ResponseEntity<SellerResponseDto> createSeller(@RequestBody SellerRequestDto sellerDto) {
        SellerResponseDto newSeller = sellerService.createSeller(sellerDto);
        if (newSeller == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(newSeller, HttpStatus.CREATED);
    }

    @PutMapping("/sellers/{SellerId}") //Update seller
    public ResponseEntity<SellerResponseDto> updateSeller
            (@PathVariable Long SellerId, @RequestBody SellerRequestDto sellerDetails) {
        SellerResponseDto updatedSeller = sellerService.updateSeller(SellerId, sellerDetails);
        if (updatedSeller == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
    }

    @DeleteMapping("/sellers/{SellerId}") //Delete seller
    public ResponseEntity<Void> deleteSeller(@PathVariable Long SellerId) {
        Boolean deleted = sellerService.deleteSeller(SellerId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Cart management
    @GetMapping("/carts/{userId}") //Get cart by user id
    public ResponseEntity<CartResponseDto> getCartByUserId(@PathVariable Long userId) {
        Optional<CartResponseDto> cart = cartService.getCartByUserId(userId);
        return cart.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/carts/{userId}/add") //Add item to cart
    public ResponseEntity<CartResponseDto> addItemToCart(@PathVariable Long userId, @RequestBody CartItemRequestDto cartItem) {
        CartResponseDto cart = cartService.addItemToCart(userId, cartItem);
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{userId}/remove/{productId}") //Remove item from cart
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        Boolean deleted = cartService.removeItemFromCart(userId, productId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Order management
    @GetMapping("/orders/{orderId}") //Get order by id
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        Optional<OrderResponseDto> order = orderService.getOrderById(orderId);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/orders/user/{userId}") //Get orders by user id
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getOrderByUserId(userId);
        if(orders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping ("/orders/{cartId}")//Create order from cart
    public ResponseEntity<OrderResponseDto> createOrderFromCart(@PathVariable Long cartId) {
        OrderResponseDto newOrder = orderService.createOrderFromCart(cartId);
        if(newOrder == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @DeleteMapping("/orders/delete/{orderId}") //Delete order
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        Boolean deleted = orderService.deleteOrder(orderId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/orders/{orderId}/discount/{code}") //Apply discount to order
    public ResponseEntity<OrderResponseDto> applyDiscountToOrder(@PathVariable Long orderId, @PathVariable String code) {
        OrderResponseDto order = orderService.applyDiscountToOrder(orderId, code);
        if(order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // Product management
    @GetMapping("/products") //Get all products
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}") //Get product by id
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        Optional<ProductResponseDto> product = productService.getProductById(productId);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/products") //Create product
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productDto) {
        ProductResponseDto newProduct = productService.createProduct(productDto);
        if (newProduct == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/products/{productId}") //Update product
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long productId,
                                                            @RequestBody ProductRequestDto productDetails) {
        ProductResponseDto updatedProduct = productService.updateProduct(productId, productDetails);
        if (updatedProduct == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}") //Delete product
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        Boolean deleted = productService.deleteProduct(productId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/products/seller/{sellerId}") //Get products by seller
    public ResponseEntity<List<ProductResponseDto>> getProductsBySellerId(@PathVariable Long sellerId) {
        List<ProductResponseDto> products = productService.getProductsBySellerId(sellerId);
        if(products.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/category/{categoryId}") //Get products by category
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategoryId(@PathVariable Long categoryId) {
        List<ProductResponseDto> products = productService.getProductsByCategoryId(categoryId);
        if(products.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Discount management
    @GetMapping("/discounts") //Get all discounts
    public ResponseEntity<List<DiscountResponseDto>> getDiscounts() {
        List<DiscountResponseDto> discounts = discountService.getDiscounts();
        return new ResponseEntity<>(discounts, HttpStatus.OK);
    }

    @GetMapping("/discounts/{discountId}") //Get discount by id
    public ResponseEntity<DiscountResponseDto> getDiscountById(@PathVariable Long discountId) {
        Optional<DiscountResponseDto> discount = discountService.getDiscount(discountId);
        return discount.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/discounts") //Create discount
    public ResponseEntity<DiscountResponseDto> createDiscount(@RequestBody DiscountRequestDto discount) {
        DiscountResponseDto newDiscount = discountService.createDiscount(discount);
        if(newDiscount == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newDiscount, HttpStatus.CREATED);
    }

    @PutMapping("/discounts/{discountId}") //Update discount
    public ResponseEntity<DiscountResponseDto> updateDiscount
            (@PathVariable Long discountId, @RequestBody DiscountRequestDto discount) {
        DiscountResponseDto updatedDiscount = discountService.updateDiscount(discountId, discount);
        if(updatedDiscount == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedDiscount, HttpStatus.OK);
    }

    @DeleteMapping("/discounts/{discountId}") //Delete discount
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long discountId) {
        Boolean deleted = discountService.deleteDiscount(discountId);
        if(!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/discounts/code/{code}") //Delete discount by code
    public ResponseEntity<Void> deleteDiscountByCode(@PathVariable String code) {
        Boolean deleted = discountService.deleteDiscountByCode(code);
        if(!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Order detail management
    @GetMapping("/orderDetails/{orderId}") //Get orderDetail by id
    public ResponseEntity<OrderDetailResponseDto> getOrderDetailById(@PathVariable Long orderId) {
        Optional<OrderDetailResponseDto> orderDetail = orderDetailService.getOrderDetailById(orderId);
        return orderDetail.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/orderDetails/{orderId}") //Update orderDetail
    public ResponseEntity<OrderDetailResponseDto> updateOrderDetail
            (@PathVariable Long orderId, @RequestBody OrderDetailRequestDto orderDetail) {
        OrderDetailResponseDto orderDetailResponseDto = orderDetailService.updateOrderDetail(orderId, orderDetail);
        if(orderDetailResponseDto == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(orderDetailResponseDto, HttpStatus.OK);
    }

    // Payment management
    @PostMapping("/payments/process/order/{orderId}")//Process payment
    public ResponseEntity<PaymentResponseDto> processPayment(@PathVariable Long orderId) {
        PaymentResponseDto payment = paymentService.processPayment(orderId);
        if (payment == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @PutMapping("/payments/confirmCard/{paymentId}")//Confirm payment with card
    public ResponseEntity<PaymentResponseDto> confirmCardPayment
            (@PathVariable Long paymentId, @RequestBody CardRequestDto cardPaymentMethod) {
        PaymentResponseDto paymentResponseDto = paymentService.confirmCardPayment(paymentId, cardPaymentMethod);
        if (paymentResponseDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }

    @PutMapping("/payments/confirmMP/{paymentId}")//Confirm payment with MercadoPago
    public ResponseEntity<PaymentResponseDto> confirmMPPayment
            (@PathVariable Long paymentId, @RequestBody MPRequestDto MercadoPagoPaymentMethod) {
        PaymentResponseDto paymentResponseDto = paymentService.confirmMPPayment(paymentId, MercadoPagoPaymentMethod);
        if (paymentResponseDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }

    @PostMapping("/payments/cancel/{paymentId}")//Cancel payment
    public ResponseEntity<PaymentResponseDto> cancelPayment(@PathVariable Long paymentId) {
        PaymentResponseDto paymentResponseDto = paymentService.cancelPayment(paymentId);
        if (paymentResponseDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }

    @GetMapping("/payments/{paymentId}")//Get payment by id
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long paymentId) {
        Optional<PaymentResponseDto> payment = paymentService.getPaymentById(paymentId);
        return payment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/payments/history/{userId}")//Get payment history by user id
    public ResponseEntity<List<PaymentResponseDto>> getPaymentHistory(@PathVariable Long userId) {
        Optional<List<PaymentResponseDto>> paymentHistory = Optional.ofNullable
                (paymentService.getPaymentHistory(userId));
        return paymentHistory.map(paymentResponseDtos -> new ResponseEntity<>(paymentResponseDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
