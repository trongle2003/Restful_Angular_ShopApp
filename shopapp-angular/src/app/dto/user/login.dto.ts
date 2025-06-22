import { IsDateString, IsNotEmpty, IsPhoneNumber, IsString } from "class-validator";

export class LoginDTO {
    @IsPhoneNumber('VN') // chỉ định rõ là số điện thoại Việt Nam
    phoneNumber: string;

    @IsString()
    @IsNotEmpty()
    passWord: string;

    roleId: number;

    constructor(data: any) {
        this.phoneNumber = data.phoneNumber;
        this.passWord = data.passWord;
        this.roleId = data.roleId;
    }
}