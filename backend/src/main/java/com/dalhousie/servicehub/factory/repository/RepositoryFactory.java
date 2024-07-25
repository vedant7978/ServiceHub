package com.dalhousie.servicehub.factory.repository;

import com.dalhousie.servicehub.repository.*;

public interface RepositoryFactory {
    BlackListRepository getBlackListRepository();
    ContractFeedbackRepository getContractFeedbackRepository();
    ContractRepository getContractRepository();
    FeedbackRepository getFeedbackRepository();
    ResetPasswordTokenRepository getResetPasswordTokenRepository();
    ServiceRepository getServiceRepository();
    UserRepository getUserRepository();
    WishlistRepository getWishlistRepository();
}
