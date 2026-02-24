package sparta.m6nytooneproject.jwt;

import io.jsonwebtoken.ExpiredJwtException;
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

        try {
            // 2. 토큰 만료 검증
//            if (jwtUtil.isExpired(token)) {
//                filterChain.doFilter(request, response);
//                return;
//            }

            // 3. 토큰에서 정보 꺼내기
            String email = jwtUtil.getEmail(token);
            Long id = jwtUtil.getId(token);
            log.info(jwtUtil.getRole(token));
            UserRole role = UserRole.valueOf(jwtUtil.getRole(token));

            CustomUserDetails userDetails = new CustomUserDetails(
                    User.ofToken(id, email, role)
            );

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\": \"토큰이 만료되었습니다.\"}");
            return;
        }
    }
}
