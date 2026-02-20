package sparta.m6nytooneproject.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import sparta.m6nytooneproject.user.dto.UserResponseDto;
import sparta.m6nytooneproject.user.entity.User;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
