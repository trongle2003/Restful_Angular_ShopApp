import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { UserService } from '../../service/user.service';
import { LoginDTO } from '../../dto/user/login.dto';
import { LoginResponse } from '../../response/user/login.response';
import { TokenService } from '../../service/token.service';
import { RoleService } from '../../service/role.service';
import { Role } from '../../model/role';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;
  phoneNumber: string = '0374192150';
  passWord: string = '123456';

  roles: Role[] = [];
  selectedRole?: Role;

  constructor(private router: Router,
    private userService: UserService,
    private tokenService: TokenService,
    private roleService: RoleService
  ) { }

  onPhoneNumberChange() {
    console.log(`Phone typed: ${this.phoneNumber}`);
  }

  ngOnInit() {
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {
        this.roles = roles;
        this.selectedRole = undefined;
      },
      error: (error: any) => {
        console.error('Error getting roles: ', error);
      }
    });
  }

  login() {
    const loginDTO: LoginDTO = {
      phoneNumber: this.phoneNumber,
      passWord: this.passWord,
      roleId: this.selectedRole?.id ?? 2
    };
    console.log('Dữ liệu gửi:', loginDTO);
    debugger
    this.userService.login(loginDTO).subscribe(
      {
        next: (response: LoginResponse) => {
          debugger
          const { token } = response;
          this.tokenService.setToken(token);
        },
        error: (error: any) => {
          console.error('Lỗi chi tiết:', {
            status: error.status,
            statusText: error.statusText,
            message: error.error?.message,
            error: error.error
          });
        },
        complete: () => {
        }
      }
    );
  }

}
