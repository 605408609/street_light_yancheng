package com.exc.street.light.wifi.webservice;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
 * @author Xiezhipeng
 * @Description
 * @Date 2020/4/29
 */
public class ClientPwdCallback implements CallbackHandler {
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
        // 注册第三方系统时的密码
        pc.setPassword("admin");
    }
}
