package syk.study.sessionauth.service;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Service;
import syk.study.sessionauth.dto.JoinRequest;
import syk.study.sessionauth.dto.LoginRequest;
import syk.study.sessionauth.entity.User;
import syk.study.sessionauth.entity.UserRole;
import syk.study.sessionauth.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    // 이름 중복체크
    // 입력 - String  : 중복체크 할 이름
    // 출력 - boolean : 중복여부
    private boolean isDuplicateUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // 회원가입
    // 입력 - JoinRequest
    // 출력 - String 이름
    public String join(JoinRequest request) {
        // 중복 회원 확인
        if(isDuplicateUsername(request.getUsername())) {
            // 가입 실패
            return null;
        }

        // 가입
        User user = new User(request.getUsername(), request.getPassword(), UserRole.USER);
        userRepository.save(user);
        return request.getUsername();
    }


    // 로그인
    // 입력 - LoginRequest
    // 출력 - LoginRequest
    public LoginRequest login(LoginRequest request) {
        // 리퀘스트로 온 유저 정보 데이터베이스에서 찾기
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());

        if(optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        // 비밀번호 검사
        if(!user.getPassword().equals(request.getPassword())) {
            return null;
        }
        return request;
    }

}
