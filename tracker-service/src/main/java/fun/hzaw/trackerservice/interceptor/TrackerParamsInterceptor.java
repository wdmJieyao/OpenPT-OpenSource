// package fun.hzaw.trackerservice.interceptor;
//
// import cn.hutool.core.util.HexUtil;
// import com.alibaba.fastjson2.JSONObject;
// import fun.hzaw.trackerservice.constant.TrackerConstants;
// import fun.hzaw.trackerservice.exception.TrackerBencodeException;
// import fun.hzaw.trackerservice.utils.EncodeConvertUtils;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang3.StringUtils;
// import org.springframework.stereotype.Component;
// import org.springframework.web.servlet.HandlerInterceptor;
//
// import java.util.Arrays;
// import java.util.Optional;
//
// /**
//  * @program: OpenPT
//  * @description: Tracker 参数拦截器
//  * @author: Luke
//  * @create: 2024/4/12
//  **/
// @Component
// @Slf4j
// public class TrackerParamsInterceptor implements HandlerInterceptor {
//
//     @Override
//     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//         return true;
//     }
//
//     @Override
//     public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//         HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//     }
//
// }
