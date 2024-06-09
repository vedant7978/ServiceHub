
    @Test
    @DisplayName("Throw exception when user not found")
    void forgotPassword_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.forgotPassword("jems007patel@gmail.com", "http://forgotPassword.com/reset"));

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(refreshTokenService, times(0)).createRefreshToken(any(UserModel.class));
        verify(mailSender, times(0)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Handle exception during email sending")
    void forgotPassword_shouldHandleEmailException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userModel));
        when(refreshTokenService.createRefreshToken(any(UserModel.class))).thenReturn(new RefreshTokenModel());

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Email sending failed")).when(mailSender).send(mimeMessage);

        assertThrows(RuntimeException.class, () -> userService.forgotPassword("jems007patel@gmail.com", "http://forgotPassword.com/reset"));

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(refreshTokenService, times(1)).createRefreshToken(any(UserModel.class));
        verify(mailSender, times(1)).send(mimeMessage);
    }

}