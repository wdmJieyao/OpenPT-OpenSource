package fun.hzaw.webapiservice.controller;

import fun.hzaw.commonbean.pojo.response.AppResult;
import fun.hzaw.webapiservice.entity.po.UserLoginPo;
import fun.hzaw.webapiservice.entity.vo.UserTokenVo;
import fun.hzaw.webapiservice.service.base.UserAuthService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @program: OpenPT
 * @description: 登录控制器
 * @author: Luke
 * @create: 2024/4/11
 **/
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Resource
    UserAuthService userAuthService;

    @GetMapping("/captcha.jpg")
    public void createCaptcha(@RequestParam(value = "bruuid") String bruuid) {
        userAuthService.createCaptcha(bruuid);
    }

    @PostMapping("/login")
    public AppResult<UserTokenVo> userLoginAndCreateToken(@Validated @RequestBody UserLoginPo userpo) {
        return AppResult.success(userAuthService.userLoginAndCreateToken(userpo));
    }

}
