package com.example.day16practice.services;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.day16practice.Utils.Util;
import com.example.day16practice.model.Book;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

@Service
public class BookRestService {
    private RestTemplate restTemplate = new RestTemplate();

    public List<Book> getAllBooks() {
        ResponseEntity<String> response = restTemplate.getForEntity(Util.BOOK_API_URL, String.class);
        String payload = response.getBody();
        List<Book> books = new ArrayList<>();

        JsonReader jReader = Json.createReader(new StringReader(payload));
        jakarta.json.JsonArray jArray = jReader.readArray();

        for (int i = 0; i < jArray.size(); i++) {
            JsonObject jObject = jArray.getJsonObject(i);
            Book book = new Book();
            book.setId(jObject.getInt("id"));
            book.setTitle(jObject.getString("title"));
            book.setAuthor(jObject.getString("author"));
            book.setPrice(jObject.getJsonNumber("price").doubleValue());
            book.setQuantity(jObject.getInt("quantity"));
            books.add(book);
        }
        return books;
    }

    public String createBook(Book book) {
        try {
            // create json manually to match format
            JsonObjectBuilder jObject = Json.createObjectBuilder()
                    .add("id", book.getId())
                    .add("title", book.getTitle())
                    .add("author", book.getAuthor())
                    .add("price", book.getPrice())
                    .add("quantity", book.getQuantity());

            String requestPayload = jObject.build().toString();

            // create req entity with content type
            RequestEntity<String> requestEntity = RequestEntity
                    .post(Util.BOOK_API_URL + "/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestPayload);

            ResponseEntity<String> response = restTemplate.exchange(
                    requestEntity, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error creating book: " + e.getMessage());
        }

    }

}
