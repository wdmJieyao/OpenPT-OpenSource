package fun.hzaw.webapiservice.service.base;

import fun.hzaw.webapiservice.entity.po.UserLoginPo;
import fun.hzaw.webapiservice.entity.vo.UserTokenVo;

/**
 * @program: OpenPT
 * @description: 用户授权服务
 * @author: Luke
 * @create: 2024/4/11
 **/
public interface UserAuthService {

    /**
     * 产生验证码
     * @param bruuid
     */
    void createCaptcha(String bruuid);

    /**
     * 登录并返回token
     * @param userpo
     * @return
     */
    UserTokenVo userLoginAndCreateToken(UserLoginPo userpo);
}
