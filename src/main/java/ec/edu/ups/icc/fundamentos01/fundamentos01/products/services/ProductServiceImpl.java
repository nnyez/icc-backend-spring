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

import java.util.ArrayList;
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
        return productRepo.findAll()
                .stream()
                .map(Product::fromEntity) // ProductEntity → Product
                .map(Product::toResponseDto) // Product → ProductResponseDto
                .toList();
    }

    @Override
    public ProductResponseDto findOne(Long id) {
        return productRepo.findById((Long) id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public List<ProductResponseDto> findByUserId(Long userId) {

        // Validar que el usuario existe
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("Usuario no encontrado con ID: " + userId);
        }

        return productRepo.findByOwnerId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategoryId(Long categoryId) {

        // Validar que la categoría existe
        if (!categoryRepo.existsById(categoryId)) {
            throw new NotFoundException("Categoría no encontrada con ID: " + categoryId);
        }

        return productRepo.findByCategoriesId(categoryId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        // 1. VALIDAR EXISTENCIA DE RELACIONES
        UserEntity owner = userRepo.findById(dto.userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + dto.userId));

        List<CategoryEntity> categories = validateCategories(dto.categoryIds);

        // Regla: nombre único
        if (productRepo.findByName(dto.name).isPresent()) {
            throw new IllegalStateException("El nombre del producto ya está registrado");
        }

        // 2. CREAR MODELO DE DOMINIO
        Product product = ProductMapper.fromCreateDto(dto);

        // 3. CONVERTIR A ENTIDAD CON RELACIONES
        ProductEntity entity = product.toEntity(owner, categories);

        // 4. PERSISTIR
        ProductEntity saved = productRepo.save(entity);

        // 5. CONVERTIR A DTO DE RESPUESTA
        return toResponseDto(saved);
    }

    private List<CategoryEntity> validateCategories(List<Long> categoryIds) {
        List<CategoryEntity> categories = new ArrayList<>();
        categoryIds.forEach(c -> {
            CategoryEntity e = categoryRepo.findById(c).orElseThrow(
                    () -> new NotFoundException("Usuario no encontrado con ID: " + c));
            categories.add(e);
        });

        return categories;
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
        List<CategoriaResponseDto> categoryDto = new ArrayList<>();
        entity.getCategories().forEach(c -> {
            CategoriaResponseDto response = new CategoriaResponseDto(c.getId(), c.getName(), c.getDescription());
            categoryDto.add(response);
        });

        // Auditoría
        dto.createdAt = entity.getCreatedAt();
        dto.updatedAt = entity.getUpdatedAt();

        return dto;
    }

    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {
        // 1. BUSCAR PRODUCTO EXISTENTE
        ProductEntity existing = productRepo.findById((Long) id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));

        // 2. VALIDAR NUEVA CATEGORÍA (si cambió)
        List<CategoryEntity> categories = validateCategories(dto.categoryIds);

        // 3. ACTUALIZAR USANDO DOMINIO
        Product product = Product.fromEntity(existing);
        product.update(dto);

        // 4. CONVERTIR A ENTIDAD MANTENIENDO OWNER ORIGINAL
        ProductEntity updated = product.toEntity(existing.getOwner(), categories);
        updated.setId((Long) id); // Asegurar que mantiene el ID

        // 5. PERSISTIR Y RESPONDER
        ProductEntity saved = productRepo.save(updated);
        return toResponseDto(saved);
    }

    @Override
    public ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto) {
        try {
            return productRepo.findById(id)
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
    public void delete(Long id) {
        // Verifica existencia y elimina
        ProductEntity product = productRepo.findById((Long) id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));

        // Eliminación física (también se puede implementar lógica)
        productRepo.delete(product);
    }

    @Override
    public void validateName(Long id, String name) {
        productRepo.findByName(name)
                .ifPresent(existingProduct -> {
                    if (existingProduct.getId() != (Long) id) {
                        throw new ConflictException("El nombre ya está registrado");
                    }
                });

    }

    // @Override
    // public ProductResponseDto secureUpdate(int id, String name, double price,
    // String reason) {
    // // if(productRepo.findById((Long) id).isPresent())

    // }

}
