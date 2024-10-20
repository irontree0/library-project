package ru.itgirl.library_project.dto;
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
    private String name;
    private Long genreId;
    private Set<Long> authorIds;
}