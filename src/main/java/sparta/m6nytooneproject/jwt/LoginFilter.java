package sparta.m6nytooneproject.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sparta.m6nytooneproject.global.dto.LoginRequestDto;
import sparta.m6nytooneproject.security.CustomUserDetails;

import java.io.IOException;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    //UsernamePasswordAuthenticationFilter -> 자동으로 잡아줌 /users/login
    public LoginFilter(AuthenticationManager authenticationManager,JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequestDto loginRequest =
                    objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            log.info("Attempting to authenticate user: " + email);
            //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
            // UsernamePasswordAuthenticationToken -> DTO
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

            //token에 담은 검증을 위한 AuthenticationManager로 전달
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("로그인 요청 파싱 실패", e);
        }
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtil.createToken(userDetails.getId(), userDetails.getUsername(), userDetails.getRole());

        // 헤더에 토큰 추가
        response.addHeader("Authorization", "Bearer " + token);

        // Body에 응답 추가
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().write("""
            {
                "message": "로그인 성공",
                "token": "Bearer %s"
            }
            """.formatted(token));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        response.getWriter().write("""
            {
                "message": "로그인 실패",
                "error": "%s"
            }
            """.formatted(failed.getMessage()));
    }
}
