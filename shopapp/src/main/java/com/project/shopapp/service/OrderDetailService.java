package com.project.shopapp.service;

import com.project.shopapp.domain.dtos.OrderDetailDTO;
import com.project.shopapp.domain.entity.Order;
import com.project.shopapp.domain.entity.OrderDetail;
import com.project.shopapp.domain.entity.Product;
import com.project.shopapp.repository.OrderDetailRepository;
import com.project.shopapp.repository.OrderRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.ultil.error.InvalidException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public OrderDetail handleCreateOrderDetail(OrderDetailDTO orderDetailDTO) throws InvalidException {
        Order order = this.orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new InvalidException(
                        "Cannot find Order with id : " + orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new InvalidException(
                        "Cannot find product with id: " + orderDetailDTO.getProductId()));
        OrderDetail orderDetail = modelMapper.map(orderDetailDTO, OrderDetail.class);
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        return this.orderDetailRepository.save(orderDetail);
    }

    public Optional<OrderDetail> handleGetOrderDetailById(long id) {
        return this.orderDetailRepository.findById(id);
    }

    public List<Optional<OrderDetail>> handleGetOrderDetailByOrderId(long id) {
        return this.orderDetailRepository.findByOrderId(id);
    }

    public boolean handleCheckOrderDetail(long id) {
        return this.orderDetailRepository.existsById(id);
    }

    public OrderDetail handleUpdateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws InvalidException {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Cannot find order detail with id: " + id));
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new InvalidException("Cannot find order with id: " + id));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new InvalidException(
                        "Cannot find product with id: " + orderDetailDTO.getProductId()));
        existingOrderDetail.setPrice(Math.round(orderDetailDTO.getPrice()));
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        // tinh total money
        double total = orderDetailDTO.getPrice() * orderDetailDTO.getNumberOfProducts();
        existingOrderDetail.setTotalMoney((int) total);
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return this.orderDetailRepository.save(existingOrderDetail);
    }

    public void handleDeleteOrderDetail(long id) {
        this.orderDetailRepository.deleteById(id);
    }
}
