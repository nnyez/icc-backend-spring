package ec.edu.ups.icc.fundamentos01.fundamentos01.products.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.fundamentos01.exception.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.repositories.UserRepository;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    public ProductServiceImpl(
            ProductRepository productRepo,
            UserRepository userRepo,
            CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<ProductResponseDto> findAll() {

        // 1. El repositorio devuelve entidades JPA (ProductEntity)
        return productRepo.findAll()
                .stream()

                // 2. Cada ProductEntity se transforma en un modelo de dominio Product
                // Aquí se desacopla la capa de persistencia de la lógica de negocio
                .map(Product::fromEntity)

                // 3. El modelo de dominio se convierte en DTO de respuesta
                // Solo se exponen los campos permitidos por la API
                .map(ProductMapper::toResponse)

                // 4. Se recopila el resultado final como una lista de DTOs
                .toList();
    }

    @Override
    public ProductResponseDto findOne(int id) {
        return productRepo.findById((long) id)
                .map(Product::fromEntity)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        // 1. VALIDAR EXISTENCIA DE RELACIONES
        UserEntity owner = userRepo.findById(dto.userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + dto.userId));

        CategoryEntity category = categoryRepo.findById(dto.categoryId)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + dto.categoryId));

        // Regla: nombre único
        if (productRepo.findByName(dto.name).isPresent()) {
            throw new IllegalStateException("El nombre del producto ya está registrado");
        }

        // 2. CREAR MODELO DE DOMINIO
        Product product = ProductMapper.fromCreateDto(dto);

        // 3. CONVERTIR A ENTIDAD CON RELACIONES
        ProductEntity entity = product.toEntity(owner, category);

        // 4. PERSISTIR
        ProductEntity saved = productRepo.save(entity);

        // 5. CONVERTIR A DTO DE RESPUESTA
        return toResponseDto(saved);
    }

private ProductResponseDto toResponseDto(ProductEntity entity) {
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
        CategoriaResponseDto categoryDto = new CategoriaResponseDto();
        categoryDto.id = entity.getCategory().getId();
        categoryDto.name = entity.getCategory().getName();
        categoryDto.description = entity.getCategory().getDescription();
        dto.category = categoryDto;
        
        // Auditoría
        dto.createdAt = entity.getCreatedAt();
        dto.updatedAt = entity.getUpdatedAt();
        
        return dto;
    }
    @Override
    public ProductResponseDto update(int id, UpdateProductDto dto) {
        try {
            return productRepo.findById((long) id)
                    // Entity → Domain
                    .map(Product::fromEntity)

                    // Aplicar cambios permitidos en el dominio
                    .map(product -> product.update(dto))

                    // Domain → Entity
                    .map(Product::toEntity)

                    // Persistencia
                    .map(productRepo::save)

                    // Entity → Domain
                    .map(Product::fromEntity)

                    // Domain → DTO
                    .map(ProductMapper::toResponse)

                    // Error controlado si no existe
                    .orElseThrow(() -> new BadRequestException("Producto no encontrado"));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Datos inválidos: " + e.getMessage());
        }
    }

    @Override
    public ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto) {
        try {
            return productRepo.findById((long) id)
                    // Entity → Domain
                    .map(Product::fromEntity)

                    // Aplicar solo los cambios presentes
                    .map(product -> product.partialUpdate(dto))

                    // Domain → Entity
                    .map(Product::toEntity)

                    // Persistencia
                    .map(productRepo::save)

                    // Entity → Domain
                    .map(Product::fromEntity)

                    // Domain → DTO
                    .map(ProductMapper::toResponse)

                    // Error si no existe
                    .orElseThrow(() -> new BadRequestException("Producto no encontrado"));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Datos inválidos: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        // Verifica existencia y elimina
        productRepo.findById((long) id)
                .ifPresentOrElse(
                        productRepo::delete,
                        () -> {
                            throw new BadRequestException("Producto no encontrado");
                        });
    }

    @Override
    public void validateName(int id, String name) {
        productRepo.findByName(name)
                .ifPresent(existingProduct -> {
                    if (existingProduct.getId() != (long) id) {
                        throw new ConflictException("El nombre ya está registrado");
                    }
                });

    }

    // @Override
    // public ProductResponseDto secureUpdate(int id, String name, double price,
    // String reason) {
    // // if(productRepo.findById((long) id).isPresent())

    // }

}
