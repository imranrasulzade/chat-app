package az.rsl.chatapp.services;


import az.rsl.chatapp.payloads.SignInPayload;
import az.rsl.chatapp.payloads.SignUpPayload;
import az.rsl.chatapp.responses.JwtAuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpPayload payload);
    JwtAuthenticationResponse signIn(SignInPayload payload);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
