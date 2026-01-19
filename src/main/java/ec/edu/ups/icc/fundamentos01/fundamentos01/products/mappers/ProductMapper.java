package ec.edu.ups.icc.fundamentos01.fundamentos01.products.mappers;

import java.util.ArrayList;
import java.util.List;

import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos.ProductSummaryDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.models.Product;

public class ProductMapper {

    public static ProductResponseDto toResponse(Product product) {
        ProductResponseDto dto = new ProductResponseDto(
                (long) product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStock());
        return dto;
    }

    public static Product fromCreateDto(CreateProductDto dto) {
        return new Product(dto.name, dto.description, dto.price, dto.stock);
    }

    public static ProductResponseDto toResponseDto(ProductEntity entity) {
        ProductResponseDto dto = new ProductResponseDto();

        // Campos básicos del producto
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.price = entity.getPrice();
        dto.description = entity.getDescription();

        // Crear objeto User anidado (se carga LAZY)
        ProductResponseDto.UserSummaryDto userDto = new ProductResponseDto.UserSummaryDto();
        userDto.id = entity.getOwner().getId();
        userDto.name = entity.getOwner().getName();
        userDto.email = entity.getOwner().getEmail();
        dto.user = userDto;

        // Crear objeto Category anidado (se carga LAZY)
        List<CategoriaResponseDto> categoryDto = new ArrayList<>();
        entity.getCategories().forEach(c -> {
            CategoriaResponseDto response = new CategoriaResponseDto(c.getId(), c.getName(), c.getDescription());
            categoryDto.add(response);
        });

        dto.categories = categoryDto;
        // Auditoría
        dto.createdAt = entity.getCreatedAt();
        dto.updatedAt = entity.getUpdatedAt();

        return dto;
    }
    public static ProductSummaryDto toResponseSummaryDto(ProductEntity entity) {
        ProductSummaryDto dto = new ProductSummaryDto();

        // Campos básicos del producto
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.price = entity.getPrice();
        dto.description = entity.getDescription();

        // Crear objeto Category anidado (se carga LAZY)
        List<CategoriaResponseDto> categoryDto = new ArrayList<>();
        entity.getCategories().forEach(c -> {
            CategoriaResponseDto response = new CategoriaResponseDto(c.getId(), c.getName(), c.getDescription());
            categoryDto.add(response);
        });

        dto.categories = categoryDto;

        return dto;
    }
}
