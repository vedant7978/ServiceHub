package com.dalhousie.servicehub.factory.util;

import com.dalhousie.servicehub.util.EmailSender;
import com.dalhousie.servicehub.util.FileHelper;
import com.dalhousie.servicehub.util.UrlResourceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implementation class for UtilFactory.
 * This factory also follows singleton design pattern. Since Util classes are autowired,
 * springboot by default provides singleton instance over here.
 */
@Component
@RequiredArgsConstructor
public class UtilFactoryImpl implements UtilFactory {

    private final FileHelper fileHelper;
    private final PasswordEncoder passwordEncoder;
    private final UrlResourceHelper urlResourceHelper;
    private final EmailSender emailSender;
    private final AuthenticationManager authenticationManager;

    @Override
    public FileHelper getFileHelper() {
        return fileHelper;
    }

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Override
    public UrlResourceHelper getUrlResourceHelper() {
        return urlResourceHelper;
    }

    @Override
    public EmailSender getEmailSender() {
        return emailSender;
    }

    @Override
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }
}
