import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient){}

  private apiUrl = 'http://localhost:8080/api/v1/report/generate';

  generateReport(username: string): Observable<string> {
    return this.http.post<string>(
      this.apiUrl,
      { username },
      {
        headers: {
          'Content-Type': 'application/json'
        },
        responseType: 'text' as 'json'
      }
    );
  }


}
