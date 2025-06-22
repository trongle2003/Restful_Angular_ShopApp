import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Category } from '../model/category';
import { UpdateCategoryDTO } from '../dto/category/update.category.dto';
import { InsertCategoryDTO } from '../dto/category/insert.category.dto';
import { ApiResponse } from '../response/api.response';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private apiBaseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }
  getCategories(page: number, size: number):Observable<ApiResponse> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());           
      return this.http.get<ApiResponse>(`${environment.apiBaseUrl}/categories`, { params });           
  }
  getDetailCategory(id: number): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiBaseUrl}/categories/${id}`);
  }
  deleteCategory(id: number): Observable<ApiResponse> {
    debugger
    return this.http.delete<ApiResponse>(`${this.apiBaseUrl}/categories/${id}`);
  }
  updateCategory(id: number, updatedCategory: UpdateCategoryDTO): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.apiBaseUrl}/categories/${id}`, updatedCategory);
  }  
  insertCategory(insertCategoryDTO: InsertCategoryDTO): Observable<ApiResponse> {
    // Add a new category
    return this.http.post<ApiResponse>(`${this.apiBaseUrl}/categories`, insertCategoryDTO);
  }
}
