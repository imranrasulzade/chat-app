package az.rsl.chatapp.controller;

import az.rsl.chatapp.payloads.SignInPayload;
import az.rsl.chatapp.payloads.SignUpPayload;
import az.rsl.chatapp.responses.JwtAuthenticationResponse;
import az.rsl.chatapp.services.AuthenticationService;
import az.rsl.chatapp.services.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final LogoutService logoutService;


    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody SignUpPayload payload){
        return ResponseEntity.ok(authenticationService.signUp(payload));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInPayload payload){
        return ResponseEntity.ok(authenticationService.signIn(payload));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication){
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
        return ResponseEntity.ok(HttpStatus.OK);
    }




}
