package ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos;

import java.time.LocalDateTime;
import java.util.List;

import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos.CategoriaResponseDto;

public class ProductResponseDto {

    public ProductResponseDto(int id2, String name2, String description2, double price2, int stock) {
        //TODO Auto-generated constructor stub
    }

    public ProductResponseDto() {
        //TODO Auto-generated constructor stub
    }

    public Long id;
    public String name;
    public Double price;
    public String description;

    // ============== OBJETOS ANIDADOS ==============
    
    public UserSummaryDto user;
    public List<CategoriaResponseDto> categories;

    // ============== AUDITORÍA ==============
    
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    // ============== DTOs INTERNOS ==============
    
    public static class UserSummaryDto {
        public Long id;
        public String name;
        public String email;
    }

}
