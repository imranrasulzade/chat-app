package az.rsl.chatapp.services.impl;

import az.rsl.chatapp.dto.RoleDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import az.rsl.chatapp.dto.UserDto;
import az.rsl.chatapp.entities.Token;
import az.rsl.chatapp.entities.User;
import az.rsl.chatapp.payloads.SignInPayload;
import az.rsl.chatapp.payloads.SignUpPayload;
import az.rsl.chatapp.repositories.RoleRepository;
import az.rsl.chatapp.repositories.TokenRepository;
import az.rsl.chatapp.repositories.UserRepository;
import az.rsl.chatapp.responses.AuthenticationResponse;
import az.rsl.chatapp.responses.JwtAuthenticationResponse;
import az.rsl.chatapp.services.AuthenticationService;
import az.rsl.chatapp.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    @Override
    public JwtAuthenticationResponse signUp(SignUpPayload payload) {
        var user = User.builder().userName(payload.getUserName())
                .password(passwordEncoder.encode(payload.getPassword()))
                .role(roleRepository.findByName(payload.getRole()).orElseThrow(()-> new RuntimeException("Role not found")))
//                .role(roleRepository.findByName(payload.getRole()).orElseThrow(()-> new NotFoundException(Role.class)))
                .createdAt(new Date())
                .status(true)
                .build();
        var savedUser = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        savedUserToken(savedUser, jwt);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }


    @Override
    public JwtAuthenticationResponse signIn(SignInPayload payload) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.getUserName(), payload.getPassword()));
        log.info("new login with name: {}, role: {}", authenticate.getName(), authenticate.getAuthorities());
        var user = userRepository.findByUserName(payload.getUserName())
                .orElseThrow(()-> new IllegalArgumentException("Invalid userName or password."));
        var jwt = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        savedUserToken(user, jwt);
        RoleDto roleDto = modelMapper.map(user.getRole(), RoleDto.class);
        return JwtAuthenticationResponse.builder()
                .user(UserDto.builder()
                        .id(user.getId())
                        .userName(user.getUsername())
                        .role(roleDto)
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .build())
                .token(jwt).build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userName;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        userName = jwtService.extractUserName(refreshToken);
        if (userName != null){
            var user = userRepository.findByUserName(userName)
                    .orElseThrow();
            if(jwtService.isValidToken(refreshToken, user)){
                var accessToken = jwtService.generateToken(user);
                refreshUserToken(accessToken, refreshToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> token.setExpired(true));
        tokenRepository.saveAll(validUserTokens);
        log.info("tokens is made expired for user: {}", user.getId());
    }


    private void savedUserToken(User user, String jwt){
        var token = Token.builder()
                .user(user)
                .token(jwt)
                .insertDate(new Date())
                .expireDate(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .expired(false)
                .build();
        tokenRepository.save(token);
        log.info("new token: {} is created for user: {} ", token, user.getId());
    }

    private void refreshUserToken(String accessToken, String refreshToken){
        Token token = tokenRepository.findByToken(refreshToken)
                .orElseThrow(()-> new RuntimeException("Token not found"));
//                .orElseThrow(()-> new NotFoundException(Token.class));
        token.setToken(accessToken);
        token.setExpireDate(new Date(System.currentTimeMillis() + 1000 * 60 * 30));
        token.setRefreshDate(new Date());
        tokenRepository.save(token);
    }
}
