import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!: NgForm;
  phone: string;
  password: string;
  retypePassword: string;
  fullName: string;
  address: string;
  isAccepted: boolean;
  dateOfBirth: string; // Đổi thành string
  isSubmitting: boolean = false; // Thêm để disable button

  constructor(private http: HttpClient, private router: Router) {
    this.phone = '0374192159';
    this.password = '123456';
    this.retypePassword = '123456';
    this.fullName = 'Lê Đức Trọng';
    this.address = 'Hà Nội';
    this.isAccepted = false;
    const defaultDate = new Date();
    defaultDate.setFullYear(defaultDate.getFullYear() - 18);
    this.dateOfBirth = formatDate(defaultDate, 'yyyy-MM-dd', 'en-US'); // Khởi tạo yyyy-MM-dd
  }

  onPhoneChange() {
    console.log(`Phone typed: ${this.phone}`);
  }

  register() {
    this.checkPasswordsMatch();
    this.checkAge();
    if (this.registerForm.invalid || !this.isAccepted) {
      alert('Vui lòng kiểm tra lại form hoặc đồng ý điều khoản');
      return;
    }

    this.isSubmitting = true; // Disable button
    const registerData = {
      fullName: this.fullName,
      phoneNumber: this.phone,
      address: this.address,
      passWord: this.password,
      retypePassword: this.retypePassword,
      dateOfBirth: this.dateOfBirth, // Đã là yyyy-MM-dd
      facebookAccountId: 0,
      googleAccountId: 0,
      roleId: 2
    };
    console.log('Dữ liệu gửi:', registerData);

    const apiUrl = 'http://localhost:8088/api/v1/users/register';
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http.post(apiUrl, registerData, { headers }).subscribe({
      next: (response: any) => {
        console.log('Phản hồi:', response);
        this.isSubmitting = false; // Re-enable button
        if (response && (response.status === 200 || response.status === 201)) {
          this.router.navigate(['/login']);
        }
      },
      error: (error: any) => {
        console.error('Lỗi chi tiết:', {
          status: error.status,
          statusText: error.statusText,
          message: error.error?.message,
          error: error.error
        });
        alert(`Không thể đăng ký, lỗi: ${error.error?.message || error.statusText || 'Dữ liệu không hợp lệ'}`);
        this.isSubmitting = false; // Re-enable button
      },
      complete: () => {
        this.isSubmitting = false; // Re-enable button
      }
    });
  }

  checkPasswordsMatch() {
    if (this.password !== this.retypePassword) {
      this.registerForm.form.controls['retypePassword'].setErrors({ passwordMismatch: true });
    } else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
    }
  }

  checkAge() {
    if (this.dateOfBirth) {
      const today = new Date();
      const birthDate = new Date(this.dateOfBirth);
      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }
      if (age < 18) {
        this.registerForm.form.controls['dateOfBirth'].setErrors({ invalidAge: true });
      } else {
        this.registerForm.form.controls['dateOfBirth'].setErrors(null);
      }
    }
  }
}