package sparta.m6nytooneproject.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.m6nytooneproject.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
}
