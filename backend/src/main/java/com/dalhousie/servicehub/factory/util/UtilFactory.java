package com.dalhousie.servicehub.factory.util;

import com.dalhousie.servicehub.util.EmailSender;
import com.dalhousie.servicehub.util.FileHelper;
import com.dalhousie.servicehub.util.UrlResourceHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UtilFactory {
    FileHelper getFileHelper();
    PasswordEncoder getPasswordEncoder();
    UrlResourceHelper getUrlResourceHelper();
    EmailSender getEmailSender();
    AuthenticationManager getAuthenticationManager();
}
