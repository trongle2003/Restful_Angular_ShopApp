import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dto/user/register.dto';
import { LoginDTO } from '../dto/user/login.dto';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiRegister = `${environment.apiBaseUrl}/users/register`;
  private apiLogin = `${environment.apiBaseUrl}/users/login`;
  private apiConfig = {
    headers: this.createHeaders()
  }

  constructor(private http: HttpClient) { }
  private createHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json', 'Accept-Language': 'vi'
    });
  }
  register(registerDTO: RegisterDTO): Observable<any> {
    return this.http.post(this.apiRegister, registerDTO, this.apiConfig)
  }

  login(loginDTO: LoginDTO): Observable<any> {
    return this.http.post(this.apiLogin, loginDTO, this.apiConfig)
  }
}
