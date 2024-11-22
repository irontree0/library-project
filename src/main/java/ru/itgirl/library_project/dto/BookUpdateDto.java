package ru.itgirl.library_project.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookUpdateDto {
    private Long id;
    @Size(min = 3, max = 20)
    @NotBlank(message = "Need to set name")
    private String name;
    @NotBlank(message = "Need to set a genre's ID")
    private Long genreId;
    @NotBlank(message = "Need to set author's or authors' ID(s)")
    private Set<Long> authorIds;
}