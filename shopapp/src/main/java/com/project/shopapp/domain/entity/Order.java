package com.project.shopapp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.shopapp.ultil.constant.StatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String fullName;
    private String email;

    @NotBlank(message = "Phone number is required")
    @Min(value = 5, message = "Phone number must be at least 5 characters")
    private String phoneNumber;
    private String address;
    private String note;
    private LocalDate orderDate;
    private StatusEnum status;

    @Min(value = 0, message = "Total money must be >= 0")
    private double totalMoney;
    private String shippingMethod;
    private String shippingAddress;
    private LocalDate shippingDate;
    private String trackingNumber;
    private String paymentMethod;
    private boolean active;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderDetail> orderDetails;
}
