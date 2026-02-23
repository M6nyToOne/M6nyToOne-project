package sparta.m6nytooneproject.config.config.initialize;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.entity.UserRole;
import sparta.m6nytooneproject.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    String name = System.getenv("SUPER_ADMIN_NAME");
    String email = System.getenv("SUPER_ADMIN_EMAIL");
    String password = System.getenv("SUPER_ADMIN_PASSWORD");
    String phone = System.getenv("SUPER_ADMIN_PHONE_NUMBER");

    @Override
    public void run(ApplicationArguments args) {
        String encodedPassword = passwordEncoder.encode(password);

        userRepository.save(new User(
                name,
                email,
                encodedPassword,
                phone,
                UserRole.SUPER_ADMIN
        ));
    }
}
