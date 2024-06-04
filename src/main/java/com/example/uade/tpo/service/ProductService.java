package com.example.uade.tpo.service;

import com.example.uade.tpo.Utils.Mapper;
import com.example.uade.tpo.dtos.request.ProductRequestDto;
import com.example.uade.tpo.dtos.response.ProductResponseDto;
import com.example.uade.tpo.entity.Product;
import com.example.uade.tpo.entity.Seller;
import com.example.uade.tpo.repository.IProductRepository;
import com.example.uade.tpo.repository.ISellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ProductCategoriesService productCategoriesService;

    @Autowired
    private ISellerRepository sellerRepository;

    public List<ProductResponseDto> getProducts() {
        return productRepository.findAll().stream().map
                (Mapper::convertToProductResponseDto).collect(Collectors.toList());
    }

    public Optional<ProductResponseDto> getProductById(Long productId) {
        return productRepository.findById(productId).map(Mapper::convertToProductResponseDto);
    }

    public ProductResponseDto createProduct(ProductRequestDto productDto) {
        Product product = new Product();
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            if (p.getName().equals(productDto.getName())) {
                return null;
            }
        }
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setBrand(productDto.getBrand());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setSellerId(productDto.getSellerId());
        product.setImage(productDto.getImage());

        Product savedProduct = productRepository.save(product);

        return Mapper.convertToProductResponseDto(savedProduct);
    }

    public ProductResponseDto updateProduct(Long productId, ProductRequestDto productDetails) {
        Optional<Product> productOptional = productRepository.findById(productId);
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            if (p.getName().equals(productDetails.getName())) {
                return null;
            }
        }
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(productDetails.getName());
            product.setBrand(productDetails.getBrand());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setStock(productDetails.getStock());
            product.setSellerId(productDetails.getSellerId());
            product.setImage(productDetails.getImage());
            return Mapper.convertToProductResponseDto(productRepository.save(product));
        }
        return null;
    }

    public Boolean deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            productCategoriesService.deleteProduct(productId);
            return true;
        }
        return false;
    }

    public List<ProductResponseDto> getProductsBySellerId(Long sellerId) {
        List<Long> sellerIds = sellerRepository.findAll().stream().map(Seller::getId).toList();
        if (!sellerIds.contains(sellerId)) {
            return null;
        }
        return getProducts().stream()
                .filter(product -> product.getSellerId().equals(sellerId))
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getProductsByCategoryId(Long categoryId) {
        List<Long> productsIds = productCategoriesService.getProductsIdByCategoryId(categoryId);
        if(productsIds == null || productsIds.isEmpty()){
            return null;
        }
        return productsIds.stream()
                .map(this::getProductById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

}
