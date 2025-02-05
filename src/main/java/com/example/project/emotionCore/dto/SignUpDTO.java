package com.example.project.emotionCore.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpDTO {

    @Size(min=2, max=10)
    private String username;

    @Email(message = "유효한 이메일 형식이 아닙니다.")
    @Size(max = 50, message = "이메일은 50자를 초과할 수 없습니다.")
    private String email;

    @Size(min = 8, max = 15)
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]+$",
            message = "비밀번호는 반드시 숫자와 특수문자를 모두 포함해야 합니다."
    )
    private String password;
}
