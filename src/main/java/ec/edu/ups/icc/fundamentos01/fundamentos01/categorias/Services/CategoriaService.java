package ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.Services;

import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos.CreateCategoriaDto;

public interface CategoriaService {
        public CategoriaResponseDto create(CreateCategoriaDto dto);
}