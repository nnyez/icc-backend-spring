package ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos;

import java.time.LocalDateTime;
import java.util.List;

import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos.CategoriaResponseDto;

public class ProductSummaryDto {

    public Long id;
    public String name;
    public Double price;
    public String description;
    public Integer stock;

    // ============== OBJETOS ANIDADOS ==============

 

    public ProductSummaryDto(Long id, String name, Double price, String description, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }

    public ProductSummaryDto() {
        //TODO Auto-generated constructor stub
    }

    public List<CategoriaResponseDto> categories;




}
