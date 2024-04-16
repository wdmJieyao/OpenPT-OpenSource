package fun.hzaw.commonbean.pojo.response;


import fun.hzaw.commonbean.exception.CommonBaseErrorCode;

/**
 * @author wdmjieyao
 * @Date 2022/1/19
 * @Time 15:39
 */
public class AppResult<T> {

    private Integer code;

    private String msg;

    private T data;

    public final static Integer SUCCESS_CODE = 0;

    public static <T> AppResult<T> success() {
        return new AppResult<>();
    }

    public static <T> AppResult<T> success(T data) {
        return new AppResult<>(data);
    }

    public static <T> AppResult<T> error(Integer code, String msg) {
        return new AppResult<>(code, msg);
    }

    public static <T> AppResult<T> error(Integer code, String msg, T data) {
        return new AppResult<>(code, msg, data);
    }

    public static <T> AppResult<T> error(CommonBaseErrorCode errorCode) {
        return new AppResult<>(errorCode);
    }

    public static <T> AppResult<T> error(CommonBaseErrorCode errorCode, T data) {
        return new AppResult<>(errorCode, data);
    }

    public static <T> AppResult<T> error(CommonBaseErrorCode errorCode, String exMsg) {
        return new AppResult<>(errorCode, exMsg);
    }

    public static <T> AppResult<T> error(CommonBaseErrorCode errorCode, String exMsg, T data) {
        return new AppResult<>(errorCode, exMsg, data);
    }

    private AppResult() {
        this.code = SUCCESS_CODE;
        this.msg = "";
    }

    private AppResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private AppResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private AppResult(T data) {
        this.code = SUCCESS_CODE;
        this.msg = "";
        this.data = data;
    }

    private AppResult(CommonBaseErrorCode errorCode) {
        if (errorCode == null) {
            return;
        }
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    private AppResult(CommonBaseErrorCode errorCode, String exMsg) {
        if (errorCode == null) {
            return;
        }
        this.code = errorCode.getCode();
        this.msg = String.format(errorCode.getMsg(), exMsg);
    }

    private AppResult(CommonBaseErrorCode errorCode, T data) {
        if (errorCode == null) {
            return;
        }
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
        this.data = data;
    }

    private AppResult(CommonBaseErrorCode errorCode, String exMsg, T data) {
        if (errorCode == null) {
            return;
        }
        this.code = errorCode.getCode();
        this.msg = String.format(errorCode.getMsg(), exMsg);
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

}
