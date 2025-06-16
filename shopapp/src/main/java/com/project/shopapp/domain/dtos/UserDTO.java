package com.project.shopapp.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("phoneNumber")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "Password cannot be blank")
    private String passWord;

    @JsonProperty("retypePassword")
    private String retypePassWord;

    @JsonProperty("dateOfBirth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")
    private Date dateOfBirth;

    @JsonProperty("facebookAccountId")
    private int facebookAccountId;

    @JsonProperty("googleAccountId")
    private int googleAccountId;

    @NotNull(message = "Role ID is required")
    @JsonProperty("roleId")
    private Long roleId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")
    private Instant createAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")
    private Instant updateAt;
}
