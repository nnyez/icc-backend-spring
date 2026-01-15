package ec.edu.ups.icc.fundamentos01.fundamentos01.categorias.dtos;


public class CategoriaResponseDto {
    public Long id;
    public String name;
    public String description;

    public CategoriaResponseDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public CategoriaResponseDto() {
        //TODO Auto-generated constructor stub
    }

}
