package com.uade.tpo.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.dto.CategoryRequest;
import com.uade.tpo.demo.entity.dto.CategoryResponseDTO;
import com.uade.tpo.demo.entity.dto.CategorySimpleDTO;
import com.uade.tpo.demo.entity.dto.ErrorResponseDTO;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;
import com.uade.tpo.demo.service.CategoryService;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<Page<CategorySimpleDTO>> getCategories(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        Page<Category> categories;
        if (page == null || size == null) {
            categories = categoryService.getCategories(PageRequest.of(0, Integer.MAX_VALUE));
        } else {
            categories = categoryService.getCategories(PageRequest.of(page, size));
        }
        // Mapeo cada Category a CategorySimpleDTO (sin message y success)
        Page<CategorySimpleDTO> dtoPage = categories.map(cat -> new CategorySimpleDTO(
            cat.getCategoryId(),
            cat.getName(),
            cat.getDescription()
        ));
        return ResponseEntity.ok(dtoPage);
    }

    // Defino el endpoint para obtener una categoria por id
    @GetMapping("/{categoryId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> result = categoryService.getCategoryById(categoryId);
        if (result.isPresent()) {
            Category cat = result.get();
            CategoryResponseDTO dto = new CategoryResponseDTO(
                true,
                null,
                cat.getCategoryId(),
                cat.getName(),
                cat.getDescription()
            );
            return ResponseEntity.ok(dto);
        }
        ErrorResponseDTO error = new ErrorResponseDTO(false, "Category not found");
        return ResponseEntity.status(404).body(error);
    }

    // Defino el endpoint para crear una nueva categoria
    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody CategoryRequest categoryRequest) {
        try {
            Category result = categoryService.createCategory(categoryRequest.getName(), categoryRequest.getDescription());
            // Construyo el DTO de respuesta exitosa (incluye datos de la categoría)
            CategoryResponseDTO response = new CategoryResponseDTO(
                true,
                "Category created successfully",
                result.getCategoryId(),
                result.getName(),
                result.getDescription()
            );
            return ResponseEntity.created(URI.create("/categories/" + result.getCategoryId())).body(response);
        } catch (CategoryDuplicateException e) {
            // Devuelvo el error usando ErrorResponseDTO
            ErrorResponseDTO error = new ErrorResponseDTO(false, "Category already exists");
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Defino el endpoint para actualizar una categoria existente
    @PutMapping("/{categoryId}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequest categoryRequest)
            throws CategoryDuplicateException {
        Category updated = categoryService.updateCategory(categoryId, categoryRequest.getDescription());
        CategoryResponseDTO dto = new CategoryResponseDTO(
            true,
            "Category updated successfully",
            updated.getCategoryId(),
            updated.getName(),
            updated.getDescription()
        );
        return ResponseEntity.ok(dto);
    }

    // Defino el endpoint para eliminar una categoria existente
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        ErrorResponseDTO response = new ErrorResponseDTO(true, "Category deleted successfully");
        return ResponseEntity.ok(response);
    }
}
