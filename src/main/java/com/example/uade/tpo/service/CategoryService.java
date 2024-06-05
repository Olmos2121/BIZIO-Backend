package com.example.uade.tpo.service;

import com.example.uade.tpo.Utils.Mapper;
import com.example.uade.tpo.dtos.request.CategoryRequestDto;
import com.example.uade.tpo.dtos.response.CategoryResponseDto;
import com.example.uade.tpo.entity.Category;
import com.example.uade.tpo.entity.Product;
import com.example.uade.tpo.entity.ProductsCategories;
import com.example.uade.tpo.repository.ICategoryRepository;
import com.example.uade.tpo.repository.IProductCategoriesRepository;
import com.example.uade.tpo.repository.IProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IProductCategoriesRepository productCategoriesRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ProductCategoriesService productCategoriesService;

    public List<CategoryResponseDto> getCategories() {
        return categoryRepository.findAll().stream().map(Mapper::convertToCategoryResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<CategoryResponseDto> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).map(Mapper::convertToCategoryResponseDto);
    }

    public CategoryResponseDto createCategory(CategoryRequestDto categoryDto) {
        Category category = new Category();
        List<String> categories = categoryRepository.findAll().stream().map(Category::getName).toList();
        if (categories.contains(categoryDto.getName())) {
            return null;
        }
        category.setName(categoryDto.getName());
        return Mapper.convertToCategoryResponseDto(categoryRepository.save(category));
    }

    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto categoryDetails) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(categoryDetails.getName());
            return Mapper.convertToCategoryResponseDto(categoryRepository.save(category));
        }
        return null;
    }

    @Transactional
    public Boolean deleteCategory(Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            productCategoriesService.deleteCategory(categoryId);
            categoryRepository.deleteById(categoryId);
            return true;
        }
        return false;
    }

    public Boolean addProductToCategory(Long categoryId, Long productId) {
        ProductsCategories productCategory = new ProductsCategories();
        List<Product> products = productRepository.findAll();
        List<Long> productsId = products.stream().map(Product::getId).toList();
        if (!productsId.contains(productId) && !categoryRepository.existsById(categoryId)) {
            return false;
        }
        productCategory.setProductId(productId);
        productCategory.setCategoryId(categoryId);
        productCategoriesRepository.save(productCategory);
        return true;
    }

    public Boolean changeProductCategory(Long categoryId, Long productId) {
        List<ProductsCategories> productsCategories = productCategoriesRepository.findAll();
        for (ProductsCategories productsCategory : productsCategories) {
            if (productsCategory.getProductId().equals(productId)) {
                productsCategory.setCategoryId(categoryId);
                productCategoriesRepository.save(productsCategory);
                return true;
            }
        }
        return false;
    }
}
