package sparta.m6nytooneproject.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sparta.m6nytooneproject.user.dto.UserResponseDto;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.entity.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    // 승인 대기 중인 관리자들만 조회
    Page<User> findByRoleInAndSignupStatus(Collection<UserRole> userRole, SignupStatus status, Pageable pageable);

    // 관리자이면서 승인된 사람만 조회
    Page<User> findByRoleNotAndSignupStatus(UserRole role, SignupStatus status, Pageable pageable);

}
