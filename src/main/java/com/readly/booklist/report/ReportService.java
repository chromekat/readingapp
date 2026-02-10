package com.readly.booklist.report;

import com.readly.booklist.book.Book;
import com.readly.booklist.book.BookRepository;
import com.readly.booklist.users.User;
import com.readly.booklist.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private UserRepository userRepository;

  public void generateAndSendReport(String username) {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

    List<Book> books = bookRepository.findByUserId(user.getId());

    StringBuilder report = new StringBuilder();
    report.append("Hi ").append(username).append(",\n \n");
    report.append("Here is a report of your current reading statistics: \n \n");

    int totalPagesRead = 0;
    double totalRating = 0;
    int ratedBooks = 0;

    for (Book book : books) {
      report.append("• ").append(book.getTitle())
        .append(" by ").append(book.getAuthor())
        .append("\n Pages Read: ").append(book.getPagesRead())
        .append(" / ").append(book.getPageCount())
        .append("\n Status: ").append(book.getStatus())
        .append("\n Rating: ").append(book.getRating())
        .append("\n\n");

      if (book.getPagesRead() != null) {
        totalPagesRead += book.getPagesRead();
      }

      if (book.getRating() != null) {
        totalRating += book.getRating();
        ratedBooks++;
      }
    }

    double avgRating = ratedBooks > 0 ? totalRating / ratedBooks : 0;

    report.append("\nSummary:\n")
      .append("Total Pages Read: ").append(totalPagesRead).append("\n")
      .append("Average Rating: ").append(String.format("%.2f", avgRating)).append("\n");

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(user.getEmail());
    message.setSubject("Book Stats");
    message.setText(report.toString());

    mailSender.send(message);

  }



}
