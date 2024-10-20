package ru.itgirl.library_project.dto;
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
    private String name;
    private Long genreId;
    private Set<Long> authorIds;
}
