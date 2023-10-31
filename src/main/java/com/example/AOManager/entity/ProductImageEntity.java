package com.example.AOManager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_image")
public class ProductImageEntity {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne
    private ProductEntity productId;
}
