package com.teorerras.buynowdotcom.service.product;

import com.teorerras.buynowdotcom.model.*;
import com.teorerras.buynowdotcom.repository.CartItemRepository;
import com.teorerras.buynowdotcom.repository.CategoryRepository;
import com.teorerras.buynowdotcom.repository.OrderItemRepository;
import com.teorerras.buynowdotcom.repository.ProductRepository;
import com.teorerras.buynowdotcom.request.AddProductRequest;
import com.teorerras.buynowdotcom.request.UpdateProductRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        if(productExists(request.getName(), request.getBrand())){
            throw new EntityExistsException(request.getName() + " already exists");
        }
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
    }
    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product updateProducts(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(updatedProduct -> productRepository.save(updatedProduct))
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }
    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(product -> {
                    List<CartItem> cartItems = cartItemRepository.findByProductId(productId);
                    cartItems.forEach(cartItem -> {
                        Cart cart = cartItem.getCart();
                        cart.removeItem(cartItem);
                        cartItemRepository.delete(cartItem);
                    });

                    List<OrderItem> orderItems = orderItemRepository.findByProductId(productId);
                    orderItems.forEach(orderItem -> {
                        orderItem.setProduct(null);
                        orderItemRepository.save(orderItem);
                    });

                    Optional.ofNullable(product.getCategory().getProductList().remove(product));
                    product.setCategory(null);

                    productRepository.deleteById(product.getId());
                }, () -> {
                    throw new EntityNotFoundException("Product not found");
                });
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product not found")
        );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }
}
