package com.dalhousie.servicehub.service.user;

import com.dalhousie.servicehub.exceptions.InvalidTokenException;
import com.dalhousie.servicehub.exceptions.UserAlreadyExistException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.ResetPasswordTokenModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.request.AuthenticationRequest;
import com.dalhousie.servicehub.request.RegisterRequest;
import com.dalhousie.servicehub.response.AuthenticationResponse;
import com.dalhousie.servicehub.service.blacklist_token.BlackListTokenService;
import com.dalhousie.servicehub.service.jwt.JwtService;
import com.dalhousie.servicehub.service.reset_password.ResetPasswordTokenService;
import com.dalhousie.servicehub.util.EmailSender;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${email.frontend-port}")
    private int frontendPort;

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final AuthenticationManager authenticationManager;
    private final BlackListTokenService blackListTokenService;


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
    public void resetPassword(String email, String password, String token) {
        UserModel user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        ResetPasswordTokenModel resetPasswordTokenModel = resetPasswordTokenService.findByUserId(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User did not initiated reset password request."));

        if (!Objects.equals(token, resetPasswordTokenModel.getToken()))
            throw new InvalidTokenException("Failed to authenticate token. Please re-request to reset password.");

        if (!resetPasswordTokenService.isTokenValid(resetPasswordTokenModel))
            throw new InvalidTokenException("Token expired. Please re-request to reset password");

        String newPassword = passwordEncoder.encode(password);
        repository.updatePassword(email, newPassword);
        resetPasswordTokenService.deleteResetPasswordToken(resetPasswordTokenModel);
    }

    @Override
    public void forgotPassword(String email, String resetUrl) {
        UserModel user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        String refreshToken = resetPasswordTokenService.createResetPasswordToken(user.getId()).getToken();
        String resetPasswordLink = resetUrl + "?token=" + refreshToken + "&email=" + email;
        sendMail(resetPasswordLink, email);
    }

    @Override
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

    private void sendMail(String resetPasswordLink, String email) {
        EmailSender emailSender = new EmailSender(mailSender);
        String subject = "Reset your password";
        String content = "<p>You have requested to reset your password. Password link only valid for 10 minutes.</p>"
                + "<p>Click the link to reset your password:</p>"
                + "<p><a href=\"" + resetPasswordLink + "\">Reset my password</a></p>";
        try {
            emailSender.sendEmail(email, subject, content);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void signOut(String token) {
        blackListTokenService.addBlackListToken(token);
    }
}
