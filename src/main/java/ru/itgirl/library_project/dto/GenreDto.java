package ru.itgirl.library_project.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GenreDto {
    private Long id;
    @NotBlank(message = "Need to set name")
    private String name;

    private List<BookDto> books;
}