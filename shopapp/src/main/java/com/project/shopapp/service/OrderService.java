package com.project.shopapp.service;

import com.project.shopapp.domain.dtos.OrderDTO;
import com.project.shopapp.domain.entity.Order;
import com.project.shopapp.domain.entity.User;
import com.project.shopapp.repository.OrderRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.ultil.constant.StatusEnum;
import com.project.shopapp.ultil.error.InvalidException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public Order handleCreateOrder(OrderDTO orderDTO) throws InvalidException {
        Order order = new Order();
        User user = this.userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new InvalidException("Cannot find user with id: " + orderDTO.getUserId()));
        // Tạo 1 luồng ánh xạ để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Cập nhật các trường của đơn hàng từ orderDTO
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus(StatusEnum.PENDING);
        //Kiểm tra shipping date phải lớn hơn hoặc bằng ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new InvalidException("Date must be at least today");
        }
        order.setActive(true);
        return this.orderRepository.save(order);
    }

    public List<Order> handleGetOrderByUserId(Long id) {
        return this.orderRepository.findByUserId(id);
    }

    public Order handleUpdateOrder(Long id, OrderDTO orderDTO) throws InvalidException {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new InvalidException("Cannot find order with id: " + id));
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new InvalidException("Cannot find user with id: " + id));
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        return this.orderRepository.save(order);
    }

    public boolean handleCheckOrderById(long id) {
        return this.orderRepository.existsById(id);
    }

    public Order handleDeleteOrder(long id) {
        Optional<Order> orderOptional = this.orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order currentOrder = orderOptional.get();
            currentOrder.setActive(false);
            return this.orderRepository.save(currentOrder);
        }
        return null;
    }
}
