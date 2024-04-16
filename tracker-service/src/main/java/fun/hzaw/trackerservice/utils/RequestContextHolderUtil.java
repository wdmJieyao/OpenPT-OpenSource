package fun.hzaw.trackerservice.utils;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * @Description: 请求上下文对象工具类
 * @Author: wdmjieyao
 * @Date: 2022/3/11 16:54
 */
@ConditionalOnClass({ServletRequestAttributes.class, HttpServletRequest.class, HttpServletResponse.class})
@Component
public class RequestContextHolderUtil {

    private RequestContextHolderUtil() {
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = Optional.ofNullable(getRequestAttributes())
                .orElseThrow(() -> new RuntimeException("RequestContextHolderUtil..getRequestAttributes..ServletRequestAttributes is null!"));
        return attributes.getResponse();
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = Optional.ofNullable(getRequestAttributes())
                .orElseThrow(() -> new RuntimeException("RequestContextHolderUtil..getRequestAttributes..ServletRequestAttributes is null!"));
        return attributes.getRequest();
    }

}
