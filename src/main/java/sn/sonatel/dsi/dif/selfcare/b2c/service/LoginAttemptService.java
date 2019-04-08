package sn.sonatel.dsi.dif.selfcare.b2c.service;

import sn.sonatel.dsi.dif.selfcare.b2c.exception.AccountB2CException;

public interface LoginAttemptService {

    void loginSucceeded(String key) throws AccountB2CException;

    void loginFailed(String key) throws AccountB2CException;

    boolean isBlocked(String key) ;
}
