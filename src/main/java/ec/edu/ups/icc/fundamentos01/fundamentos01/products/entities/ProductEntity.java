package ec.edu.ups.icc.fundamentos01.fundamentos01.products.entities;

import java.util.Set;
import java.util.Locale.Category;

import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.fundamentos01.core.entities.BaseModel;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.entities.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseModel {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stock;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity owner;

    // @ManyToOne(optional = false, fetch = FetchType.LAZY)
    // @JoinColumn(name = "category_id", nullable = false)
    // private CategoryEntity category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_categories", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<CategoryEntity> categories;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public void removeCategory(CategoryEntity entity) {
        this.categories.remove(entity);
    }

    public void addCategory(CategoryEntity entity) {
        this.categories.add(entity);
    }

    public void clearCategory(CategoryEntity entity) {
        this.categories.clear();
    }

}
