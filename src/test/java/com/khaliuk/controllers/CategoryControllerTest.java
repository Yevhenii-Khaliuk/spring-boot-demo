package com.khaliuk.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khaliuk.model.Category;
import com.khaliuk.services.CategoryService;
import java.util.Collections;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testThatGetByIdShouldReturnCategory() throws Exception {
        Long categoryId = 1L;
        Category category = new Category(1L, "Mobile Phones", "Mobile Phones", Collections.emptyList());
        String categoryJson = objectMapper.writeValueAsString(category);
        when(categoryService.getById(anyLong())).thenReturn(Optional.of(category));

        mockMvc.perform(get("/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().json(categoryJson));
    }

    @Test
    public void testThatCreateShouldReturnCategory() throws Exception {
        String uri = "/category";
        Long categoryId = 1L;
        Category category = new Category(null, "Mobile Phones", "Mobile Phones", Collections.emptyList());
        Category persisted = new Category(1L, "Mobile Phones", "Mobile Phones", Collections.emptyList());
        String categoryJson = objectMapper.writeValueAsString(category);
        String persistedJson = objectMapper.writeValueAsString(persisted);

        when(categoryService.create(any(Category.class))).thenReturn(Optional.of(persisted));

        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("%s/%s", uri, categoryId)))
                .andExpect(content().json(persistedJson));
    }

    @Test
    public void testThatCreateShouldReturn400OnEmptyCategoryName() throws Exception {
        Category category = new Category(null, null, "Mobile Phones", Collections.emptyList());
        String categoryJson = objectMapper.writeValueAsString(category);

        mockMvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isConflict());
    }
}