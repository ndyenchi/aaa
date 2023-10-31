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
@Table(name = "order_supplier")
public class OrderSupplierEntity {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Basic(optional = false)
    @Column(name = "supplier_name")
    private String supplierName;
    @Basic(optional = false)
    @Column(name = "order_date")
    private Long orderDate;
    @Basic(optional = false)
    @Column(name = "delivery_date")
    private Long deliveryDate;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "orderSupplierId")
    private List<OrderSupplierDetailEntity> orderSupplierDetailList;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UsersEntity employeeId;
    @OneToOne(cascade = CascadeType.REFRESH, mappedBy = "orderSupplierId")
    private ImportFormEntity importForm;
}
