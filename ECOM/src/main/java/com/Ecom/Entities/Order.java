package com.Ecom.Entities;
import com.Ecom.Entities.Enums.OrderStatus;
import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PLACED;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();


    @OneToOne(mappedBy = "order")
    private Payment payment;

    public void setShippingAddress(String shippingAddress) {
        if (this.address == null) {
            this.address = new Address();
        }
        this.address.setStreet(shippingAddress);
    }

    private BigDecimal totalAmount;


}