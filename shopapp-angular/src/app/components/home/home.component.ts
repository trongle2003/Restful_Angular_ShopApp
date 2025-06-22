import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { Product } from '../../model/product';
import { Category } from '../../model/category';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../../response/api.response';

import { ProductService } from '../../service/product.service';
import { CategoryService } from '../../service/category.service';
import { ToastService } from '../../service/toast.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  selectedCategoryId: number = 0;
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = "";
  localStorage?: Storage;
  apiBaseUrl = environment.apiBaseUrl;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private toastService: ToastService
  ) {
    this.localStorage = window.localStorage;
  }


  ngOnInit() {
    this.getProducts(this.currentPage, this.itemsPerPage);
  }


  getCategories(page: number, size: number) {
    this.categoryService.getCategories(page, size).subscribe({
      next: (apiResponse: ApiResponse) => {
        this.categories = apiResponse.data;
      },
      error: (error: HttpErrorResponse) => {
        this.toastService.showToast({
          error: error,
          defaultMsg: 'Lỗi tải danh sách danh mục',
          title: 'Lỗi'
        });
      }
    });
  }

  getProducts(page: number, size: number) {
    this.productService.getProducts(page, size).subscribe({
      next: (apiResponse: ApiResponse) => {
        const response = apiResponse.data;
        response.products.forEach((product: Product) => {
          product.url = `${this.apiBaseUrl}/products/images/${product.thumbnail}`;
        });
        this.products = response.products;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      error: (error: HttpErrorResponse) => {
        this.toastService.showToast({
          error: error,
          defaultMsg: 'Lỗi tải danh sách sản phẩm',
          title: 'Lỗi'
        });
      }
    });
  }

  onPageChange(page: number) {
    this.currentPage = page < 0 ? 0 : page;
    this.getProducts(this.currentPage, this.itemsPerPage);

  }


  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);

    let startPage = Math.max(currentPage - halfVisiblePages, 1);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }

    return new Array(endPage - startPage + 1).fill(0).map((_, i) => startPage + i);
  }


}
