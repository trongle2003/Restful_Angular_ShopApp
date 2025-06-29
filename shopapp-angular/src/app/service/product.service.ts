import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Product } from '../model/product';
import { UpdateProductDTO } from '../dto/product/update.product.dto';
import { InsertProductDTO } from '../dto/product/insert.product.dto';
import { ApiResponse } from '../response/api.response';

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    private apiBaseUrl = environment.apiBaseUrl;

    constructor(private http: HttpClient) { }

    getProducts(
        page: number,
        size: number
    ): Observable<ApiResponse> {
        const params = {
            page: page.toString(),
            size: size.toString()
        };
        return this.http.get<ApiResponse>(`${this.apiBaseUrl}/products`, { params });
    }

    getDetailProduct(productId: number): Observable<ApiResponse> {
        return this.http.get<ApiResponse>(`${this.apiBaseUrl}/products/${productId}`);
    }

    getProductsByIds(productIds: number[]): Observable<ApiResponse> {
        const params = new HttpParams().set('ids', productIds.join(','));
        return this.http.get<ApiResponse>(`${this.apiBaseUrl}/products/by-ids`, { params });
    }
    deleteProduct(productId: number): Observable<ApiResponse> {
        debugger
        return this.http.delete<ApiResponse>(`${this.apiBaseUrl}/products/${productId}`);
    }
    updateProduct(productId: number, updatedProduct: UpdateProductDTO): Observable<ApiResponse> {
        return this.http.put<ApiResponse>(`${this.apiBaseUrl}/products/${productId}`, updatedProduct);
    }
    insertProduct(insertProductDTO: InsertProductDTO): Observable<ApiResponse> {
        // Add a new product
        return this.http.post<ApiResponse>(`${this.apiBaseUrl}/products`, insertProductDTO);
    }
    uploadImages(productId: number, files: File[]): Observable<ApiResponse> {
        const formData = new FormData();
        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }
        // Upload images for the specified product id
        return this.http.post<ApiResponse>(`${this.apiBaseUrl}/products/uploads/${productId}`, formData);
    }
    deleteProductImage(id: number): Observable<any> {
        debugger
        return this.http.delete<string>(`${this.apiBaseUrl}/product_images/${id}`);
    }
}
//update.category.admin.component.html