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
public class BookDto {
    private Long id;
    @Size(min = 3, max = 20)
    @NotBlank(message = "Need to set name")
    private String name;
    @NotBlank(message = "Need to set genre")
    private String genre;
    @NotBlank(message = "Need to set author or authors")
    private String authors;
}