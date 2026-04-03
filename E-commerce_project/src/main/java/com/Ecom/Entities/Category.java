package com.Ecom.Entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private  String imageUrl;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products=new LinkedHashSet<>();
}