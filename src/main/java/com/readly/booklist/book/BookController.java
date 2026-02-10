package com.readly.booklist.book;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
//@CrossOrigin(origins = {
//  "http://localhost:4200",
//  "http://readly-book-app.s3-website.us-east-2.amazonaws.com"
//})
@RequestMapping("/api/v1")
public class BookController {

    private final BooklistService booklistService;

    // for patching
    private ObjectMapper objectMapper;

    // constructor
    @Autowired
    public BookController(BooklistService booklistService, ObjectMapper objectMapper) {
        this.booklistService = booklistService;
        this.objectMapper = objectMapper;
    }


    @GetMapping("/books")
    public List<Book> getBooksForCurrentUser(Authentication authentication){
        String username = authentication.getName();

        return booklistService.getBooksByUser(username);
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book, Authentication authentication){

        String username = authentication.getName();
        return booklistService.addBookForUser(username, book);
    }

    @PutMapping("/books/{bookId}")
    public Book updateBook(@PathVariable Long bookId, @RequestBody Book book, Authentication authentication){
        String username = authentication.getName();
        return booklistService.updateBookForUser(username, bookId, book);
    }

    @PatchMapping("/books/{bookId}")
    public Book partiallyUpdateBook(@PathVariable Long bookId, @RequestBody Map<String, Object> patchPayload, Authentication authentication){
        String username = authentication.getName();

        Optional<Book> currentBook = booklistService.getBookById(bookId);

        Book patchedBook = apply(patchPayload, currentBook);
        return booklistService.updateBookForUser(username, bookId, patchedBook);
    }

    @DeleteMapping("/books/{bookId}")
    public void deleteBook(@PathVariable Long bookId, Authentication authentication){
        String username = authentication.getName();
        booklistService.deleteBook(username, bookId);
    }

    private Book apply(Map<String, Object> patchPayload, Optional<Book> currentBook){
        ObjectNode bookNode = objectMapper.convertValue(currentBook, ObjectNode.class);
        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        bookNode.setAll(patchNode);

        return objectMapper.convertValue(bookNode, Book.class);
    }
}
