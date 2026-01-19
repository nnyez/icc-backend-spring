package ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.entities;

import java.util.HashSet;
import java.util.Set;

import ec.edu.ups.icc.fundamentos01.fundamentos01.core.entities.BaseModel;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.entities.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseModel {

   
    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    // ============== RELACIÓN BIDIRECCIONAL N:N ==============
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Relación inversa con Product
     * mappedBy = "categories" hace referencia al atributo en ProductEntity
     */
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();

    // Constructores y getters/setters
    
    public Set<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductEntity> products) {
        this.products = products != null ? products : new HashSet<>();
    }

    // ============== MÉTODOS DE CONVENIENCIA ==============
    
    public void addProduct(ProductEntity product) {
        this.products.add(product);    }

    public void removeProduct(ProductEntity product) {
        this.products.remove(product);

    }
}
