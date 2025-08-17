package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.CategoryDTO;

import java.util.List;

/**
 * Service interface for managing Category entities.
 * Provides methods to perform CRUD operations on categories.
 */
public interface CategoryService {

    /**
     * Creates a new category in the system.
     *
     * @param categoryDTO the category details to create
     * @return the created CategoryDTO with generated ID and timestamps
     */
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    /**
     * Retrieves a list of all categories.
     *
     * @return a list of CategoryDTOs
     */
    List<CategoryDTO> getAllCategories();

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category
     * @return the CategoryDTO corresponding to the given ID
     */
    CategoryDTO getCategoryById(Long id);

    /**
     * Updates the details of an existing category.
     *
     * @param id the ID of the category to update
     * @param categoryDTO the updated category details
     * @return the updated CategoryDTO
     */
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     */
    void deleteCategory(Long id);
}