package com.teorerras.buynowdotcom.service.category;

import com.teorerras.buynowdotcom.model.Category;
import com.teorerras.buynowdotcom.repository.CategoryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements  ICategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        if(categoryRepository.existsByName(category.getName())){
            throw new EntityExistsException(category.getName() + " already exists");
        } else {
            return categoryRepository.save(category);
        }
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        return Optional.ofNullable(findCategoryById(categoryId)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId).ifPresentOrElse(categoryRepository :: delete, () -> {
            throw new EntityNotFoundException("Category not found");
        });
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }
}
