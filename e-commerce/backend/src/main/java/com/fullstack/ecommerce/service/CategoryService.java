package com.fullstack.ecommerce.service;

import com.fullstack.ecommerce.dto.CategoryDto;
import com.fullstack.ecommerce.entity.Category;
import com.fullstack.ecommerce.exception.ResourceNotFoundException;
import com.fullstack.ecommerce.mapper.CategoryMapper;
import com.fullstack.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class managing product category operations for the e-commerce platform.
 * 
 * <p>This service provides comprehensive category management functionality including
 * creating, reading, updating, and deleting product categories. Categories are used
 * to organize products and enable filtered browsing experiences for customers.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Complete CRUD operations for product categories</li>
 *   <li>Category listing and retrieval by ID</li>
 *   <li>Category creation with name and description</li>
 *   <li>Category updates with validation</li>
 *   <li>Category deletion with existence validation</li>
 *   <li>Transaction management for data consistency</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Categories serve as organizational units for product classification</li>
 *   <li>All category operations are transactional to ensure data integrity</li>
 *   <li>Category existence is validated before update and delete operations</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    /** Repository for category persistence operations */
    private final CategoryRepository categoryRepository;
    
    /** Mapper for converting between Category entities and DTOs */
    private final CategoryMapper categoryMapper;

    /**
     * Retrieves all product categories in the system.
     * 
     * <p>This method fetches all available categories and converts them to DTOs
     * for client consumption. Useful for populating category navigation menus
     * and filter options.</p>
     * 
     * @return list of {@link CategoryDto} objects representing all categories
     * 
     * @see CategoryDto
     */
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a specific category by its unique identifier.
     * 
     * @param id the unique identifier of the category to retrieve
     * @return {@link CategoryDto} containing the category details
     * @throws ResourceNotFoundException if the category with the given ID does not exist
     * 
     * @see CategoryDto
     */
    public CategoryDto getById(Long id) {
        Category c = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", id));
        return categoryMapper.toDto(c);
    }

    /**
     * Creates a new product category in the system.
     * 
     * <p>This method creates a new category with the provided name and description.
     * The category can then be used to classify and organize products.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that category creation is atomic and consistent.</p>
     * 
     * @param name the name of the category (required)
     * @param description the description of the category (optional, can be null)
     * @return {@link CategoryDto} containing the created category details including generated ID
     * 
     * @see CategoryDto
     */
    @Transactional
    public CategoryDto create(String name, String description) {
        // Build new category entity
        Category c = Category.builder().name(name).description(description).build();
        
        // Persist category to database
        c = categoryRepository.save(c);
        
        // Convert to DTO and return
        return categoryMapper.toDto(c);
    }

    /**
     * Updates an existing category's name and description.
     * 
     * <p>This method allows modification of category details. The category must
     * exist in the system before it can be updated.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that category updates are atomic and consistent.</p>
     * 
     * @param id the unique identifier of the category to update
     * @param name the new name for the category
     * @param description the new description for the category
     * @return {@link CategoryDto} containing the updated category details
     * @throws ResourceNotFoundException if the category with the given ID does not exist
     * 
     * @see CategoryDto
     */
    @Transactional
    public CategoryDto update(Long id, String name, String description) {
        // Retrieve category and validate existence
        Category c = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", id));
        
        // Update category properties
        c.setName(name);
        c.setDescription(description);
        
        // Persist changes
        c = categoryRepository.save(c);
        
        // Convert to DTO and return
        return categoryMapper.toDto(c);
    }

    /**
     * Deletes a category from the system.
     * 
     * <p>This method permanently removes a category after validating its existence.
     * The operation is transactional to ensure data consistency.</p>
     * 
     * <p><b>Note:</b> Consider the impact on associated products before deleting
     * a category. Products referencing this category may need to be updated or
     * reassigned to another category.</p>
     * 
     * @param id the unique identifier of the category to delete
     * @throws ResourceNotFoundException if the category with the given ID does not exist
     */
    @Transactional
    public void delete(Long id) {
        // Validate category existence before deletion
        if (!categoryRepository.existsById(id)) throw new ResourceNotFoundException("Category", id);
        
        // Permanently delete the category
        categoryRepository.deleteById(id);
    }
}
