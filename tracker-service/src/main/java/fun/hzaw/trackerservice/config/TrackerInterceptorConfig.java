// package fun.hzaw.trackerservice.config;
//
// import fun.hzaw.trackerservice.interceptor.TrackerParamsInterceptor;
// import jakarta.annotation.Resource;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// /**
//  * @program: OpenPT
//  * @description: 过滤器配置
//  * @author: Luke
//  * @create: 2024/4/12
//  **/
// @Configuration
// public class TrackerInterceptorConfig implements WebMvcConfigurer {
//
//     @Resource
//     TrackerParamsInterceptor trackerParamsInterceptor;
//
//     @Override
//     public void addInterceptors(InterceptorRegistry registry) {
//         registry.addInterceptor(trackerParamsInterceptor)
//                 .addPathPatterns("/**/announce");
//     }
// }
