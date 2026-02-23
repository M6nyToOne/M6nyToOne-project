package sparta.m6nytooneproject.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sparta.m6nytooneproject.security.CustomUserDetails;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.entity.UserRole;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        // 토큰 없으면 return
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("Authorization header is missing");
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer" 제거
        String token = authorization.substring(7);

        // 2. 토큰 만료 검증
        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 토큰에서 정보 꺼내기
        String email = jwtUtil.getEmail(token);
        UserRole role = Arrays.stream(UserRole.values())
                .filter(r -> r.getKey().equals(jwtUtil.getRole(token)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 권한입니다."));

        CustomUserDetails userDetails = new CustomUserDetails(
                User.ofToken(email, role)
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
