package com.Ecom.Services.Impl;
import com.Ecom.Dtos.ProdResponse;
import com.Ecom.Dtos.ProductDto;
import com.Ecom.Entities.Product;
import com.Ecom.Exceptions.ResourceNotFoundException;
import com.Ecom.Repositories.ProductRepository;
import com.Ecom.Services.ProductService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;


    @Override
    public ProductDto createProduct(ProductDto productDto) {
        var product = modelMapper.map(productDto, Product.class);
        var savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public Product udpateProduct(Long productId, Product product) {
        var oldProduct = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found in db"));
        oldProduct.setTitle(product.getTitle());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setShort_description(product.getShort_description());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setLive(product.isLive());
        oldProduct.setOutOfStock(product.isOutOfStock());
        oldProduct.setImage(product.getImage());
        return productRepository.save(oldProduct);

    }

    @Override
    public ProdResponse<Product> getAll(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> pagePrdoucts = productRepository.findAll(pageable);
        return ProdResponse.of(pagePrdoucts);

    }

    @Override
    public Product get(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found !!"));
    }

    @Override
    public void delete(Long productId) {
        var product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("product not found !!"));
        productRepository.delete(product);
    }
}