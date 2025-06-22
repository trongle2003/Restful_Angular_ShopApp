package com.project.shopapp.controller;

import com.project.shopapp.domain.dtos.OrderDTO;
import com.project.shopapp.domain.entity.Order;
import com.project.shopapp.domain.entity.User;
import com.project.shopapp.service.OrderService;
import com.project.shopapp.service.UserService;
import com.project.shopapp.ultil.anotation.ApiMessage;
import com.project.shopapp.ultil.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("")
    @ApiMessage("Create Order")
    @Transactional
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult bindingResult) throws InvalidException {
        if (bindingResult.hasErrors()) {
            throw new InvalidException(bindingResult.getFieldError().getDefaultMessage().toString());
        }
        User user = this.userService.handleCheckUser(orderDTO.getUserId())
                .orElseThrow(() -> new InvalidException("User not found"));
        Order newOrder = this.orderService.handleCreateOrder(orderDTO);
        return ResponseEntity.ok().body(newOrder);
    }

    @GetMapping("/{user_id}")
    @ApiMessage("Get Order By UserId")
    public ResponseEntity<?> getOrders(@PathVariable("user_id") Long userId) throws InvalidException {
        if (userId == null || userId <= 0) {
            throw new InvalidException("User ID must be positive and not null");
        }
        List<Order> orders = this.orderService.handleGetOrderByUserId(userId);
        return ResponseEntity.ok().body(orders);
    }

    @PutMapping("/{id}")
    @ApiMessage("Update Order")
    @Transactional
    public ResponseEntity<?> updateOrder(@Valid @RequestBody OrderDTO orderDTO, @PathVariable long id, BindingResult bindingResult) throws InvalidException {
        if (bindingResult.hasErrors()) {
            throw new InvalidException(bindingResult.getFieldError().getDefaultMessage().toString());
        }
        boolean check = this.orderService.handleCheckOrderById(id);
        if (!check) {
            throw new InvalidException("Id does not exists");
        }

        User user = this.userService.handleCheckUser(orderDTO.getUserId())
                .orElseThrow(() -> new InvalidException("User not found"));
        Order updateOrder = this.orderService.handleUpdateOrder(id, orderDTO);
        return ResponseEntity.ok().body(updateOrder);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete By Id")
    @Transactional
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        this.orderService.handleDeleteOrder(id);
        return ResponseEntity.ok().body("Delete order successfully");
    }
}
