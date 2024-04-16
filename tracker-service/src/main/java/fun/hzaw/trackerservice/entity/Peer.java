package fun.hzaw.trackerservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @program: OpenPT
 * @description: torrent 对等客户端
 * @author: Luke
 * @create: 2024/4/15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Peer {

    private String peerId;

    private String ip;

    private int port;

    private String infoHash;

    private long id;

    private String userAgent;

    private long uploaded;

    private long downloaded;

    private long left;

    private boolean seeder;

    private boolean partialSeeder;

    private String passKey;

    private LocalDateTime updateAt;

    private long seedingTime;

    private long uploadSpeed;

    private long downloadSpeed;

}

