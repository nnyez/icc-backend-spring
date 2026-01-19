package ec.edu.ups.icc.fundamentos01.fundamentos01.users.dtos;

import java.util.List;

import ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.products.dtos.ProductSummaryDto;

public class UserResponseDto {
    public int id;
    public String name;
    public String email;
    public String password;
    public List<ProductSummaryDto> products;

    public UserResponseDto(int id, String name, String email, String password, List<ProductSummaryDto> products) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.products = products;
    }

    public UserResponseDto(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
