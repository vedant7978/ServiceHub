package com.dalhousie.servicehub.service.blacklist_token;


import com.dalhousie.servicehub.exceptions.BlackListTokenAlreadyExistsException;
import com.dalhousie.servicehub.model.BlackListTokenModel;
import com.dalhousie.servicehub.repository.BlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlackListTokenServiceImpl implements BlackListTokenService{

    @Autowired
    private BlackListRepository blackListRepository;

    @Override
    public void addBlackListToken(String token) {
        if (blackListRepository.findByToken(token).isPresent()){
            throw new BlackListTokenAlreadyExistsException("BlackList Token already exists");
        }
        BlackListTokenModel blackListTokenModel =  BlackListTokenModel.builder().token(token).build();
        blackListRepository.save(blackListTokenModel);
    }

    @Override
    public boolean doesBlackListTokenExists(String token) {
        return blackListRepository.findByToken(token).isPresent();
    }
}
