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

@Service
public class CategoryServiceImpl implements CategoryService {

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


    // Actualiza la descripción de una categoría existente
     public Category updateCategory(Long categoryId, String description) throws CategoryDuplicateException {
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);
        if (existingCategory.isEmpty()) {
            throw new RuntimeException("Categoria no encontrada");
        }
        // Verifica si ya existe otra categoría con la nueva descripción
        if(!categoryRepository.findByDescription(description).isEmpty()) {
            throw new CategoryDuplicateException();
        }
        // Actualiza la descripción y guarda
        Category category = existingCategory.get();
        category.setDescription(description);
        return categoryRepository.save(category);

    }

     // Elimina una categoría por su ID.
    public void deleteCategory(Long categoryId) {
        // Elimina la categoría de la base de datos
        categoryRepository.deleteById(categoryId);
    }

}
