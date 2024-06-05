package com.example.uade.tpo.service;

import com.example.uade.tpo.entity.ProductsCategories;
import com.example.uade.tpo.repository.IProductCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoriesService {

    @Autowired
    private IProductCategoriesRepository productCategoryRepository;

    public List<Long> getProductsIdByCategoryId(Long categoryId) {
        List<Long> categoryIds = productCategoryRepository.findAllCategoryIds();
        if (!categoryIds.contains(categoryId)) {
            return null;
        }
        return productCategoryRepository.getProductsIdByCategoryId(categoryId);
    }

    public void deleteProduct(Long productId) {
        List<ProductsCategories> productsCategories = productCategoryRepository.findAll();
        for (ProductsCategories productsCategory : productsCategories) {
            if (productsCategory.getProductId().equals(productId)) {
                productCategoryRepository.delete(productsCategory);
            }
        }
    }

    public void deleteCategory(Long categoryId) {
        List<ProductsCategories> productsCategories = productCategoryRepository.findAll();
        for (ProductsCategories productsCategory : productsCategories) {
            if (productsCategory.getCategoryId().equals(categoryId)) {
                Long noValid = (long) -1;
                productsCategory.setCategoryId(noValid);
                productCategoryRepository.save(productsCategory);
            }
        }
    }

}
