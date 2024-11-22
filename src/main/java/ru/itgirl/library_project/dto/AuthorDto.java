package ru.itgirl.library_project.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class AuthorDto {

    private Long id;

    @Size(min = 1, max = 10)
    @NotBlank(message = "Need to set name")
    private String name;
    @NotBlank(message = "Need to set surname")
    private String surname;

    private List<BookDto> books;
}