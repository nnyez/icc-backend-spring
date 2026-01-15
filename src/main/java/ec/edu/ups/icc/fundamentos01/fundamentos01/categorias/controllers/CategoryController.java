package ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.Services.CategoriaService;
import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.Services.CategoriaServiceImpl;
import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos.CreateCategoriaDto;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoriaService service;
    
    public CategoryController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDto> create(@Valid @RequestBody CreateCategoriaDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }
    
}
