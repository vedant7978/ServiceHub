package com.dalhousie.servicehub.factory.repository;

import com.dalhousie.servicehub.repository.*;

/**
 * Factory pattern for repositories used in the application
 * This factory is responsible for creating objects for all repository used in the application.
 */
public interface RepositoryFactory {

    /**
     * Provides the blacklist repository
     * @return BlackListRepository instance
     */
    BlackListRepository getBlackListRepository();

    /**
     * Provides the contract feedback repository
     * @return ContractFeedbackRepository instance
     */
    ContractFeedbackRepository getContractFeedbackRepository();

    /**
     * Provides the contract repository
     * @return ContractRepository instance
     */
    ContractRepository getContractRepository();

    /**
     * Provides the feedback repository
     * @return FeedbackRepository instance
     */
    FeedbackRepository getFeedbackRepository();

    /**
     * Provides the reset password token repository
     * @return ResetPasswordTokenRepository instance
     */
    ResetPasswordTokenRepository getResetPasswordTokenRepository();

    /**
     * Provides the service repository
     * @return ServiceRepository instance
     */
    ServiceRepository getServiceRepository();

    /**
     * Provides the user repository
     * @return UserRepository instance
     */
    UserRepository getUserRepository();

    /**
     * Provides the wishlist repository
     * @return WishlistRepository instance
     */
    WishlistRepository getWishlistRepository();
}
