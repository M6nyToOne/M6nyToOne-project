package sparta.m6nytooneproject.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.global.AuthConstants;
import sparta.m6nytooneproject.global.dto.SessionUserDto;
import sparta.m6nytooneproject.global.exception.user.UserRoleNotMatchException;
import sparta.m6nytooneproject.global.exception.common.UnAuthorizedException;
import sparta.m6nytooneproject.global.exception.user.*;
import sparta.m6nytooneproject.order.dto.CustomerOrderSummaryDto;
import sparta.m6nytooneproject.order.repository.OrderRepository;
import sparta.m6nytooneproject.user.dto.*;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.entity.UserRole;
import sparta.m6nytooneproject.user.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 디폴트가 읽기모드
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    // ===== 3. 관리자 정보 관리 =====

    // 슈퍼 관리자 맞는지 검증
    public void isSuperAdmin(SessionUserDto sessionUser) {
        isAdmin(sessionUser);
        if (!sessionUser.getUserRole().equals(UserRole.SUPER_ADMIN)) {
            throw new UnAuthorizedException("슈퍼 관리자 권한이 필요합니다.");
        }
    }

    // 그냥 관리자들이 맞는 지 검증
    public void isAdmin(SessionUserDto sessionUser) {
        if (sessionUser == null) {
            throw new UnAuthorizedException("세션이 존재하지 않습니다.");
        }
        if (sessionUser.getUserRole().equals(UserRole.CUSTOMER)) {
            throw new UnAuthorizedException("관리자 권한이 필요합니다.");
        }
    }



    // 회원가입
    @Transactional // 쓰기모드
    public UserResponseDto createUser(UserRequestDto requestDto) {
        // 회원가입 시 이메일 중복 확인
        boolean existence = userRepository.existsByEmail(requestDto.getEmail());
        if (existence) {
            throw new AlreadyExistingEmailException("이미 존재하는 이메일입니다.");
        }
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(
                requestDto.getName(),
                requestDto.getEmail(),
                encodedPassword,
                requestDto.getPhoneNumber(),
                requestDto.getUserRole()
        );
        User savedUser = userRepository.save(user);
        if (requestDto.getUserRole().equals(UserRole.CUSTOMER)) {
            savedUser.setSignupStatus(SignupStatus.ACTIVE);
            return new UserResponseDto(savedUser);
        }
        savedUser.setSignupStatus(SignupStatus.PENDING);
        return new UserResponseDto(savedUser);
        // 슈퍼 관리자가 승인해야 됨.
    }
    // 로그인
//    public SessionUserDto login(LoginRequestDto request) {
//        // 이메일이 유효한지
//        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
//                () -> new UserNotFoundException("존재하지 않는 이메일입니다.")
//        );
//        // 비밀번호가 일치하는지
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new UnmatchPasswordException("비밀번호가 일치하지 않습니다.");
//        }
//        // 회원가입 상태가 활성되지 않았다면 예외
//        if (!user.getSignupStatus().equals(SignupStatus.ACTIVE)) {
//            switch (user.getSignupStatus()) {
//                case PENDING -> throw new SessionNotActiveException("회원가입 승인 대기중입니다.");
//                case REJECTED -> throw new SessionNotActiveException("회원가입 신청이 거부되었습니다.");
//                case SUSPEND -> throw new SessionNotActiveException("계정이 정지되었습니다.");
//                case INACTIVE -> throw new SessionNotActiveException("계정이 비활성화 상태입니다.");
//            }
//        }
//        // 활성상태라면 로그인
//        return new SessionUserDto(user);
//    }

    // 슈퍼 관리자가 승인대기중인 관리자 전체조회
    public Page<UserResponseDto> getPendingUsers(int page, int size, SessionUserDto sessionUser) {
        isSuperAdmin(sessionUser);
        Pageable pageable = PageRequest.of(page + AuthConstants.PAGE_DEFAULT, size, Sort.by("createdAt").descending());
        return userRepository.findByRoleInAndSignupStatus(
                List.of(UserRole.SUPER_ADMIN, UserRole.OPER_ADMIN, UserRole.MARKET_ADMIN, UserRole.CS_ADMIN),
                SignupStatus.PENDING,
                pageable
                ).map(UserResponseDto::new);
    }

    // 슈퍼 관리자가 승인대기중인 관리자 승인(업데이트) / 각종 상태 변경
    @Transactional
    public UpdateUserStatusResponseDto updatePendingUser(Long userId, UpdateUserStatusRequestDto request, SessionUserDto sessionUser) {
        isSuperAdmin(sessionUser);
        User user = getUserById(userId);
        validateIsAdmin(user.getRole());
        // 슈퍼 관리자가 승인대기 상태를 활성/거부 상태로 업데이트
        // 거부 사유가 null이 아니면 거부 상태로 변경
        String reason = request.getRejectMessage();
        if (reason != null && !reason.isBlank()) {
            user.reject(reason);
        } else {
            // null이라면 활성 상태로 변경
            user.approve();
        }
        return new UpdateUserStatusResponseDto(user);
    }

    // 등록된 관리자 전체 조회
    public Page<UserResponseDto> getRegisteredUsers(int page, int size, SessionUserDto sessionUser) {
        isSuperAdmin(sessionUser);
        Pageable pageable = PageRequest.of(page + AuthConstants.PAGE_DEFAULT, size, Sort.by("createdAt").descending());
        Page<User> users = userRepository.findByRoleNotAndSignupStatus(UserRole.CUSTOMER, SignupStatus.ACTIVE, pageable);
        return users.map(UserResponseDto::new);
    }

    // 등록된 관리자 상세 조회 (단 건 조회)
    public UserResponseDto getOneRegisteredUser(Long userId, SessionUserDto sessionUser) {
        isSuperAdmin(sessionUser);
        User user = getUserById(userId);
        validateIsAdmin(user.getRole());
        if (!user.getSignupStatus().equals(SignupStatus.ACTIVE)) {
            throw new UnAuthorizedException("승인된 계정이 아닙니다.");
        }
        return new UserResponseDto(user);
    }

    // 등록된 관리자 정보 수정
    @Transactional
    public UpdateUserInfoResponseDto updateUserInfo(Long userId, UpdateUserInfoRequestDto request, SessionUserDto sessionUser) {
        isSuperAdmin(sessionUser);
        User user = getUserById(userId);
        validateIsAdmin(user.getRole());
        user.updateUserInfo(
                request.getUserName(),
                request.getEmail(),
                request.getPhoneNumber()
        );
        return new UpdateUserInfoResponseDto(user);
    }

    // 등록된 관리자 역할 변경
    @Transactional
    public UpdateRegisteredUserResponseDto updateRegisteredUser(Long userId, UpdateRegisteredRequestDto request, SessionUserDto sessionUser) {
        isSuperAdmin(sessionUser);
        User user = getUserById(userId);
        validateIsAdmin(user.getRole());
        if (request.getUserRole().equals(user.getRole())) {
            throw new AlreadySameRoleException("이미 해당 역할입니다.");
        }
        user.updateUserRole(request.getUserRole());
        return new UpdateRegisteredUserResponseDto(user);
    }

    // 등록된 관리자 삭제
    @Transactional
    public void deleteUser(Long userId, SessionUserDto sessionUser) {
        isSuperAdmin(sessionUser);
        User user = getUserById(userId);
        validateIsAdmin(user.getRole());
        userRepository.delete(user);
    }

    // 해당 유저id가 존재하는지 확인하는 메서드
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("존재하지 않는 유저입니다.")
        );
    }

    // 내 프로필 조회 (관리자 자신)
    public GetUserResponseDto getMyInfo(Long userId) {
        User user = getUserById(userId);
        validateIsAdmin(user.getRole());
        return new GetUserResponseDto(user);
    }

    // 내 프로필 수정 (관리자 자신의 이름, 이메일, 전화번호), 비번이 일치해야 수정할 수 있게.
    @Transactional
    public UpdateUserInfoResponseDto updateMyInfo(Long userId, UpdateUserInfoRequestDto request) {
        User user = getUserById(userId);
        validateIsAdmin(user.getRole());
        user.updateUserInfo(request.getUserName(), request.getEmail(), request.getPhoneNumber());
        return new UpdateUserInfoResponseDto(user);
    }

    // 내 비밀번호 변경 (관리자 자신)
    // TODO: 기존비밀번호와 동일했을때의 에러 수정 필요
    @Transactional
    public void changeMyPassword(Long userId, UpdateMyPasswordRequestDto request) {
        User user = getUserById(userId);
        validateIsAdmin(user.getRole());
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new UnmatchPasswordException("현재 비밀번호가 일치하지 않습니다.");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalStateException("기존 비밀번호와 다르게 설정해주세요.");
        }
        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    // ===== 4. 고객 정보 관리 =====
    // 고객 전체조회 (페이징) + 총 주문수, 총 구매 금액
    public Page<GetAllCustomerResponseDto> getAllCustomer(int page, int size, SessionUserDto sessionUser) {
        isAdmin(sessionUser);
        Pageable pageable = PageRequest.of(page + AuthConstants.PAGE_DEFAULT, size, Sort.by("createdAt").descending());
        Page<User> users = userRepository.findByRoleAndSignupStatus(UserRole.CUSTOMER, SignupStatus.ACTIVE, pageable);

        // TODO: Lv1. 고객 id 뽑아서 각 고객의 총 주문수, 총 구매 금액 구해서 응답에 추가하기
        List<Long> customerIds = users.stream().map(User::getId).toList(); // user에서 각 고객 id 뽑기
        List<CustomerOrderSummaryDto> summaries = orderRepository.summaryCustomerOrder(customerIds); // 주문 집계

        // Map으로 변환해서 원래의 고객 리스트에 위 두 응답 추가하기
        Map<Long, CustomerOrderSummaryDto> OrderSummary = summaries.stream()
                .collect(Collectors.toMap(CustomerOrderSummaryDto::getUserId, Function.identity()));

        //
        return users.map(user -> {
            CustomerOrderSummaryDto dto = OrderSummary.getOrDefault( // 고객 id로 주문집계를 찾아서
                    user.getId(), // 고객 id로 주문집계를 찾아서 있으면 가져오고 없으면 주문 0개 금액 0원으로 디폴트 고정해라.
                    new CustomerOrderSummaryDto(user.getId(), 0L, 0L));
            return new GetAllCustomerResponseDto(
                    user,
                    dto.getOrderCount(),
                    dto.getTotalAmount());
        });

    }

    // 고객 상세 조회 + 총 주문 수, 총 구매 금액
    public GetOneCustomerResponseDto getOneCustomer(Long userId, SessionUserDto sessionUser) {
        isAdmin(sessionUser);
        User user = getUserById(userId);
        validCustomer(user.getRole());
        // TODO: Lv1. 해당 고객의 총 주문수, 총 구매 금액 구해서 응답에 추가하기
        // 해당 고객id의 주문 정보가 존재하면 가져와서 dto에 넣고, 주문이 없다면 디폴트값 주문수 0, 금액 0원으로 표기해라.
        CustomerOrderSummaryDto dto = orderRepository.summaryOneCustomerOrder(user.getId())
                .orElse(new CustomerOrderSummaryDto(user.getId(), 0L, 0L));
        return new GetOneCustomerResponseDto(user, dto.getOrderCount(), dto.getTotalAmount());
    }

    // 고객 정보 수정
    @Transactional
    public UpdateCustomerInfoResponseDto updateCustomer(Long userId, UpdateUserInfoRequestDto request, SessionUserDto sessionUser) {
        isAdmin(sessionUser);
        User user = getUserById(userId);
        validCustomer(user.getRole());
        user.updateUserInfo(request.getUserName(), request.getEmail(), request.getPhoneNumber());
        return new UpdateCustomerInfoResponseDto(user);
    }

    // 고객 상태 변경
    @Transactional
    public UpdateCustomerInfoResponseDto updateCustomerStatus(Long userId, UpdateCustomerStatusRequestDto request, SessionUserDto sessionUser) {
        isAdmin(sessionUser);
        SignupStatus target = request.getSignupStatus();
        User user = getUserById(userId);
        validCustomer(user.getRole());
        if (target == SignupStatus.PENDING || target == SignupStatus.REJECTED) {
            throw new IllegalStateException("고객은 해당 상태로 변경이 불가능합니다.");
        }
        if (user.getSignupStatus() == target) {
            throw new IllegalStateException("이미 동일한 상태입니다.");
        }
        user.updateSignupStatus(target);
        return new UpdateCustomerInfoResponseDto(user);
    }

    // 고객 삭제
    @Transactional
    public void deleteCustomer(Long userId, SessionUserDto sessionUser) {
        isAdmin(sessionUser);
        User user = getUserById(userId);
        validCustomer(user.getRole());
        userRepository.delete(user);
    }

    // 고객인지?
    public void validCustomer(UserRole role){
        if(!role.equals(UserRole.CUSTOMER)){
            throw new UserRoleNotMatchException("권한이 올바르지 않습니다.");
        }
    }

    // 관리자인지?
    public void validateIsAdmin(UserRole role) {
        if(role.equals(UserRole.CUSTOMER)){
            throw new UserRoleNotMatchException("권한이 올바르지 않습니다.");
        }
    }

    //삭제, 수정 등 작업을 요청할때 요청한 유저와 그것을 작성한 유저가 동일한지 확인하는 서비스
    public void validateRequesterIsOwner(Long requesterId, Long ownerId){
        if(!requesterId.equals(ownerId)){
            throw new UnAuthorizedException("권한이 없습니다.");
        }
    }
}
