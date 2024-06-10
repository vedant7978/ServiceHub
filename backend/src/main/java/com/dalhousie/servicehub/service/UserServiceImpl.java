package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.exceptions.UserAlreadyExistException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.request.AuthenticationRequest;
import com.dalhousie.servicehub.request.RegisterRequest;
import com.dalhousie.servicehub.response.AuthenticationResponse;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${email.frontend-port}")
    private int frontendPort;

    @Value("${email.ip-address}")
    private String ipAddress;

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;


@Override
    public AuthenticationResponse registerUser(RegisterRequest registerRequest) {
        if (repository.findByEmail(registerRequest.getEmail()).isPresent())
            throw new UserAlreadyExistException("User with this email already exists.");
        var userModel = UserModel.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .address(registerRequest.getAddress())
                .image(registerRequest.getImage())
                .build();
        repository.save(userModel);
        var jwtToken = jwtService.generateToken(userModel);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        if (repository.findByEmail(authenticationRequest.getEmail()).isEmpty())
            throw new UserNotFoundException("User not found with email: " + authenticationRequest.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user = repository.findByEmail(authenticationRequest.getEmail()).get();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public void resetPassword(String email, String password) {
        if (!repository.existsByEmail(email))
            throw new UserNotFoundException("User not found with email: " + email);
        String newPassword = passwordEncoder.encode(password);
        repository.updatePassword(email, newPassword);
    }

    public void forgotPassword(String email, String resetUrl) {
        UserModel user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        var refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        repository.save(user);

        String resetLink = resetUrl + "?token=" + refreshToken;
        String subject = "Reset your password";
        String content = "<p>You have requested to reset your password.</p>"
                + "<p>Click the link to reset your password:</p>"
                + "<p><a href=\"" + resetLink + "\">Reset my password</a></p>";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while processing forgot password request", ex);
        }
    }

    public String getURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString().replace(request.getServletPath(), "");
        try {
            java.net.URL oldURL = new java.net.URL(siteURL);
            java.net.URL newURL = new java.net.URL(oldURL.getProtocol(), oldURL.getHost(), frontendPort, oldURL.getFile());
            return newURL.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to change URL port", e);
        }
    }

}


