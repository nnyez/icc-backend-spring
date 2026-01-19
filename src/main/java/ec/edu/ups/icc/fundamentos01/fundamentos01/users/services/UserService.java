package ec.edu.ups.icc.fundamentos01.fundamentos01.users.services;

import java.util.List;

import ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos.ProductSummaryDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.dtos.UserResponseDto;

public interface UserService {

    List<UserResponseDto> findAll();

    UserResponseDto findOne(Long id);

    UserResponseDto create(CreateUserDto dto);

    Object update(Long id, UpdateUserDto dto);

    Object partialUpdate(Long id, PartialUpdateUserDto dto);

    void delete(Long id);

    List<ProductSummaryDto> findUserProducts(Long id);

    List<ProductSummaryDto> findUserProducts(Long id, String name, Double minPrice, Double maxPrice, Long categoryId);
}
