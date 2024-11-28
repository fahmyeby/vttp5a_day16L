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
        List<Book> books = bookService.findAll(); // get all books from service
        JsonArrayBuilder jsonArray = Json.createArrayBuilder(); // create json array to store books

        //convert each book data to json format
        for (Book book : books){
            JsonObject jsonObj = Json.createObjectBuilder()
            .add("id", book.getId()) //add variables
            .add("title", book.getTitle())
            .add("author", book.getAuthor())
            .add("price", book.getPrice())
            .add("quantity", book.getQuantity())
            .build(); //create json object

            jsonArray.add(jsonObj); //add json object to the array
        }
        return ResponseEntity.ok().body(jsonArray.build().toString());
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody String payload){
        try {
            //convert json string to reader
            JsonReader jReader = Json.createReader(new StringReader(payload));
            //parse json string to object
            JsonObject jObject = jReader.readObject();

            //create new book and set properties from json
            Book book = new Book();
            book.setId(jObject.getInt("id"));
            book.setTitle(jObject.getString("title"));
            book.setAuthor(jObject.getString("author"));
            book.setPrice(jObject.getJsonNumber("price").doubleValue());
            book.setQuantity(jObject.getInt("quantity"));

            //save book thru service
            bookService.add(book);
            return ResponseEntity.ok().body("true");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
}
