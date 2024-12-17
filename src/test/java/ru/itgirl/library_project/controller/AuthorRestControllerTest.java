package ru.itgirl.library_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.library_project.dto.AuthorCreateDto;
import ru.itgirl.library_project.dto.AuthorDto;
import ru.itgirl.library_project.dto.AuthorUpdateDto;
import ru.itgirl.library_project.service.AuthorService;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthorRestControllerTest {

    @MockBean
    private AuthorService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testGetAuthorById() throws Exception {
        Long id = 1L;
        AuthorDto authorDto = AuthorDto.builder()
                .id(id)
                .name("Александр")
                .surname("Пушкин")
                .build();

        when(service.getAuthorById(id)).thenReturn(authorDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/author/{id}", id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByName() throws Exception {
        String authorSurname = "Doe";
        AuthorDto authorDto = AuthorDto.builder()
                .id(1L)
                .surname(authorSurname)
                .name("John")
                .build();

        when(service.getAuthorByNameV1(authorSurname)).thenReturn(authorDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/author")
                        .param("surname", authorSurname))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()));
    }

    @Test
    public void testGetAuthorByNameV2() throws Exception {
        String name = "John";
        String surname = "Doe";
        AuthorDto authorDto = AuthorDto.builder()
                .id(1L)
                .name(name)
                .surname(surname)
                .build();

        when(service.getAuthorByNameV2(name, surname)).thenReturn(authorDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/author/v2")
                        .param("name", name)
                        .param("surname", surname))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByNameV3() throws Exception {
        String name = "John";
        String surname = "Doe";
        AuthorDto authorDto = AuthorDto.builder()
                .id(1L)
                .name(name)
                .surname(surname)
                .build();

        when(service.getAuthorByNameV3(name, surname)).thenReturn(authorDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/author/v3")
                        .param("name", name)
                        .param("surname", surname))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testCreateAuthor() throws Exception {
        AuthorDto authorDto = AuthorDto.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .build();

        AuthorCreateDto authorCreateDto = AuthorCreateDto.builder()
                .name("John")
                .surname("Doe")
                .build();

        when(service.createAuthor(authorCreateDto)).thenReturn(authorDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authorDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        AuthorDto updatedAuthor = AuthorDto.builder()
                .id(1L)
                .name("John")
                .surname("Doe Updated")
                .build();

        AuthorUpdateDto authorUpdateDto = AuthorUpdateDto.builder()
                .id(1L)
                .name("John")
                .surname("Doe Updated")
                .build();

        when(service.updateAuthor(authorUpdateDto)).thenReturn(updatedAuthor);

        mockMvc.perform(MockMvcRequestBuilders.put("/author/update")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authorUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedAuthor.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedAuthor.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(updatedAuthor.getSurname()));
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/author/delete/{id}", id))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteAuthor(id);
    }
}
