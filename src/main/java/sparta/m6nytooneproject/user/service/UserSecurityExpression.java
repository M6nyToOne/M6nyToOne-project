package sparta.m6nytooneproject.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sparta.m6nytooneproject.security.CustomUserDetails;
import sparta.m6nytooneproject.user.repository.UserRepository;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurityExpression {
    private final UserRepository userRepository;

    public boolean isOwner(Authentication authentication, Long userId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userRepository.findById(userId)
                .map(user -> user.getId().equals(userDetails.getId()))
                .orElse(false);
    }
}
