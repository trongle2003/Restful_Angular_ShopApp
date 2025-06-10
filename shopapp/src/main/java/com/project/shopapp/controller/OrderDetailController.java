package com.project.shopapp.controller;

import com.project.shopapp.domain.dtos.OrderDetailDTO;
import com.project.shopapp.domain.entity.OrderDetail;
import com.project.shopapp.service.OrderDetailService;
import com.project.shopapp.ultil.anotation.ApiMessage;
import com.project.shopapp.ultil.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/order_details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @PostMapping("")
    @ApiMessage("Create OrderDetail")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult bindingResult) throws InvalidException {
        if (bindingResult.hasErrors()) {
            throw new InvalidException(bindingResult.getFieldError().getDefaultMessage().toString());
        }
        OrderDetail newOrderDetail = this.orderDetailService.handleCreateOrderDetail(orderDetailDTO);
        return ResponseEntity.ok().body(newOrderDetail);
    }

    @GetMapping("/{id}")
    @ApiMessage("Get OrderDetail By Id")
    public ResponseEntity<?> getOrderDetailById(@PathVariable long id) {
        Optional<OrderDetail> orderDetail = this.orderDetailService.handleGetOrderDetailById(id);
        return ResponseEntity.ok().body(orderDetail);
    }

    @GetMapping("order/{order_id}")
    @ApiMessage("Get OrderDetail By OrderId")
    public ResponseEntity<?> getOrderDetailByOrderId(@PathVariable("order_id") long id) {
        List<Optional<OrderDetail>> orderDetailList = this.orderDetailService.handleGetOrderDetailByOrderId(id);
        return ResponseEntity.ok().body(orderDetailList);
    }

    @PutMapping("/{id}")
    @ApiMessage("Update OrderDetail")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable long id, @RequestBody OrderDetailDTO orderDetailDTO) throws InvalidException {
        boolean check = this.orderDetailService.handleCheckOrderDetail(id);
        if (!check) {
            throw new InvalidException("Id does not exists");
        }
        OrderDetail updateOrderDetail = this.orderDetailService.handleUpdateOrderDetail(id, orderDetailDTO);
        return ResponseEntity.ok().body(updateOrderDetail);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete OrderDetail")
    public ResponseEntity<?> deleleOrderDetail(@Valid @PathVariable long id) throws InvalidException {
        this.orderDetailService.handleDeleteOrderDetail(id);
        return ResponseEntity.ok().body("Delete successfuly order detail id " + id);
    }
}
