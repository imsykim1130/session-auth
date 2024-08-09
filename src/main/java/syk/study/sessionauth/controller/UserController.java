package syk.study.sessionauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import syk.study.sessionauth.dto.JoinRequest;
import syk.study.sessionauth.dto.LoginRequest;
import syk.study.sessionauth.service.UserService;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> index(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(session.getAttribute("sessionId"));
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody JoinRequest joinRequest){

        // 중복체크
        String joinResult = userService.join(joinRequest);
        if(joinResult == null) {
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).body("이미 가입된 회원입니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(joinRequest.getUsername());
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.status(HttpStatus.OK).body("로그인 페이지");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request){
        // 유저 가입 여부 확인
        if(userService.login(loginRequest) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이디 또는 비밀번호가 틀립니다.");
        }

        // 유저 확인 완료되면 세션 부여
        HttpSession session = request.getSession(true);

        if(session.getAttribute("sessionId") == null) {
            session.setAttribute("sessionId", loginRequest.getUsername());
            session.setMaxInactiveInterval(60); // 60초
        }

        return ResponseEntity.status(HttpStatus.OK).body(session.getAttribute("sessionId"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그아웃 실패");
            }
        }
        return ResponseEntity.ok("로그아웃 성공");
    }

}
