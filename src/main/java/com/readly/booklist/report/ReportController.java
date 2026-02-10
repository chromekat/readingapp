package com.readly.booklist.report;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
//@CrossOrigin(origins = {
//  "http://localhost:4200",
//  "http://readly-book-app.s3-website.us-east-2.amazonaws.com"
//})
public class ReportController {

  @Autowired
  private ReportService reportService;

  public static class ReportRequest{
    private String username;
    public String getUsername(){
      return username;
    }
    public void setUsername(String username){
      this.username = username;
    }
  }

@PostMapping("/generate")
public ResponseEntity<String> generateReport(@RequestBody ReportRequest request) {
  reportService.generateAndSendReport(request.getUsername());
  return ResponseEntity.ok("Report generated. Check email");
}
}
