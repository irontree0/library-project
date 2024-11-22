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
public class AuthorCreateDto {

    @Size(min = 1, max = 10)
    @NotBlank(message =  "Need to set name")
    private String name;

    @NotBlank(message = "Need to set surname")
    private String surname;
}