package com.dalhousie.servicehub.service.blacklist_token;

public interface BlackListTokenService {
    void addBlackListToken(String token);
    boolean doesBlackListTokenExists(String token);
}
