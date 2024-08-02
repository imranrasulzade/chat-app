package az.rsl.chatapp.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);
    String generateToken(UserDetails userDetails);
    boolean isValidToken(String token, UserDetails userDetails);
}
