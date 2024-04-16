package fun.hzaw.webapiservice.entity.po;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @program: OpenPT
 * @description: 用户登录操作对象
 * @author: Luke
 * @create: 2024/4/11
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginPo {

    @NotBlank(message = "username can be not blank!!")
    private String username;

    @NotBlank(message = "password can be not blank!!")
    private String password;

    @NotBlank(message = "bruuid can be not blank!!")
    private String bruuid;

    @NotBlank(message = "captchaCode can be not blank!!")
    private String captchaCode;
}
