package com.example.AOManager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_customer")
public class OrderCustomerEntity {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Basic(optional = false)
    @Column(name = "order_date")
    private Long orderDate;
    @Basic(optional = false)
    @Column(name = "delivery_name")
    private String deliveryName;
    @Basic(optional = false)
    @Column(name = "delivery_address")
    private String deliveryAddress;
    @Basic(optional = false)
    @Column(name = "delivery_phone")
    private String deliveryPhone;
    @Basic(optional = false)
    @Column(name = "delivery_email")
    private String deliveryEmail;
    @Basic(optional = false)
    @Column(name = "delivery_date")
    private Long deliveryDate;
    @Basic(optional = false)
    @Column(name = "total_price")
    private Long totalPrice;
    @Basic(optional = false)
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "orderCustomerId")
    private List<CartDetailEntity> cartDetailList;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UsersEntity customerId;
    @JoinColumn(name = "approve_employee_id", referencedColumnName = "id")
    @ManyToOne
    private UsersEntity approveEmployeeId;
    @JoinColumn(name = "delivery_employee_id", referencedColumnName = "id")
    @ManyToOne
    private UsersEntity deliveryEmployeeId;
    @JoinColumn(name = "cancel_employee_id", referencedColumnName = "id")
    @ManyToOne
    private UsersEntity cancelEmployeeId;
    @JoinColumn(name = "order_status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private OrderStatusEntity orderStatusId;
}
