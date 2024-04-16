package fun.hzaw.commonbean.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * @author wdmjieyao
 * @Date 2023/4/10
 * @Time 11:09
 */
@Getter
public class CommonBaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2404372373182554123L;

    private static final String FORMAT_FLAG = "%s";

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 详细数据
     */
    private Object data;

    public CommonBaseException() {
        this(CommonBaseErrorCode.UNKNOWN_EXCEPTION);
    }

    public CommonBaseException(AppErrorInterface code) {
        code = (code == null ? CommonBaseErrorCode.UNKNOWN_EXCEPTION : code);
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public CommonBaseException(AppErrorInterface code, Exception e) {
        this(code);
        this.addSuppressed(e);
    }

    public CommonBaseException(AppErrorInterface code, String exMsg) {
        this(code);
        if (code.getMsg().contains(FORMAT_FLAG)) {
            this.msg = String.format(code.getMsg(), exMsg);
        } else {
            this.msg = exMsg;
        }
    }

    public CommonBaseException(AppErrorInterface code, String exMsg, Object data) {
        this(code);
        if (code.getMsg().contains(FORMAT_FLAG)) {
            this.msg = String.format(code.getMsg(), exMsg);
        } else {
            this.msg = exMsg;
        }
        this.data = data;
    }

    public CommonBaseException(int code, String exMsg) {
        this.code = code;
        this.msg = exMsg;
    }

    public CommonBaseException(int code, String exMsg, Object data) {
        this.code = code;
        this.msg = exMsg;
        this.data = data;
    }

}
