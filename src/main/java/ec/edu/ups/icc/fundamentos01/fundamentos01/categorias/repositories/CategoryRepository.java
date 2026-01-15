package ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.entities.CategoryEntity;

@Repository
public interface CategoryRepository   extends JpaRepository<CategoryEntity, Long>{

    boolean existsByName(String name);

    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    Optional<CategoryEntity> findById(Long id);
}
