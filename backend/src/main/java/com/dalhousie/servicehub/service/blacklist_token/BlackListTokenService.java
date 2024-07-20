package com.dalhousie.servicehub.service.blacklist_token;

import com.dalhousie.servicehub.util.ResponseBody;

public interface BlackListTokenService {

    /**
     * Add the token to blacklist token list
     * @param token Token to add
     * @return Response body object for String representing api result
     */
    ResponseBody<String> addBlackListToken(String token);

    /**
     * Check if the token is already blacklisted or not
     * @param token Token to check
     * @return True if token is added to blacklist, False otherwise
     */
    boolean doesBlackListTokenExists(String token);
}
