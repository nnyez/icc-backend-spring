package ec.edu.ups.icc.fundamentos01.fundamentos01.products.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.fundamentos01.fundamentos01.products.entities.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);

    List<ProductEntity> findByOwnerId(Long userId);

    @Query("SELECT DISTINCT p FROM ProductEntity p " +
            "LEFT JOIN p.categories c " +
            "WHERE p.owner.id = :userId " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:categoryId IS NULL OR c.id = :categoryId) " +
            "ORDER BY p.name ASC ")
    List<ProductEntity> findByOwnerIdWithFilters(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("categoryId") Long categoryId);

    List<ProductEntity> findByOwnerIdAndNameAndPriceBetweenAndCategoriesId(Long userId, String name, Double minPrice,
            Double maxPrice,
            Long categoryId);

    List<ProductEntity> findByOwnerName(String name);

    List<ProductEntity> findByCategoriesId(Long categoryId);

    List<ProductEntity> findByCategoriesName(String name);

    List<ProductEntity> findByCategoriesIdAndPriceGreaterThan(Long categoryId, Double price);

}
