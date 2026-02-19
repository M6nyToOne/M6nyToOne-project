package sparta.m6nytooneproject.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.m6nytooneproject.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
