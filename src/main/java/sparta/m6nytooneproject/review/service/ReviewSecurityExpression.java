package sparta.m6nytooneproject.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sparta.m6nytooneproject.review.repository.ReviewRepository;
import sparta.m6nytooneproject.security.CustomUserDetails;

@Component("reviewSecurity")
@RequiredArgsConstructor
public class ReviewSecurityExpression {
    private final ReviewRepository reviewRepository;
    public boolean isOwner(Authentication authentication, Long reviewId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return reviewRepository.findById(reviewId)
                .map(review -> review.getCustomer().getId().equals(userDetails.getId()))
                .orElse(false);
    }
}
