import { IsDateString, IsNotEmpty, IsPhoneNumber, IsString } from "class-validator";

export class RegisterDTO {
    @IsString()
    fullName: string;

    @IsPhoneNumber('VN') // chỉ định rõ là số điện thoại Việt Nam
    phoneNumber: string;

    @IsString()
    @IsNotEmpty()
    address: string;

    @IsString()
    @IsNotEmpty()
    passWord: string;

    @IsString()
    @IsNotEmpty()
    retypePassword: string;

    @IsDateString()
    dateOfBirth: string;

    facebookAccountId: number = 0;
    googleAccountId: number = 0;
    roleId: number = 2;

    constructor(data: any) {
        this.fullName = data.fullName;
        this.phoneNumber = data.phone;
        this.address = data.address;
        this.passWord = data.password;
        this.retypePassword = data.retypePassword;
        this.dateOfBirth = data.dateOfBirth;
        this.facebookAccountId = data.facebookAccountId || 0;
        this.googleAccountId = data.googleAccountId || 0;
        this.roleId = data.roleId || 2;
    }
}
