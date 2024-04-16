package fun.hzaw.trackerservice.pojo.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Tracker 响应
 * <p>
 * Tracker 返回一个 "text/plain" 文档，该文档包含一个 bencoded 字典，其中包含以下键值：
 * <p>
 * failure reason（失败原因）：如果此键存在，则可能没有其他键。该值是一个人类可读的错误消息，说明请求为何失败（字符串）。
 * warning message（警告信息）：（新的，可选的）类似于 failure reason，但响应仍会被正常处理。警告信息就像错误信息一样显示。
 * interval（间隔）：客户端应在向 tracker 发送常规请求之间等待的秒数。
 * min interval（最小间隔）：（可选的）最小公告间隔。如果此键存在，客户端不得在此时间间隔内重复汇报。
 * tracker id（跟踪器 ID）：客户端应在其下一次汇报中发送回的字符串。如果没有并且先前的公告发送了一个追踪器ID，请不要丢弃旧值；继续使用它。
 * complete（完成）：拥有整个文件的 peer（对等方）数量，即种子seeders （整数类型）。
 * incomplete（未完成）：非种子 peer（对等方）的数量，又名 "leechers"（整数类型）。
 * peers ：（字典模型）该键的值是一个字典列表，每个字典都包含以下键：
 * ---- peer id （字符串）
 * ---- ip：peer的IP地址，可以是IPv6（十六进制），IPv4（点状四元组）或DNS名称（字符串）
 * ---- port（端口）：peer的端口号（整数）
 * peers ：（二进制模型）不使用上述的字典模型，peers 键的值可以是一个字符串，由多个6字节组成。前4字节是 IP 地址，后2字节是端口号。所有这些都采用网络字节序（大端序）。
 * <p>
 * 如上所述，默认情况下，peers列表长度为 50。如果 torrent 中的peers较少，则列表会更小。
 * 否则，tracker 会随机选择包含在响应中的peers。
 * tracker 可能会选择实施更智能的peers选择机制来响应请求。
 * 例如，可以避免向其他种子报告种子。
 * <p>
 * 在发生特定事件（如 stopped 或 completed）时，或者客户端需要了解更多peers时，客户端可以比指定的间隔更频繁地向 tracker 发送请求。但是，频繁地“轰炸” tracker 以获取多个peers是一种不良行为。如果客户端希望在响应中获得大量peer列表，则应指定 numwant 参数。
 * <p>
 * 实施者注意：即使30个peer已经足够，官方客户端版本3实际上只有在peer少于30个时才主动形成新连接，并且如果有55个peer，它将拒绝连接。这个值对性能非常重要。
 * 当新的片段下载完成后，需要向大多数活跃的peer发送 HAVE 消息（见下文）。结果是广播流量的成本与peer的数量成正比。
 * 超过25个peer后，新的peer不太可能增加下载速度。
 *
 * @return
 */
/**
 * @program: OpenPT
 * @description: 种子汇报响应Vo
 * @author: Luke
 * @create: 2024/4/15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackerAnnounceCompatVo {

    /**
     * 客户端应在向 tracker 发送常规请求之间等待的秒数。
     */
    @JSONField(name = "interval")
    private Long interval;

    /**
     * 客户端应在向 tracker 发送常规请求之间等待的秒数。
     */
    @JSONField(name = "min interval")
    private Long minInterval;
    /**
     * （跟踪器 ID）：客户端应在其下一次汇报中发送回的字符串。如果没有并且先前的公告发送了一个追踪器ID，请不要丢弃旧值；继续使用它。
     */
    @JSONField(name = "tracker id")
    private String trackerId;

    /**
     * （完成）：拥有整个文件的 peer（对等方）数量，即种子seeders （整数类型）。
     */
    @JSONField(name = "complete")
    private Long complete;

    /**
     * （未完成）：非种子 peer（对等方）的数量，又名 "leechers"（整数类型）。
     * 还在下载的数量
     */
    @JSONField(name = "incomplete")
    private Long incomplete;

    /**
     * 下载次数
     * qbit可以看到
     */
    @JSONField(name = "downloaded")
    private Long downloaded;

    /**
     *
     */
    @JSONField(name = "downloaders")
    private Long downloaders;

    /**
     * peers ：（字典模型）该键的值是一个字典列表，每个字典都包含以下键：
     * ---- peer id （字符串）
     * ---- ip：peer的IP地址，可以是IPv6（十六进制），IPv4（点状四元组）或DNS名称（字符串）
     * ---- port（端口）：peer的端口号（整数）
     * peers ：（二进制模型）不使用上述的字典模型，peers 键的值可以是一个字符串，由多个6字节组成。前4字节是 IP 地址，后2字节是端口号。所有这些都采用网络字节序（大端序）。
     */
    @JSONField(name = "peers")
    private List<PeerVo> peers;


    public static TrackerAnnounceCompatVo noAnyPeers(Long complete, Long incomplete) {
        TrackerAnnounceCompatVo vo = new TrackerAnnounceCompatVo();
        // 配置化控制汇报时间
        vo.setInterval(15 * 60L);
        vo.setMinInterval(30L);
        // 等待配置
        vo.setTrackerId("47350bda-62ba-466d-b50b-da62ba266d41");
        vo.setComplete(complete);
        vo.setIncomplete(incomplete);
        vo.setDownloaded(0L);
        vo.setDownloaders(0L);
        vo.setPeers(Lists.newArrayList());
        return vo;
    }

}
