package az.rsl.chatapp.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface LogoutService {
    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
