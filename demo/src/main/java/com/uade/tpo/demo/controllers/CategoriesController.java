package com.uade.tpo.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.dto.CategoryRequest;
import com.uade.tpo.demo.entity.dto.CategoryResponseDTO;
import com.uade.tpo.demo.entity.dto.CategorySimpleDTO;
import com.uade.tpo.demo.entity.dto.ErrorResponseDTO;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;
import com.uade.tpo.demo.service.CategoryService;
import com.uade.tpo.demo.exceptions.CategoryNotFoundException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    // Defino el endpoint para obtener todas las categorias
    @GetMapping
    public ResponseEntity<List<CategorySimpleDTO>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategorySimpleDTO> dtoList = new ArrayList<>();
        for (Category cat : categories) {
            dtoList.add(new CategorySimpleDTO(
                cat.getCategoryId(),
                cat.getName(),
                cat.getDescription()
            ));
        }
        return ResponseEntity.ok(dtoList);
    }

    // Defino el endpoint para obtener una categoria por id
    @GetMapping("/{categoryId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> result = categoryService.getCategoryById(categoryId);
        if (result.isPresent()) {
            Category cat = result.get();
            CategorySimpleDTO dto = new CategorySimpleDTO(
                cat.getCategoryId(),
                cat.getName(),
                cat.getDescription()
            );
            return ResponseEntity.ok(dto);
        }
        ErrorResponseDTO error = new ErrorResponseDTO(false, "Categoria no encontrada");
        return ResponseEntity.status(404).body(error);
    }

    // Defino el endpoint para crear una nueva categoria
    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody CategoryRequest categoryRequest) {
        try {
            Category result = categoryService.createCategory(categoryRequest.getName(), categoryRequest.getDescription());
            CategoryResponseDTO response = new CategoryResponseDTO(
                true,
                "Categoria creada exitosamente",
                result.getCategoryId(),
                result.getName(),
                result.getDescription()
            );
            return ResponseEntity.created(URI.create("/categories/" + result.getCategoryId())).body(response);
        } catch (CategoryDuplicateException e) {
            ErrorResponseDTO error = new ErrorResponseDTO(false, "La categoría ya existe");
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Defino el endpoint para actualizar una categoria existente
    @PutMapping("/{categoryId}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequest categoryRequest) {
        try {
            Category updated = categoryService.updateCategory(categoryId, categoryRequest.getName(), categoryRequest.getDescription());
            CategoryResponseDTO dto = new CategoryResponseDTO(
                true,
                "Categoria actualizada exitosamente",
                updated.getCategoryId(),
                updated.getName(),
                updated.getDescription()
            );
            return ResponseEntity.ok(dto);
        } catch (CategoryDuplicateException e) {
            ErrorResponseDTO error = new ErrorResponseDTO(false, "La categoría ya existe");
            return ResponseEntity.badRequest().body(error);
        } catch (CategoryNotFoundException e) {
            ErrorResponseDTO error = new ErrorResponseDTO(false, e.getMessage());
            return ResponseEntity.status(404).body(error);
        }
    }

    // Defino el endpoint para eliminar una categoria existente
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long categoryId) {
        Optional<Category> result = categoryService.getCategoryById(categoryId);
        if (result.isPresent()) {
            Category cat = result.get();
            categoryService.deleteCategory(categoryId);
            CategoryResponseDTO response = new CategoryResponseDTO(
                true,
                "Categoría eliminada exitosamente",
                cat.getCategoryId(),
                cat.getName(),
                cat.getDescription()
            );
            return ResponseEntity.ok(response);
        } else {
            ErrorResponseDTO error = new ErrorResponseDTO(false, "Categoria no encontrada");
            return ResponseEntity.status(404).body(error);
        }
    }
}
