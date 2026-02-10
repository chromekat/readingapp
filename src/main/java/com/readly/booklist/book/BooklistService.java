package com.readly.booklist.book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BooklistService {

//    List<Book> getBooks();

    List<Book> getBooksByUser(String username);

    Book addBookForUser(String username, Book book);

    Book updateBookForUser(String username, Long bookId, Book updatedBook);

//    Book partialUpdateBook(String username, Long bookId, Map<String, Object> updates);

    void deleteBook(String username, Long bookId);

    double calculateAvgRating(String username);

    Optional<Book> getBookById(long id);

}
