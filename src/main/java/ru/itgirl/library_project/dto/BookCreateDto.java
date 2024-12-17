package ru.itgirl.library_project.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookCreateDto {
    @Size(min = 3, max = 10)
    @NotBlank(message = "Need to set name")
    private String name;
    @NotNull(message = "Need to set a genre's ID")
    private Long genreId;
    @NotNull(message = "Need to set author's or authors' ID(s)")
    private Set<Long> authorIds;
}
