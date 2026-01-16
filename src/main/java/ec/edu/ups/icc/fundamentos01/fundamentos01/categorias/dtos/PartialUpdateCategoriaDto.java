package ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos;

import jakarta.validation.constraints.Size;

public class PartialUpdateCategoriaDto {
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    public String name;

    @Size(min = 3, max = 300, message = "La descripción debe tener entre 3 y 300 caracteres")
    public String description;
}
