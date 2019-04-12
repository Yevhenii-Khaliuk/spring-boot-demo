package com.khaliuk.controllers;

import com.khaliuk.model.Category;
import com.khaliuk.services.CategoryService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @CrossOrigin("http://localhost:4200")
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = categoryService.getAll();
        ResponseEntity<List<Category>> result;
        if (categories.isEmpty()) {
            result = ResponseEntity.notFound().build();
        } else {
            result = ResponseEntity.ok(categories);
        }
        return result;
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return categoryService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public ResponseEntity<Category> create(@Valid @RequestBody Category category) {
        return categoryService.create(category)
                .map(c -> ResponseEntity.created(getUri(c.getId())).body(c))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Category> update(@RequestBody Category category, @PathVariable Long id) {
        category.setId(id);
        return categoryService.update(category)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    private URI getUri(Long id) {
        return URI.create(String.format("/category/%s", id));
    }
}
