package fun.hzaw.webapiservice.cache.local;

import cn.hutool.captcha.ICaptcha;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fun.hzaw.commonbean.exception.CommonBaseErrorCode;
import fun.hzaw.webapiservice.exception.WebApiErrorCode;
import fun.hzaw.webapiservice.exception.WebApiException;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.Optional;

/**
 * @program: OpenPT
 * @description: 验证码缓存器
 * @author: Luke
 * @create: 2024/4/11
 **/
public class CaptchaCache {

    private CaptchaCache() {
    }


    private static final Cache<String, ICaptcha> LOCAL_CAPTCHA_CACHE =
            CacheBuilder.newBuilder()
                    .initialCapacity(1000) // 初始容量
                    .maximumSize(10000L)   // 设定最大容量
                    // 设定写入过期时间
                    .expireAfterWrite(Duration.ofMinutes(10))
                    // 设置最大并发写操作线程数
                    .concurrencyLevel(8)
                    // 开启缓存执行情况统计
                    // .recordStats()
                    .build();

    /**
     * 缓存验证码验证器
     *
     * @param key
     * @param captcha
     */
    public static void put(String key, ICaptcha captcha) {
        if (StringUtils.isBlank(key) || null == captcha) {
            throw new WebApiException(CommonBaseErrorCode.SYSTEM_ERROR, "缓存参数不能设置为空！");
        }

        LOCAL_CAPTCHA_CACHE.put(key, captcha);
    }

    /**
     * 获取验证码验证器
     *
     * @param key
     * @return
     */
    public static ICaptcha get(String key) {
        return Optional.ofNullable(LOCAL_CAPTCHA_CACHE.getIfPresent(key))
                .orElseThrow(() -> new WebApiException(WebApiErrorCode.CAPTCHA_CODE_ERROR));
    }

    /**
     * 清除本地缓存
     *
     * @param key
     */
    public static void invalidate(String key) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        LOCAL_CAPTCHA_CACHE.invalidate(key);
    }
}
