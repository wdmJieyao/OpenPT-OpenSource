package fun.hzaw.webapiservice.exception;

import fun.hzaw.commonbean.exception.AppErrorInterface;
import lombok.AllArgsConstructor;

/**
 * @program: OpenPT
 * @description: 错误码
 * @author: Luke
 * @create: 2024/4/11
 **/
@AllArgsConstructor
public enum WebApiErrorCode implements AppErrorInterface {
    CAPTCHA_CODE_ERROR(9000, "验证码错误！");
    ;

    private final Integer code;

    private final String msg;

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
