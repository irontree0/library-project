package ru.itgirl.library_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.itgirl.library_project.controller.rest.BookRestController;
import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;
import ru.itgirl.library_project.service.BookService;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @InjectMocks
    private BookRestController bookRestController;


    @Test
    public void testGetBookById() throws Exception {
        Long bookId = 6L;
        BookDto bookDto = BookDto.builder()
                .id(bookId)
                .name("Test Book")
                .genre("Fiction")
                .authors("John Doe")
                .build();

        when(bookService.getBookById(bookId)).thenReturn(bookDto);

        mockMvc.perform(get("/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookDto.getId()))
                .andExpect(jsonPath("$.name").value(bookDto.getName()))
                .andExpect(jsonPath("$.genre").value(bookDto.getGenre()))
                .andExpect(jsonPath("$.authors").value(bookDto.getAuthors()));
    }

    @Test
    public void testGetBookByName() throws Exception {
        String bookName = "Test Book";
        BookDto bookDto = BookDto.builder()
                .id(1L)
                .name(bookName)
                .genre("Fiction")
                .authors("John Doe")
                .build();

        when(bookService.getByNameV1(bookName)).thenReturn(bookDto);

        mockMvc.perform(get("/book").param("name", bookName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookDto.getId()))
                .andExpect(jsonPath("$.name").value(bookDto.getName()))
                .andExpect(jsonPath("$.genre").value(bookDto.getGenre()))
                .andExpect(jsonPath("$.authors").value(bookDto.getAuthors()));
    }

    @Test
    public void testCreateBook() throws Exception {
        BookCreateDto bookCreateDto = BookCreateDto.builder()
                .name("New Book")
                .genreId(1L)
                .authorIds(Set.of(1L, 2L))
                .build();

        BookDto bookDto = BookDto.builder()
                .id(1L)
                .name(bookCreateDto.getName())
                .genre("Fiction")
                .authors("John Doe, Jane Doe")
                .build();

        when(bookService.createBook(any(BookCreateDto.class))).thenReturn(bookDto);

        mockMvc.perform(post("/book/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookDto.getId()))
                .andExpect(jsonPath("$.name").value(bookDto.getName()))
                .andExpect(jsonPath("$.genre").value(bookDto.getGenre()))
                .andExpect(jsonPath("$.authors").value(bookDto.getAuthors()));
    }

    @Test
    public void testUpdateBook() throws Exception {
        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .id(1L)
                .name("Updated Book")
                .genreId(2L)
                .authorIds(Set.of(2L))
                .build();

        BookDto bookDto = BookDto.builder()
                .id(bookUpdateDto.getId())
                .name(bookUpdateDto.getName())
                .genre("Non-Fiction")
                .authors("Jane Doe")
                .build();

        when(bookService.updateBook(any(BookUpdateDto.class))).thenReturn(bookDto);

        mockMvc.perform(put("/book/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookDto.getId()))
                .andExpect(jsonPath("$.name").value(bookDto.getName()))
                .andExpect(jsonPath("$.genre").value(bookDto.getGenre()))
                .andExpect(jsonPath("$.authors").value(bookDto.getAuthors()));
    }

    @Test
    public void testDeleteBook() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/book/delete/{id}", bookId))
                .andExpect(status().isOk());
    }
}
