package fun.hzaw.webapiservice.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @program: OpenPT
 * @description: 用户Token信息
 * @author: Luke
 * @create: 2024/4/11
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTokenVo {

    private String token;

    private Long expires;

    private String refresh;

    private String username;

    private String email;

    private String id;
}
