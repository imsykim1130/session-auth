package syk.study.sessionauth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {
    private String username;
    private String password;
}
