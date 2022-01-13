package sn.sonatel.dsi.dif.selfcare.b2c.service;


public interface LoginAttemptService {

    void loginSucceeded(String key);

    int loginFailed(String key);

    boolean isBlocked(String key) ;
}
