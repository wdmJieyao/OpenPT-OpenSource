package fun.hzaw.webapiservice.service.base.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import fun.hzaw.commonbean.exception.CommonBaseErrorCode;
import fun.hzaw.webapiservice.cache.local.CaptchaCache;
import fun.hzaw.webapiservice.entity.po.UserLoginPo;
import fun.hzaw.webapiservice.entity.vo.UserTokenVo;
import fun.hzaw.webapiservice.exception.WebApiErrorCode;
import fun.hzaw.webapiservice.exception.WebApiException;
import fun.hzaw.webapiservice.service.base.UserAuthService;
import fun.hzaw.webapiservice.utils.RequestContextHolderUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: OpenPT
 * @description: 用户授权服务
 * @author: Luke
 * @create: 2024/4/11
 **/
@Service
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    @Override
    public void createCaptcha(String bruuid) {
        HttpServletResponse response = RequestContextHolderUtil.getResponse();
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            LineCaptcha captcha = CaptchaUtil.createLineCaptcha(150, 35, 5, 60);
            // 自定义验证码内容为四则运算方式
            captcha.setGenerator(new RandomGenerator(5));
            captcha.write(outputStream);
            CaptchaCache.put(bruuid, captcha);
        } catch (Exception e) {
            throw new WebApiException(CommonBaseErrorCode.SYSTEM_ERROR, e);
        }
    }

    @Override
    public UserTokenVo userLoginAndCreateToken(UserLoginPo userpo) {
        // 模拟登录
        StpUtil.login(123456L);
        ICaptcha captcha = CaptchaCache.get(userpo.getBruuid());
        if (!captcha.verify(userpo.getCaptchaCode())) {
            throw new WebApiException(WebApiErrorCode.CAPTCHA_CODE_ERROR);
        }

        CaptchaCache.invalidate(userpo.getBruuid());
        // 模拟假数据
        UserTokenVo tokenVo = new UserTokenVo();
        tokenVo.setToken(StpUtil.getTokenValue());
        tokenVo.setExpires(StpUtil.getTokenTimeout());
        return tokenVo;
    }
}
