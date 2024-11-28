package com.example.day16practice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.day16practice.model.Book;
import com.example.day16practice.services.BookRestService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired BookRestService bookRestService;

    @GetMapping("/list")
    public String getAllBooks(Model model){
        List<Book> books = bookRestService.getAllBooks();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("book", new Book());
        return "bookForm";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute Book book, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "bookForm";
        }
        try {
            String result = bookRestService.createBook(book);
            if(result.equals("true")){
                return "redirect:/books/list";
            } else {
                model.addAttribute("errorMessage", "Error creating book");
                return "bookForm";
            }
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error creating book: " + ex.getMessage());
            return "bookForm";
        }
    }
}
