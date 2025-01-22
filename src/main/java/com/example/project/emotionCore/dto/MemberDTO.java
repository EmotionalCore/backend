package com.example.project.emotionCore.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long id;

    @Size(min=2, max=10)
    private String username;

    @Size(max=50)
    @Email
    private String email;

    @Size(min = 8, max = 15)
    @Pattern(
            regexp = "^(?=.*[0-9!@#$%^&*]).+$",
            message = "비밀번호는 반드시 숫자 또는 특수문자를 포함해야 합니다."
    )
    private String password;
}
