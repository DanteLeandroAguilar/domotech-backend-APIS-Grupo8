package com.uade.tpo.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;
import com.uade.tpo.demo.repository.CategoryRepository;
import com.uade.tpo.demo.exceptions.CategoryNotFoundException;

@Service
public class CategoryServiceImpl implements CategoryService {
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> getCategories(PageRequest pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    // Crea una nueva categoría si no existe una con la misma descripción
    public Category createCategory(String name, String description) throws CategoryDuplicateException {
        // Busca si ya existe una categoría con la misma descripción
       if (!categoryRepository.findByDescription(description).isEmpty()) {
            throw new CategoryDuplicateException();
        }
        Category category = new Category(name, description);
        return categoryRepository.save(category);
    }


    // Actualiza el nombre y la descripción de una categoría existente
    public Category updateCategory(Long categoryId, String name, String description) throws CategoryDuplicateException {
    Optional<Category> existingCategory = categoryRepository.findById(categoryId);
    if (existingCategory.isEmpty()) {
        throw new CategoryNotFoundException();
    }
    // Valida igual que createCategory: no permitir descripción duplicada (excluyendo la actual)
    List<Category> categoriesWithDescription = categoryRepository.findByDescription(description);
    for (Category cat : categoriesWithDescription) {
        if (!cat.getCategoryId().equals(categoryId)) {
            throw new CategoryDuplicateException();
        }
    }
    // Actualiza el nombre y la descripción y guarda
    Category category = existingCategory.get();
    category.setName(name);
    category.setDescription(description);
    return categoryRepository.save(category);
}

     // Elimina una categoría por su ID.
    public void deleteCategory(Long categoryId) {
        // Elimina la categoría de la base de datos
        categoryRepository.deleteById(categoryId);
    }

}
