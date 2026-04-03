package com.Ecom.Repositories;

import com.Ecom.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long> {

}
