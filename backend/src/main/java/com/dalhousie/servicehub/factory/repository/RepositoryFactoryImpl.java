package com.dalhousie.servicehub.factory.repository;

import com.dalhousie.servicehub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RepositoryFactoryImpl implements RepositoryFactory {

    private final BlackListRepository blackListRepository;
    private final ContractFeedbackRepository contractFeedbackRepository;
    private final ContractRepository contractRepository;
    private final FeedbackRepository feedbackRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;

    @Override
    public BlackListRepository getBlackListRepository() {
        return blackListRepository;
    }

    @Override
    public ContractFeedbackRepository getContractFeedbackRepository() {
        return contractFeedbackRepository;
    }

    @Override
    public ContractRepository getContractRepository() {
        return contractRepository;
    }

    @Override
    public FeedbackRepository getFeedbackRepository() {
        return feedbackRepository;
    }

    @Override
    public ResetPasswordTokenRepository getResetPasswordTokenRepository() {
        return resetPasswordTokenRepository;
    }

    @Override
    public ServiceRepository getServiceRepository() {
        return serviceRepository;
    }

    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public WishlistRepository getWishlistRepository() {
        return wishlistRepository;
    }
}
