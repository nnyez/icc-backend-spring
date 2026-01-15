package ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.Services;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos.CreateCategoriaDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.repositories.CategoryRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    
    final CategoryRepository repo;


    public CategoriaServiceImpl(CategoryRepository repo) {
        this.repo = repo;
    }


    public CategoriaResponseDto create(CreateCategoriaDto dto){
        CategoryEntity entity = new CategoryEntity();
        entity.setDescription(dto.description);
        entity.setName(dto.name);
        
        CategoryEntity save = repo.save(entity);

        return new CategoriaResponseDto(save.getId(),save.getName(),save.getDescription());

    }
}
