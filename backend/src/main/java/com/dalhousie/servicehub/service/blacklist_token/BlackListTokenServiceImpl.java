package com.dalhousie.servicehub.service.blacklist_token;

import com.dalhousie.servicehub.exceptions.BlackListTokenAlreadyExistsException;
import com.dalhousie.servicehub.model.BlackListTokenModel;
import com.dalhousie.servicehub.repository.BlackListRepository;
import com.dalhousie.servicehub.util.Constants;
import com.dalhousie.servicehub.util.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;

@Service
@RequiredArgsConstructor
public class BlackListTokenServiceImpl implements BlackListTokenService {

    private final BlackListRepository blackListRepository;

    @Override
    public ResponseBody<String> addBlackListToken(String token) {
        if (blackListRepository.findByToken(token).isPresent())
            throw new BlackListTokenAlreadyExistsException("BlackList Token already exists");

        BlackListTokenModel blackListTokenModel =  BlackListTokenModel.builder().token(token).build();
        blackListRepository.save(blackListTokenModel);
        return new ResponseBody<>(SUCCESS, "", Constants.SIGN_OUT_SUCCESS_MESSAGE);
    }

    @Override
    public boolean doesBlackListTokenExists(String token) {
        return blackListRepository.findByToken(token).isPresent();
    }
}
