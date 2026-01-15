package ec.edu.ups.icc.fundamentos01.fundamentos01.users.mappers;

import java.security.PublicKey;

import ec.edu.ups.icc.fundamentos01.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.fundamentos01.users.models.User;

public class UserMapper {
    public static User toEntity(int id, String name, String email, String password) {
        return new User(id, name, email, password   );
    }

    public static UserResponseDto toResponse(User user) {
        UserResponseDto dto = new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getPassword());
        return dto;
    }

    public static User fromCreateDto(CreateUserDto dto) {
        return new User( dto.name, dto.email, dto.password);
    }
}   
