package com.readly.booklist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readly.booklist.book.Book;
import com.readly.booklist.book.BookRepository;
import com.readly.booklist.book.BookStatus;
import com.readly.booklist.users.User;
import com.readly.booklist.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private User testUser;
  private final String TEST_USERNAME = "testUser";
  private final String TEST_PASSWORD = "testPassword123";
  private final String TEST_EMAIL = "test@readly.com";

  @BeforeEach
  void setUp() {
    testUser = userRepository.findByUsername(TEST_USERNAME).orElseGet(() -> {
      User user = new User();
      user.setUsername(TEST_USERNAME);
      user.setPasswordHash(passwordEncoder.encode(TEST_PASSWORD));
      user.setEmail(TEST_EMAIL);
      return userRepository.save(user);
    });

  }

  @Test
  void addBookTest() throws Exception {
    Book book = new Book();
    book.setTitle("Test Book");
    book.setAuthor("Test Author");
    book.setYearPublished("2025");
    book.setPageCount(200);
    book.setStatus(BookStatus.HAVE_READ);
    book.setRating(1);

    String jsonBook = objectMapper.writeValueAsString(book);

    mvc.perform(post("/api/v1/books").with(httpBasic("testUser", "testPassword123"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(jsonBook)).andExpect(status().isOk());

    List<Book> books = bookRepository.findByUserId(testUser.getId());
    boolean wasAdded = false;

    for(Book b : books){
      if(b.getTitle().equals("Test Book") && b.getAuthor().equals("Test Author")){
        wasAdded = true;
        break;
      }
    }

    assertTrue(wasAdded, "Book was not added to the book list");


  }



}
