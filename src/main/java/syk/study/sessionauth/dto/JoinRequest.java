package syk.study.sessionauth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import syk.study.sessionauth.entity.User;
import syk.study.sessionauth.entity.UserRole;

@Getter
@Setter
public class JoinRequest {
    @NotBlank(message = "사용자 이름이 비어있습니다.")
    private String username;
    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;
    private String passwordCheck;

    public User toEntity() {
        return new User(username, password, UserRole.USER);
    }

    public User toEntity(String encodedPassword) {
        return new User(username, encodedPassword, UserRole.USER);
    }
}
