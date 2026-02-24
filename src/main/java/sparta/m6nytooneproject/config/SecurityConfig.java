package sparta.m6nytooneproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sparta.m6nytooneproject.jwt.JwtFilter;
import sparta.m6nytooneproject.jwt.JwtUtil;
import sparta.m6nytooneproject.jwt.LoginFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http.csrf(AbstractHttpConfigurer::disable);

        //From 로그인 방식 disable
        http.formLogin(AbstractHttpConfigurer::disable);

        //http basic 인증 방식 disable
        http.httpBasic(AbstractHttpConfigurer::disable);

        //경로별 인가 작업
        // 해당 작업을 해주면 컨트롤러에서 따로 작업 X
        http.authorizeHttpRequests((auth) -> auth
                //permitAll 누구나 접근 가능. hasRole 해당 역할을 가진 유저만 접근 가능. authenticated 로그인한 유저만 접근 가능.
                        .requestMatchers(
                                "/users/login",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/users/signup").permitAll()
//                        .requestMatchers("/users/**").hasAnyRole("SUPER", "OPER", "MARKET", "CS")
                        .anyRequest().authenticated());

        // LoginFilter 필터 전에 JwtFilter 로 검증 하기.
        http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);
        // 필터 내가작성한것으로 변경
        http.addFilterAt(new LoginFilter(authenticationManager(),jwtUtil),
                UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http.sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // STATELESS 하게 유지 하기 위한 옵션

        return http.build();
    }
}