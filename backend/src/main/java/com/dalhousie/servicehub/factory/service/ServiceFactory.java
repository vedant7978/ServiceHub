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

public interface ServiceFactory {
    BlackListTokenService getBlackListTokenService();
    ContractService getContractService();
    ContractFeedbackService getContractFeedbackService();
    DashboardService getDashboardService();
    FeedbackService getFeedbackService();
    FileUploadService getFileUploadService();
    JwtService getJwtService();
    ProfileService getProfileService();
    PublicUploadsService getPublicUploadsService();
    ResetPasswordTokenService getResetPasswordTokenService();
    UserService getUserService();
    ManageService getManageService();
    WishlistService getWishlistService();
    UserDetailsService getUserDetailsService();
}
