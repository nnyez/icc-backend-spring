package ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateCategoriaDto {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    public String name;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 3, max = 300, message = "La descripción debe tener entre 3 y 300 caracteres")
    public String description;
}
