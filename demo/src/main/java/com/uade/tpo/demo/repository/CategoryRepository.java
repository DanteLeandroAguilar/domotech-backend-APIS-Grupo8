package com.uade.tpo.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Devuelve una lista de categorías que coinciden con el nombre dado
    @Query(value = "SELECT c FROM Category c WHERE c.description = ?1")
    List<Category> findByDescription(String description);

    // Busca categorías por nombre exacto
    List<Category> findByName(String name);

    // Busca categorías que contengan una palabra clave en la descripción
    @Query(value = "SELECT c FROM Category c WHERE c.description LIKE %?1%")
    List<Category> findByDescriptionContaining(String keyword);

    // Lista todas las categorías ordenadas por nombre ascendente
    List<Category> findAllByOrderByNameAsc();

    // Lista todas las categorías ordenadas por nombre descendente
    List<Category> findAllByOrderByNameDesc();

}
