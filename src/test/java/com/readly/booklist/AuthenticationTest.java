package com.readly.booklist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpHeaders;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTest {
  
  @Autowired
  private MockMvc mvc;


  @Test
  void invalidCredentials() throws Exception {
    String credentials = "johnsmith:12345678910";
    String baseCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

    mvc.perform(
      get("/books")
        .header(HttpHeaders.AUTHORIZATION, "Basic " + baseCredentials))
      .andExpect(status().isUnauthorized());
  }


}
