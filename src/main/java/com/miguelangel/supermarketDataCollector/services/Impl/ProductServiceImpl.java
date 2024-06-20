package com.miguelangel.supermarketDataCollector.services.Impl;

import java.util.*;

import com.miguelangel.supermarketDataCollector.dao.IUserDAO;
import com.miguelangel.supermarketDataCollector.dto.ProductDTO;
import com.miguelangel.supermarketDataCollector.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.miguelangel.supermarketDataCollector.dao.IProductDAO;
import com.miguelangel.supermarketDataCollector.entity.Product;
import com.miguelangel.supermarketDataCollector.services.IProductService;

import jakarta.transaction.Transactional;

/**
 * Implementation of the IProductService interface that provides operations for managing products.
 *
 *  @since 2024
 *  @author Miguel Angel Moreno Garcia
 */
@Service
public class ProductServiceImpl implements IProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final IProductDAO productDAO;
    private final IUserDAO userDAO;
    private final EntityManager entityManager;

    @Autowired
    public ProductServiceImpl(IProductDAO productDAO, IUserDAO userDAO, EntityManager entityManager) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.entityManager = entityManager;
    }

    /**
     * Saves a product in the system.
     *
     * @param product The product to save.
     * @return true if the product was saved successfully, false otherwise.
     */
    @Override
    public boolean save(Product product) {
        try {
            productDAO.save(product);
        } catch (IllegalArgumentException i) {
            logger.error("Product to save was null in save: " + i.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("A general exception happened when saving the product: {}", product.getId());
            return false;
        }
        return true;
    }

    /**
     * Saves a set of products in the system.
     *
     * @param productList The set of products to save.
     * @return true if all products were saved successfully, false otherwise.
     */
    @Override
    public boolean saveAllProducts(Set<Product> productList) {
        try {
            productDAO.saveAll(productList);
        } catch (IllegalArgumentException i) {
            logger.error("Product to save was null in saveAllProducts: " + i.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("A general exception happened when saving the list of products: {}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Sets or removes a product as a favorite for a user.
     * @param productId The unique identifier of the product.
     * @param userId The unique identifier of the user.
     * @return true if the operation was successful, false otherwise.
     */
    @Override
    public boolean setProductFavourite(String productId, int userId) {
        Optional<Product> product;
        Optional<UserEntity> user;
        try {
            product = productDAO.findById(productId);
            user = userDAO.findById(userId);
        } catch (Exception e) {
            logger.info("Fail retrieving either Product or UserEntity: {}", e.getMessage());
            return false;
        }

        if (user.isPresent() && product.isPresent()) {
            Product productLiked = product.get();
            Set<UserEntity> users = productLiked.getFavouriteUserEntities();
            boolean foundProduct = users.stream().anyMatch(userEntity -> userEntity.getId() == userId);
            if (foundProduct) {
                productLiked.getFavouriteUserEntities().removeIf(u -> u.getId() == userId);
            } else {
                productLiked.getFavouriteUserEntities().add(user.get());
            }
            try {
                productDAO.save(productLiked);
                return true;
            } catch (Exception e) {
                logger.info("Fail saving the user who liked the product: {}", e.getMessage());
                return false;
            }
        } else {
            logger.info("UserEntity or Product not found when liked the product");
            return false;
        }
    }

    /**
     * Retrieves the list of favorite products for the specified user.
     * @param userId The unique identifier of the user.
     * @return A list of favorite products for the user.
     */
    @Override
    public List<Product> getFavouriteProducts(int userId) {
        return productDAO.findFavouriteProducts(userId);
    }
    /**
     * Retrieves the number of times a product has been added to a shopping list by the specified user.
     * @param productId The unique identifier of the product.
     * @param userId The unique identifier of the user.
     * @return The number of times the product has been added to a shopping list.
     * @throws RuntimeException If an error occurs while retrieving the information.
     */
    @Override
    public Long getTimesProductInShoppingList(String productId, int userId) {
        Query query = entityManager.createNamedQuery("User.timesProductAddedToShoppingList");
        query.setParameter("productId", productId);
        query.setParameter("userId", userId);
        try {
            return (Long) query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Retrieves the price variation for a product based on the user's preferences.
     * @param productId The unique identifier of the product.
     * @param userId The unique identifier of the user.
     * @return The price variation for the product.
     * @throws RuntimeException If an error occurs while retrieving the information.
     */
    @Override
    public Double getPriceVariation(String productId, Integer userId) {
        Query query = entityManager.createNativeQuery("SELECT calculate_price_variation(:userId, :productId)");

        query.setParameter("userId", userId);
        query.setParameter("productId", productId);

        try {
            return (Double) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Finds products based on specified filters.
     *
     * @param filters A map of filters to apply.
     * @return A list of products matching the filters.
     */
    @Override
    public Page<ProductDTO> findBy(Map<String, Object> filters, int page, int size) {
        List<ProductDTO> pageResultListDTO = getProductList(filters, page, size);
        // Count total products
        Long totalCount = getTotalCount(filters);
        // Create a Page object
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(pageResultListDTO, pageable, totalCount.intValue());
    }

    /**
     * Retrieves the total count of products based on the specified filters.
     *
     * @param filters A map of filters to apply.
     * @return The total count of products matching the filters.
     */
    @Transactional
    private Long getTotalCount(Map<String, Object> filters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> product = countQuery.from(Product.class);
        countQuery.select(cb.count(product));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(product.get("available"), true));

        // Add conditions based on filters
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            if (filter.getValue() != null) {
                switch (filter.getKey()) {
                    case "productId" -> predicates.add(cb.equal(product.get("id"), filter.getValue()));
                    case "categoryId" -> predicates.add(cb.equal(product.get("category").get("id"), filter.getValue()));
                    case "supermarketIds" ->
                            predicates.add(product.get("supermarket").get("id").in((Collection<?>) filter.getValue()));
                    case "productName" ->
                            predicates.add(cb.like(cb.lower(product.get("name")), "%" + filter.getValue() + "%"));
                    case "onSale" -> predicates.add(cb.equal(product.get("onSale"), filter.getValue()));
                }
            }
        }
        countQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    /**
     * Retrieves a list of products based on specified filters, with pagination support.
     *
     * @param filters A map of filters to apply.
     * @param page The page number.
     * @param size The number of items per page.
     * @return A list of products matching the filters and pagination criteria, represented as ProductDTO objects.
     */
    @Transactional
    private List<ProductDTO> getProductList(Map<String, Object> filters, int page, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(product.get("available"), true));

        // Add conditions based on filters
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            if (filter.getValue() != null) {
                switch (filter.getKey()) {
                    case "productId" -> predicates.add(cb.equal(product.get("id"), filter.getValue()));
                    case "categoryId" -> predicates.add(cb.equal(product.get("category").get("id"), filter.getValue()));
                    case "supermarketIds" ->
                            predicates.add(product.get("supermarket").get("id").in((Collection<?>) filter.getValue()));
                    case "productName" ->
                            predicates.add(cb.like(cb.lower(product.get("name")), "%" + filter.getValue() + "%"));
                    case "onSale" -> predicates.add(cb.equal(product.get("onSale"), filter.getValue()));
                }
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<Order> orders = new ArrayList<>();

        if (filters.get("priceSort") != null) {
            switch ((Integer) filters.get("priceSort")) {
                case 1 -> orders.add(cb.asc(product.get("priceHistories").get("price")));
                case 2 -> orders.add(cb.desc(product.get("priceHistories").get("price")));
            }
        }
        if (filters.get("alphabeticSort") != null) {
            switch ((Integer) filters.get("alphabeticSort")) {
                case 1 -> orders.add(cb.asc(product.get("name")));
                case 2 -> orders.add(cb.desc(product.get("name")));
            }
        }
        if (!orders.isEmpty()) {
            cq.orderBy(orders);
        }


        TypedQuery<Product> query = entityManager.createQuery(cq);

        // Pagination
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<Product> resultList = query.getResultList();
        return resultList.stream().map(ProductDTO::new).toList();
    }


    /**
     * Finds a product by its unique identifier.
     *
     * @param id The unique identifier of the product.
     * @return An Optional that may contain the found product, or empty if not found.
     */
    @Override
    public Optional<Product> findById(String id) {
        return productDAO.findById(id);
    }

    /**
     * Finds products by their unique identifiers.
     *
     * @param ids List of product unique identifiers.
     * @return List of found products.
     */
    @Override
    public List<Product> findByIds(List<String> ids) {
        return (List<Product>) productDAO.findAllById(ids);
    }
}
