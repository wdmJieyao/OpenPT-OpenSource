package fun.hzaw.webapiservice.exception;

import fun.hzaw.commonbean.exception.AppErrorInterface;
import fun.hzaw.commonbean.exception.CommonBaseException;

/**
 * @program: OpenPT
 * @description: webapi服务异常
 * @author: Luke
 * @create: 2024/4/11
 **/
public class WebApiException extends CommonBaseException {

    public WebApiException() {
    }

    public WebApiException(AppErrorInterface code) {
        super(code);
    }

    public WebApiException(AppErrorInterface code, Exception e) {
        super(code, e);
    }

    public WebApiException(AppErrorInterface code, String exMsg) {
        super(code, exMsg);
    }

    public WebApiException(AppErrorInterface code, String exMsg, Object data) {
        super(code, exMsg, data);
    }

    public WebApiException(int code, String exMsg) {
        super(code, exMsg);
    }

    public WebApiException(int code, String exMsg, Object data) {
        super(code, exMsg, data);
    }

}
