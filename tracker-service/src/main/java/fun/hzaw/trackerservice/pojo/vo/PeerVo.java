package fun.hzaw.trackerservice.pojo.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: OpenPT
 * @description: 对等客户端Vo
 * @author: Luke
 * @create: 2024/4/15
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PeerVo {

    @JSONField(name = "peer id")
    private String peerId;

    @JSONField(name = "ip")
    private String ip;

    @JSONField(name = "port")
    private int port;
}
