package com.readly.booklist.book;

import com.readly.booklist.users.User;
import com.readly.booklist.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BooklistServiceImpl implements BooklistService {

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    // injection
    @Autowired
    public BooklistServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // returning books specific to a user that has logged in
    @Override
    public List<Book> getBooksByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return bookRepository.findByUserId(user.getId());
    }

    @Override
    public Book addBookForUser(String username, Book book){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        book.setUser(user);
        return bookRepository.save(book);
    }

    @Override
    public Book updateBookForUser(String username, Long bookId, Book updatedBook){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Book existingBook = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        if(!existingBook.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Cannot update this book");
        }

        existingBook.setPagesRead(updatedBook.getPagesRead());
        existingBook.setStatus(updatedBook.getStatus());
        existingBook.setRating(updatedBook.getRating());

        return bookRepository.save(existingBook);
    }

//    @Override
    public Book partialUpdateBook(String username, Long bookId, Map<String, Object> updates){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        if(!book.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Cannot update this book");
        }

        if(updates.containsKey("pagesRead")){
            book.setPagesRead((Integer)updates.get("pagesRead"));
        }

        if(updates.containsKey("status")){
            book.setStatus(BookStatus.valueOf(updates.get("status").toString()));
        }

        if(updates.containsKey("rating")){
            book.setRating((Integer)updates.get("rating"));
        }

        return bookRepository.save(book);
    }


    @Override
    public void deleteBook(String username, Long bookId){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        if(!book.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Cannot delete this book");
        }

        bookRepository.delete(book);
    }

    @Override
    public double calculateAvgRating(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        List<Book> books = bookRepository.findByUserId(user.getId());

        return books.stream().filter(book -> book.getRating() != null)
                .mapToInt(Book :: getRating).average().orElse(0.0);
    }

    @Override
    public Optional<Book> getBookById(long id){
        return bookRepository.findById(id);
    }


}
