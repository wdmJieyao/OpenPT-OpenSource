package fun.hzaw.trackerservice.exception;

import fun.hzaw.commonbean.exception.AppErrorInterface;
import fun.hzaw.commonbean.exception.CommonBaseException;

/**
 * @program: OpenPT
 * @description: webapi服务异常
 * @author: Luke
 * @create: 2024/4/11
 **/
public class TrackerException extends CommonBaseException {

    public TrackerException() {
    }

    public TrackerException(AppErrorInterface code) {
        super(code);
    }

    public TrackerException(AppErrorInterface code, Exception e) {
        super(code, e);
    }

    public TrackerException(AppErrorInterface code, String exMsg) {
        super(code, exMsg);
    }

    public TrackerException(AppErrorInterface code, String exMsg, Object data) {
        super(code, exMsg, data);
    }

    public TrackerException(int code, String exMsg) {
        super(code, exMsg);
    }

    public TrackerException(int code, String exMsg, Object data) {
        super(code, exMsg, data);
    }

}
