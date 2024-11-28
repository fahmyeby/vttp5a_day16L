package com.example.day16practice.controllers;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.day16practice.model.Book;
import com.example.day16practice.services.BookService;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@RestController
@RequestMapping(path = "/api/books", produces = "application/json")
public class BookRestController {
    @Autowired BookService bookService;

    @GetMapping("")
    public ResponseEntity<String> findAll(){
        List<Book> books = bookService.findAll();
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();

        for (Book book : books){
            JsonObject jsonObj = Json.createObjectBuilder()
            .add("id", book.getId())
            .add("title", book.getTitle())
            .add("author", book.getAuthor())
            .add("price", book.getPrice())
            .add("quantity", book.getQuantity()).build();

            jsonArray.add(jsonObj);
        }
        return ResponseEntity.ok().body(jsonArray.build().toString());
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody String payload){
        try {
            JsonReader jReader = Json.createReader(new StringReader(payload));
            JsonObject jObject = jReader.readObject();

            Book book = new Book();
            book.setId(jObject.getInt("id"));
            book.setTitle(jObject.getString("title"));
            book.setAuthor(jObject.getString("author"));
            book.setPrice(jObject.getJsonNumber("price").doubleValue());
            book.setQuantity(jObject.getInt("quantity"));
            bookService.add(book);
            return ResponseEntity.ok().body("true");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
}
