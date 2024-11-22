package ru.itgirl.library_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    private Long id;
    @Size(min = 3, max = 30)
    @NotBlank(message = "Need to set name")
    private String name;
    @NotBlank(message = "Need to set password")
    private String password;
    @NotBlank(message = "Admin, set the role for user")
    private String role;
}
