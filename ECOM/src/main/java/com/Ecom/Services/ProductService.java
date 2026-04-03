package com.Ecom.Services;

import com.Ecom.Dtos.ProdResponse;
import com.Ecom.Dtos.ProductDto;
import com.Ecom.Entities.Product;

public interface ProductService {



    ProductDto createProduct(ProductDto product);
    //update the product
    Product udpateProduct(Long productId, Product product);

    ProdResponse<Product> getAll(int page, int size, String sortBy, String sortDir);

    Product get(Long productId);

    void delete(Long productId);
}