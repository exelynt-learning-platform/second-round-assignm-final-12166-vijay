package com.Ecom.Entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long productId;


    private  String title;
    private  String description;
    private  String short_description;
    private  double price;
    private  boolean live;
    private  boolean outOfStock;
    private Integer stockQuantity = 0;
    private String imageUrl;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    private  FileMetaData image;


    @ManyToMany
    private Set<Category> categories=new LinkedHashSet<>();


}