package com.teorerras.buynowdotcom.repository;

import com.teorerras.buynowdotcom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.brand = :brand")
    List<Product> findByCategoryAndBrand(String category, String brand);

    @Query("SELECT p FROM Product p WHERE p.category.name = :name")
    List<Product> findByCategory(@Param("name") String category);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    List<Product> findByBrandAndName(String brand, String name);

    List<Product> findByBrand(String brand);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByName(String name);

    boolean existsByNameAndBrand(String name, String brand);
}
