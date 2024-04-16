package fun.hzaw.commonbean.exception;

/**
 * @Description: 错误代码封装
 * @Author: wdmjieyao
 * @Date: 2022/3/11 16:54
 */
public interface AppErrorInterface {

    /**
     * 获取错误码
     *
     * @return code
     */
    default Integer getCode() {
        return CommonBaseErrorCode.SYSTEM_ERROR.getCode();
    }

    ;

    /**
     * 获取信息
     *
     * @return msg
     */
    default String getMsg() {
        return CommonBaseErrorCode.SYSTEM_ERROR.getMsg();
    }
}
