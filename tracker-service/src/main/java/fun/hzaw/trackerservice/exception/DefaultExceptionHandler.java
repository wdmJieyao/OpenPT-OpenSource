package fun.hzaw.trackerservice.exception;

import com.fasterxml.jackson.core.JsonParseException;
import fun.hzaw.commonbean.exception.CommonBaseErrorCode;
import fun.hzaw.commonbean.pojo.response.AppResult;
import fun.hzaw.trackerservice.utils.EncodeConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.stream.Collectors;


/**
 * 所有的异常的处理 需要做国际化处理
 *
 * @author luke
 * @Date 2020/04/01
 * @Time 11:08
 */
@SuppressWarnings("rawtypes")
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    /**
     * Json转换异常
     */
    @ExceptionHandler(JsonParseException.class)
    @ResponseBody
    public AppResult handleJsonParseExceptions(JsonParseException ex) {
        return AppResult.error(CommonBaseErrorCode.PARAM_ERROR.getCode(), ex.toString());
    }

    /**
     * 方法参数异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public AppResult<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" && "));
        return AppResult.error(CommonBaseErrorCode.PARAM_ERROR, errorMsg);
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler(TrackerException.class)
    @ResponseBody
    public AppResult<Void> handleAppException(TrackerException ex) {
        log.info("#handleAppException:", ex);
        return AppResult.error(ex.getCode(), ex.getMsg());
    }

    /**
     * tacker 服务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(TrackerBencodeException.class)
    @ResponseBody
    public String handleExceptions(TrackerBencodeException ex) {
        log.error("handleTrackerBencodeException...error:", ex);
        return EncodeConvertUtils.encode(Map.of(
                "failure reason", "tracker be upgrading, please try again later!",
                "retry in", "never"
        ));
    }

    /**
     * 未知异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AppResult handleExceptions(Exception ex) {
        log.error("handleExceptions...error:", ex);
        return AppResult.error(CommonBaseErrorCode.SYSTEM_ERROR);
    }


}