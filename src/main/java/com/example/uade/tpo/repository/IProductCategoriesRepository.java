package com.example.uade.tpo.repository;

import com.example.uade.tpo.entity.ProductsCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductCategoriesRepository extends JpaRepository<ProductsCategories, Long> {
    @Query("SELECT pc.productId FROM ProductsCategories pc WHERE pc.categoryId = :categoryId")
    List<Long> getProductsIdByCategoryId(Long categoryId);
    @Query("SELECT pc.categoryId FROM ProductsCategories pc")
    List<Long> findAllCategoryIds();
}

