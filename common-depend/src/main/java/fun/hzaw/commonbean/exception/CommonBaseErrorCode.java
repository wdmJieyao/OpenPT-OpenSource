package fun.hzaw.commonbean.exception;

public enum CommonBaseErrorCode implements AppErrorInterface {

    /*系统类异常*/
    SYSTEM_ERROR(1000, "服务器异常！"),
    PARAM_ERROR(1001, "请求参数异常！"),
    UNKNOWN_EXCEPTION(1002, "未知异常！"),
    CALL_REMOTE_ERROR(1003, "远程调用异常！"),
    TOTAL_REQ_REACH_LIMITED(1005, "请求数达到上限，请稍后重试！"),
    IP_REQ_REACH_LIMITED(1006, "请求数达到上限，请稍后重试！"),
    COMMIT_REPEATED(1007, "请勿重复提交"),
    REQUEST_METHOD_ERROR(1008, "请求方式错误"),
    INTERFACE_ERROR(1100, "接口异常"),
    INVALID_PARAMS(1101, "参数不合法"),
    FILE_DIRECTORY_NOT_EXIST(1102, "文件目录不存在"),

    /*用户异常信息*/
    USER_NOT_LOGIN(2000, "用户未登陆或登录已过期！"),
    INVALID_USER(2001, "用户不合法！"),
    ;

    private final Integer code;

    private final String msg;

    CommonBaseErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
