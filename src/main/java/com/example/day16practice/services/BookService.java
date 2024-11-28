package com.example.day16practice.services;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.day16practice.Utils.Util;
import com.example.day16practice.model.Book;
import com.example.day16practice.repository.ListRepo;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

@Service
public class BookService {
    @Autowired
    ListRepo listRepo;

    // add book
    public void add(Book book) {
        // convert book data to json string to store
        JsonObjectBuilder bookBuilder = Json.createObjectBuilder()
                .add("id", book.getId())
                .add("title", book.getTitle())
                .add("author", book.getAuthor())
                .add("price", book.getPrice())
                .add("quantity", book.getQuantity());

        String bookJson = bookBuilder.build().toString();

        //store in redis
        listRepo.rightPush(Util.BOOK_LIST_KEY, bookJson);
    }

    public List<Book> findAll() {
        // retrieve all json strings from redis
        List<String> bookJsonList = listRepo.getList(Util.BOOK_LIST_KEY);
        List<Book> books = new ArrayList<>();

        //convert json string back to book object
        for (String bookJson : bookJsonList) {
            try {
                if (bookJson != null && !bookJson.trim().isEmpty()) {
                    JsonReader reader = Json.createReader(new StringReader(bookJson));
                    JsonObject bookObj = reader.readObject();

                    Book book = new Book();
                    book.setId(bookObj.getInt("id"));
                    book.setTitle(bookObj.getString("title"));
                    book.setAuthor(bookObj.getString("author"));
                    book.setPrice(bookObj.getJsonNumber("price").doubleValue());
                    book.setQuantity(bookObj.getInt("quantity"));
                    books.add(book);
                }
            } catch (Exception e) {
                System.err.println("Error parsing book JSON: " + bookJson);
                e.printStackTrace();
                // skip invalid entries
                continue;
            }
        }

        return books;
    }

    public Book findById(Integer id) {
        List<String> bookJsonList = listRepo.getList(Util.BOOK_LIST_KEY);

        for (String bookJson : bookJsonList) {
            // MAKE SURE TO USE JAKARTA NOT GOOGLE GSON!!!
            jakarta.json.JsonReader reader = Json.createReader(new StringReader(bookJson));
            JsonObject bookObj = reader.readObject();

            if (bookObj.getInt("id") == id) {
                Book book = new Book();
                book.setId(bookObj.getInt("id"));
                book.setTitle(bookObj.getString("title"));
                book.setAuthor(bookObj.getString("author"));
                book.setPrice(bookObj.getJsonNumber("price").doubleValue());
                book.setQuantity(bookObj.getInt("quantity"));
                return book;
            }
        }
        return null;
    }

}
