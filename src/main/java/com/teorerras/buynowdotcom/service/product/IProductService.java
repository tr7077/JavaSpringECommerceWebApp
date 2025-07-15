package com.teorerras.buynowdotcom.service.product;

import com.teorerras.buynowdotcom.model.Product;
import com.teorerras.buynowdotcom.request.AddProductRequest;
import com.teorerras.buynowdotcom.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest request);
    Product updateProducts(UpdateProductRequest request, Long productId);
    Product getProductById(Long productId);
    void deleteProductById(Long productId);

    List<Product> getAllProducts();
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByBrandAndName(String brand, String name);
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByName(String name);
}
