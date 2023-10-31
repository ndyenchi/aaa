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
@Table(name = "users")
public class UsersEntity {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "avatar")
    private String avatar;
    @Basic(optional = false)
    @Column(name = "email", unique = true)
    private String email;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @Column(name = "birthday")
    private Long birthday;
    @Basic(optional = false)
    @Column(name = "gender")
    private String gender;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "phone")
    private String phone;
    @Basic(optional = false)
    @Column(name = "status")
    private Boolean status;
    @Column(name = "token")
    private String token;
    @Basic(optional = false)
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Column(name = "token_creation_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime tokenCreationDate;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "employeeId")
    private List<OrderSupplierEntity> orderSupplierList;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "customerId")
    private List<CartDetailEntity> cartDetailList;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "employeeId")
    private List<PriceDetailEntity> priceDetailList;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "userId")
    private List<UserRoleEntity> userRoleList;
    @OneToMany(mappedBy = "approveEmployeeId")
    private List<OrderCustomerEntity> orderCustomerList;
    @OneToMany(mappedBy = "deliveryEmployeeId")
    private List<OrderCustomerEntity> orderCustomerList1;
    @OneToMany(mappedBy = "cancelEmployeeId")
    private List<OrderCustomerEntity> orderCustomerList2;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "customerId")
    private List<OrderCustomerEntity> orderCustomerList3;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "employeeId")
    private List<DeductionEntity> deductionList;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "employeeId")
    private List<ImportFormEntity> importFormList;
}
