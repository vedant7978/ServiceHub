package com.dalhousie.servicehub.factory.service;

import com.dalhousie.servicehub.service.blacklist_token.BlackListTokenService;
import com.dalhousie.servicehub.service.contract.ContractService;
import com.dalhousie.servicehub.service.contract_feedback.ContractFeedbackService;
import com.dalhousie.servicehub.service.dashboard_services.DashboardService;
import com.dalhousie.servicehub.service.feedback.FeedbackService;
import com.dalhousie.servicehub.service.file_upload.FileUploadService;
import com.dalhousie.servicehub.service.jwt.JwtService;
import com.dalhousie.servicehub.service.profile.ProfileService;
import com.dalhousie.servicehub.service.public_uploads.PublicUploadsService;
import com.dalhousie.servicehub.service.reset_password.ResetPasswordTokenService;
import com.dalhousie.servicehub.service.user.UserService;
import com.dalhousie.servicehub.service.user_services.ManageService;
import com.dalhousie.servicehub.service.wishlist.WishlistService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Factory pattern for services used in the application
 * This factory is responsible for creating objects for all services used in the application.
 */
public interface ServiceFactory {

    /**
     * Provides the blacklist token service
     * @return BlackListTokenService instance
     */
    BlackListTokenService getBlackListTokenService();

    /**
     * Provides the contract service
     * @return ContractService instance
     */
    ContractService getContractService();

    /**
     * Provides the contract feedback service
     * @return ContractFeedbackService instance
     */
    ContractFeedbackService getContractFeedbackService();

    /**
     * Provides the dashboard service
     * @return DashboardService instance
     */
    DashboardService getDashboardService();

    /**
     * Provides the feedback service
     * @return FeedbackService instance
     */
    FeedbackService getFeedbackService();

    /**
     * Provides the file upload service
     * @return FileUploadService instance
     */
    FileUploadService getFileUploadService();

    /**
     * Provides the jwt service
     * @return JwtService instance
     */
    JwtService getJwtService();

    /**
     * Provides the profile service
     * @return ProfileService instance
     */
    ProfileService getProfileService();

    /**
     * Provides the public uploads service
     * @return PublicUploadsService instance
     */
    PublicUploadsService getPublicUploadsService();

    /**
     * Provides the reset password token service
     * @return ResetPasswordTokenService instance
     */
    ResetPasswordTokenService getResetPasswordTokenService();

    /**
     * Provides the user service
     * @return UserService instance
     */
    UserService getUserService();

    /**
     * Provides the manage service
     * @return ManageService instance
     */
    ManageService getManageService();

    /**
     * Provides the wishlist service
     * @return WishlistService instance
     */
    WishlistService getWishlistService();

    /**
     * Provides the user details service
     * @return UserDetailsService instance
     */
    UserDetailsService getUserDetailsService();
}
