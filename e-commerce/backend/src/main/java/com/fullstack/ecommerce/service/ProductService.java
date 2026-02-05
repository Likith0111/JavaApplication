package com.fullstack.ecommerce.service;

import com.fullstack.ecommerce.dto.ProductDto;
import com.fullstack.ecommerce.entity.Category;
import com.fullstack.ecommerce.entity.Product;
import com.fullstack.ecommerce.exception.BadRequestException;
import com.fullstack.ecommerce.exception.ResourceNotFoundException;
import com.fullstack.ecommerce.mapper.ProductMapper;
import com.fullstack.ecommerce.repository.CategoryRepository;
import com.fullstack.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class managing product operations for the e-commerce platform.
 * 
 * <p>This service provides comprehensive product management functionality including
 * product creation, updates, deletion, retrieval, and search operations. Products
 * are organized by categories and support pagination for efficient browsing.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Complete CRUD operations for products</li>
 *   <li>Product search by name with case-insensitive matching</li>
 *   <li>Category-based product filtering</li>
 *   <li>Pagination support for product listings</li>
 *   <li>Stock management and tracking</li>
 *   <li>Price management with BigDecimal precision</li>
 *   <li>Transaction management for data consistency</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Products must be associated with a valid category</li>
 *   <li>Stock levels are tracked and validated during order processing</li>
 *   <li>Product prices are stored with high precision using BigDecimal</li>
 *   <li>Search operations support partial name matching</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    /** Repository for product persistence operations */
    private final ProductRepository productRepository;
    
    /** Repository for category validation during product creation */
    private final CategoryRepository categoryRepository;
    
    /** Mapper for converting between Product entities and DTOs */
    private final ProductMapper productMapper;

    /**
     * Retrieves a specific product by its unique identifier.
     * 
     * @param id the unique identifier of the product to retrieve
     * @return {@link ProductDto} containing the product details
     * @throws ResourceNotFoundException if the product with the given ID does not exist
     * 
     * @see ProductDto
     */
    public ProductDto getById(Long id) {
        Product p = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return productMapper.toDto(p);
    }

    /**
     * Retrieves all products with pagination support.
     * 
     * <p>This method fetches products from all categories, supporting pagination
     * for efficient retrieval of large product catalogs.</p>
     * 
     * @param pageable pagination parameters (page number, size, sorting)
     * @return list of {@link ProductDto} objects representing products in the requested page
     * 
     * @see ProductDto
     * @see Pageable
     */
    public List<ProductDto> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).getContent().stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves products filtered by category with pagination support.
     * 
     * <p>This method enables category-based product browsing, allowing customers
     * to view products within a specific category.</p>
     * 
     * @param categoryId the unique identifier of the category to filter by
     * @param pageable pagination parameters (page number, size, sorting)
     * @return list of {@link ProductDto} objects representing products in the specified category
     * 
     * @see ProductDto
     * @see Pageable
     */
    public List<ProductDto> findByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable).getContent().stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Searches for products by name with case-insensitive partial matching.
     * 
     * <p>This method performs a case-insensitive search for products whose names
     * contain the provided search term. Useful for implementing product search
     * functionality in the frontend.</p>
     * 
     * @param name the search term to match against product names
     * @param pageable pagination parameters (page number, size, sorting)
     * @return list of {@link ProductDto} objects matching the search criteria
     * 
     * @see ProductDto
     * @see Pageable
     */
    public List<ProductDto> searchByName(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable).getContent().stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Creates a new product in the system.
     * 
     * <p>This method creates a new product with the provided details. The product
     * must be associated with a valid category. Stock defaults to 0 if not provided.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that product creation is atomic and consistent.</p>
     * 
     * @param name the name of the product (required)
     * @param description the description of the product
     * @param price the price of the product (required, uses BigDecimal for precision)
     * @param stock the initial stock quantity (optional, defaults to 0 if null)
     * @param categoryId the unique identifier of the category this product belongs to
     * @return {@link ProductDto} containing the created product details including generated ID
     * @throws ResourceNotFoundException if the category with the given ID does not exist
     * 
     * @see ProductDto
     */
    @Transactional
    public ProductDto create(String name, String description, BigDecimal price, Integer stock, Long categoryId) {
        // Validate category exists
        Category cat = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
        
        // Build product entity with provided details
        Product p = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .stock(stock != null ? stock : 0) // Default to 0 if stock not provided
                .category(cat)
                .build();
        
        // Persist product to database
        p = productRepository.save(p);
        
        // Convert to DTO and return
        return productMapper.toDto(p);
    }

    /**
     * Updates an existing product's details.
     * 
     * <p>This method allows modification of product information. Stock can be
     * optionally updated; if null, the existing stock value is preserved.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that product updates are atomic and consistent.</p>
     * 
     * @param id the unique identifier of the product to update
     * @param name the new name for the product
     * @param description the new description for the product
     * @param price the new price for the product
     * @param stock the new stock quantity (optional, existing value preserved if null)
     * @return {@link ProductDto} containing the updated product details
     * @throws ResourceNotFoundException if the product with the given ID does not exist
     * 
     * @see ProductDto
     */
    @Transactional
    public ProductDto update(Long id, String name, String description, BigDecimal price, Integer stock) {
        // Retrieve product and validate existence
        Product p = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", id));
        
        // Update product properties
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        
        // Update stock only if provided (preserve existing value if null)
        if (stock != null) p.setStock(stock);
        
        // Persist changes
        p = productRepository.save(p);
        
        // Convert to DTO and return
        return productMapper.toDto(p);
    }

    /**
     * Deletes a product from the system.
     * 
     * <p>This method permanently removes a product after validating its existence.
     * The operation is transactional to ensure data consistency.</p>
     * 
     * <p><b>Note:</b> Consider the impact on existing orders and cart items before
     * deleting a product. Products referenced in orders should typically be soft-deleted
     * or marked as inactive rather than hard-deleted.</p>
     * 
     * @param id the unique identifier of the product to delete
     * @throws ResourceNotFoundException if the product with the given ID does not exist
     */
    @Transactional
    public void delete(Long id) {
        // Validate product existence before deletion
        if (!productRepository.existsById(id)) throw new ResourceNotFoundException("Product", id);
        
        // Permanently delete the product
        productRepository.deleteById(id);
    }
}
