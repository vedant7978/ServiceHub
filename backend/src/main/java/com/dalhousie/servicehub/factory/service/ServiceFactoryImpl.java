package com.dalhousie.servicehub.factory.service;

import com.dalhousie.servicehub.factory.mapper.MapperFactory;
import com.dalhousie.servicehub.factory.repository.RepositoryFactory;
import com.dalhousie.servicehub.factory.util.UtilFactory;
import com.dalhousie.servicehub.service.blacklist_token.BlackListTokenService;
import com.dalhousie.servicehub.service.blacklist_token.BlackListTokenServiceImpl;
import com.dalhousie.servicehub.service.contract.ContractService;
import com.dalhousie.servicehub.service.contract.ContractServiceImpl;
import com.dalhousie.servicehub.service.contract_feedback.ContractFeedbackService;
import com.dalhousie.servicehub.service.contract_feedback.ContractFeedbackServiceImpl;
import com.dalhousie.servicehub.service.dashboard_services.DashboardService;
import com.dalhousie.servicehub.service.dashboard_services.DashboardServiceImpl;
import com.dalhousie.servicehub.service.feedback.FeedbackService;
import com.dalhousie.servicehub.service.feedback.FeedbackServiceImpl;
import com.dalhousie.servicehub.service.file_upload.FileUploadService;
import com.dalhousie.servicehub.service.file_upload.FileUploadServiceImpl;
import com.dalhousie.servicehub.service.jwt.JwtService;
import com.dalhousie.servicehub.service.jwt.JwtServiceImpl;
import com.dalhousie.servicehub.service.profile.ProfileService;
import com.dalhousie.servicehub.service.profile.ProfileServiceImpl;
import com.dalhousie.servicehub.service.public_uploads.PublicUploadsService;
import com.dalhousie.servicehub.service.public_uploads.PublicUploadsServiceImpl;
import com.dalhousie.servicehub.service.reset_password.ResetPasswordTokenService;
import com.dalhousie.servicehub.service.reset_password.ResetPasswordTokenServiceImpl;
import com.dalhousie.servicehub.service.user.UserService;
import com.dalhousie.servicehub.service.user.UserServiceImpl;
import com.dalhousie.servicehub.service.user_services.ManageService;
import com.dalhousie.servicehub.service.user_services.ManageServiceImpl;
import com.dalhousie.servicehub.service.wishlist.WishlistService;
import com.dalhousie.servicehub.service.wishlist.WishlistServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceFactoryImpl implements ServiceFactory {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${email.frontend-port}")
    private int frontendPort;

    // Factories
    private final RepositoryFactory repositoryFactory;
    private final MapperFactory mapperFactory;
    private final UtilFactory utilFactory;

    // Services
    private BlackListTokenService blackListTokenService;
    private ContractService contractService;
    private ContractFeedbackService contractFeedbackService;
    private DashboardService dashboardService;
    private FeedbackService feedbackService;
    private FileUploadService fileUploadService;
    private JwtService jwtService;
    private ProfileService profileService;
    private PublicUploadsService publicUploadsService;
    private ResetPasswordTokenService resetPasswordTokenService;
    private UserService userService;
    private ManageService manageService;
    private WishlistService wishlistService;
    private final UserDetailsService userDetailsService;

    @Override
    public BlackListTokenService getBlackListTokenService() {
        if (blackListTokenService == null) {
            blackListTokenService = new BlackListTokenServiceImpl(repositoryFactory.getBlackListRepository());
        }
        return blackListTokenService;
    }

    @Override
    public ContractService getContractService() {
        if (contractService == null) {
            contractService = new ContractServiceImpl(repositoryFactory.getContractRepository(),
                    repositoryFactory.getUserRepository(),
                    repositoryFactory.getServiceRepository(),
                    getFeedbackService());
        }
        return contractService;
    }

    @Override
    public ContractFeedbackService getContractFeedbackService() {
        if (contractFeedbackService == null) {
            contractFeedbackService = new ContractFeedbackServiceImpl(repositoryFactory.getContractFeedbackRepository(),
                    repositoryFactory.getContractRepository(),
                    getFeedbackService());
        }
        return contractFeedbackService;
    }

    @Override
    public DashboardService getDashboardService() {
        if (dashboardService == null) {
            dashboardService = new DashboardServiceImpl(repositoryFactory.getServiceRepository(),
                    repositoryFactory.getUserRepository(),
                    repositoryFactory.getWishlistRepository(),
                    repositoryFactory.getContractRepository(),
                    mapperFactory.getServiceMapper(),
                    getFeedbackService());
        }
        return dashboardService;
    }

    @Override
    public FeedbackService getFeedbackService() {
        if (feedbackService == null) {
            feedbackService = new FeedbackServiceImpl(repositoryFactory.getFeedbackRepository(),
                    repositoryFactory.getUserRepository(),
                    mapperFactory.getFeedbackMapper());
        }
        return feedbackService;
    }

    @Override
    public FileUploadService getFileUploadService() {
        if (fileUploadService == null) {
            fileUploadService = new FileUploadServiceImpl(uploadPath, utilFactory.getFileHelper());
        }
        return fileUploadService;
    }

    @Override
    public JwtService getJwtService() {
        if (jwtService == null) {
            jwtService = new JwtServiceImpl();
        }
        return jwtService;
    }

    @Override
    public ProfileService getProfileService() {
        if (profileService == null) {
            profileService = new ProfileServiceImpl(repositoryFactory.getUserRepository(),
                    utilFactory.getPasswordEncoder());
        }
        return profileService;
    }

    @Override
    public PublicUploadsService getPublicUploadsService() {
        if (publicUploadsService == null) {
            publicUploadsService = new PublicUploadsServiceImpl(uploadPath, utilFactory.getUrlResourceHelper());
        }
        return publicUploadsService;
    }

    @Override
    public ResetPasswordTokenService getResetPasswordTokenService() {
        if (resetPasswordTokenService == null) {
            resetPasswordTokenService = new ResetPasswordTokenServiceImpl(
                    repositoryFactory.getResetPasswordTokenRepository(),
                    repositoryFactory.getUserRepository());
        }
        return resetPasswordTokenService;
    }

    @Override
    public UserService getUserService() {
        if (userService == null) {
            userService = new UserServiceImpl(frontendPort,
                    repositoryFactory.getUserRepository(),
                    utilFactory.getPasswordEncoder(),
                    getJwtService(),
                    utilFactory.getEmailSender(),
                    getResetPasswordTokenService(),
                    utilFactory.getAuthenticationManager(),
                    getBlackListTokenService());
        }
        return userService;
    }

    @Override
    public ManageService getManageService() {
        if (manageService == null) {
            manageService = new ManageServiceImpl(repositoryFactory.getServiceRepository(),
                    repositoryFactory.getUserRepository(),
                    mapperFactory.getServiceMapper());
        }
        return manageService;
    }

    @Override
    public WishlistService getWishlistService() {
        if (wishlistService == null) {
            wishlistService = new WishlistServiceImpl(repositoryFactory.getWishlistRepository(),
                    repositoryFactory.getServiceRepository(),
                    repositoryFactory.getUserRepository(),
                    getFeedbackService(),
                    mapperFactory.getServiceMapper(),
                    repositoryFactory.getContractRepository());
        }
        return wishlistService;
    }

    @Override
    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }
}
