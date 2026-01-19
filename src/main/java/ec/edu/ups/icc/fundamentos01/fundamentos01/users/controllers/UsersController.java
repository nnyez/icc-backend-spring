package ec.edu.ups.icc.fundamentos01.fundamentos01.users.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos.ProductSummaryDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

    @GetMapping("/{id}/products")
    public List<ProductSummaryDto> findUserProducts(@PathVariable Long id) {
        return service.findUserProducts(id);
    }

    // @GetMapping("/{id}/products-v2")
    // public List<ProductSummaryDto> findUserProductsBy(@PathVariable Long id,
    //         @RequestParam(required = false) String name, @RequestParam(required = false) Double minPrice,
    //         @RequestParam(required = false) Double maxPrice, @RequestParam(required = false) Long categoryId) {
    //     return service.findUserProducts(id, name, minPrice, maxPrice, categoryId);
    // }
    @GetMapping("/{id}/products-v2")
    public List<ProductSummaryDto> findUserProductsBy(@PathVariable Long userId,
            @RequestParam(required = false) String name, @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice, @RequestParam(required = false) Long categoryId) {
        return service.findUserProducts(userId, name, minPrice, maxPrice, categoryId);
    }

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody CreateUserDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @Valid @RequestBody UpdateUserDto dto) {
        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public Object partialUpdate(@PathVariable Long id, @Valid @RequestBody PartialUpdateUserDto dto) {
        return service.partialUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}