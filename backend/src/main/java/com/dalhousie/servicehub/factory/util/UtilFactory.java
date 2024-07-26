package com.dalhousie.servicehub.factory.util;

import com.dalhousie.servicehub.util.EmailSender;
import com.dalhousie.servicehub.util.FileHelper;
import com.dalhousie.servicehub.util.UrlResourceHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Factory pattern for util classes used in the application
 * This factory is responsible for creating objects for all util classes used in the application.
 */
public interface UtilFactory {

    /**
     * Provides the file helper
     * @return FileHelper instance
     */
    FileHelper getFileHelper();

    /**
     * Provides the password encoder
     * @return PasswordEncoder instance
     */
    PasswordEncoder getPasswordEncoder();

    /**
     * Provides the url resource helper
     * @return UrlResourceHelper instance
     */
    UrlResourceHelper getUrlResourceHelper();

    /**
     * Provides the email sender
     * @return EmailSender instance
     */
    EmailSender getEmailSender();

    /**
     * Provides the authentication manager
     * @return AuthenticationManager instance
     */
    AuthenticationManager getAuthenticationManager();
}
