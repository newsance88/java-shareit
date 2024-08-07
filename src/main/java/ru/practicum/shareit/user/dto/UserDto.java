package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserDto {
    Long id;
    String name;
    @Email(message = "Email should be valid")
    String email;
}
