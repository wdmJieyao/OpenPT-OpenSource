package fun.hzaw.trackerservice.controller;

import cn.hutool.core.util.HexUtil;
import com.alibaba.fastjson2.JSON;
import fun.hzaw.trackerservice.constant.TrackerConstants;
import fun.hzaw.trackerservice.exception.TrackerBencodeException;
import fun.hzaw.trackerservice.pojo.po.AnnounceRequestPo;
import fun.hzaw.trackerservice.pojo.vo.TrackerAnnounceCompatVo;
import fun.hzaw.trackerservice.service.AnnounceService;
import fun.hzaw.trackerservice.utils.EncodeConvertUtils;
import fun.hzaw.trackerservice.utils.IPUtils;
import fun.hzaw.trackerservice.utils.RequestContextHolderUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

/**
 * @program: OpenPT
 * @description: Tracker控制器
 * @author: Luke
 * @create: 2024/4/12
 **/
@RestController
@RequestMapping("/report")
@Slf4j
public class TrackerController {

    @Resource
    AnnounceService announceService;

    /**
     * http://www.bittorrent.org/beps/bep_0003.html
     * <p>
     * want-digest 是为了校验aria2
     * <p>
     * 1. bep建议接口路径是 /announce
     * 2. 成功响应
     * 返回一个经过 Bencode 编码的 Dictionary （也就是 Map），包含以下 key：
     * <p>
     * interval – 代表间隔时间，单位是秒，表示 BT 客户端应该指定时间之后再与 Tracker 联系更新状态
     * peers – 这是个 List，每个 List 存储一个 Dictionary（也就是 Map），每个 Dictionary 包含以下 key：
     * peer id  客户端随机唯一 ID
     * ip 客户端 IP 地址，既可是 IPV4，也可以是 IPV6，以常规字符串表示即可，如 `127.0.0.11 或者 ::1，也支持 DNS 名称
     * port 客户端端口号
     */
    @GetMapping("/announce")
    public String announce(@ModelAttribute AnnounceRequestPo announceRequest,
                           @RequestHeader(name = "User-Agent") String ua,
                           @RequestHeader(name = "want-digest", required = false) String wantDigest) {

        convertAnnounceRequestParams(announceRequest);
        log.info("announce request: {}", announceRequest);
        TrackerAnnounceCompatVo compatVo = announceService.trackerAnnounce(announceRequest);
        // 转换编码并返回
        log.info("announce compatVo: {}", compatVo);
        return EncodeConvertUtils.encode(JSON.parseObject(JSON.toJSONString(compatVo)));
    }

    /**
     * 从请求中获取所有info_hash信息，
     * 从数据库中匹配出来对应的做种内容，
     * <p>
     * 一个特殊的请求，它允许用户获取关于一个或多个 torrent 的基本统计信息，而不需要完全加入 swarm（即 torrent 的下载/上传群体）。
     * 通常，这些信息包括：完成下载的次数（即有多少 peers 拥有了文件的全部内容）、正在下载的用户数量（leechers）、拥有完整文件并且正在分发的用户数量（seeders）。
     * 通过 scrape 请求，用户可以快速了解 torrent 的“健康状况”，而无需加入 swarm。例如，一个拥有很多 seeders 的 torrent 可能下载速度更快，表明它是一个活跃的 torrent。
     * 另一方面，如果一个 torrent 没有 seeders，那么新用户可能无法下载到完整的文件。
     * 但是，对于PT来说，scrape 请求对于一般用户而言并不是必需，用户可以从网站的页面上获得关于 torrent 健康状况的足够信息，无需直接进行 scrape 请求。
     * <p>
     * http://www.bittorrent.org/beps/bep_0048.html
     */
    @GetMapping("/scrape")
    public String processingScrape() {
        HttpServletRequest request = RequestContextHolderUtil.getRequest();
        log.info("accept scrape request: {}", request.getQueryString());
        throw new TrackerBencodeException("scrape failed");
    }

    private void convertAnnounceRequestParams(AnnounceRequestPo announceRequest) {
        HttpServletRequest request = RequestContextHolderUtil.getRequest();
        String[] queryParams = Optional.ofNullable(request.getQueryString())
                .filter(StringUtils::isNotBlank)
                .map(curr -> StringUtils.split(curr, TrackerConstants.PARAMS_SEPARATOR))
                .orElseThrow(() -> new TrackerBencodeException("query string is error!!!"));
        // hash
        // 20字节SHA1哈希。请注意，该值将是一个b编码的字典
        String infoHashHex = Arrays.stream(queryParams)
                .filter(curr -> StringUtils.contains(curr, TrackerConstants.INFO_HASH))
                .map(curr -> StringUtils.substringAfter(curr, String.format("%s=", TrackerConstants.INFO_HASH)))
                .map(String::getBytes)
                .map(EncodeConvertUtils::urlDecode)
                .peek(announceRequest::setInfoHash)
                .map(HexUtil::encodeHexStr)
                .findFirst()
                .orElseThrow(() -> new TrackerBencodeException("Missing necessary parameters!!!"));
        announceRequest.setInfoHashHex(infoHashHex);

        // 客户端ID
        // 客户端在启动时生成的用作客户端唯一ID的url编码的20字节字符串。
        // 它可以是任何值，也可以是二进制数据。它至少必须对您的本地机器是唯一的，因此应该包括诸如进程ID和可能在启动时记录的时间戳之类的东西。
        String peerId = Arrays.stream(queryParams)
                .filter(curr -> StringUtils.contains(curr, TrackerConstants.PEER_ID))
                .map(curr -> StringUtils.substringAfter(curr, String.format("%s=", TrackerConstants.PEER_ID)))
                .map(String::getBytes)
                .map(EncodeConvertUtils::urlDecode)
                .map(HexUtil::encodeHexStr)
                .findFirst()
                .orElseThrow(() -> new TrackerBencodeException("Missing necessary parameters!!"));
        announceRequest.setPeerIdHex(peerId);
        // 设置请求头信息
        announceRequest.setUserAgent(request.getHeader("User-Agent"));
        announceRequest.setWantDigest(request.getHeader("want-digest"));

        // 设置IP地址
        announceRequest.setRemoteAddr(IPUtils.getIpAddr(request));
        announceRequest.setSeeder(announceRequest.getLeft().equals(0L));
        // 设置汇报时间
        announceRequest.setAnnounceDateTime(LocalDateTime.now());
        log.info("accept announce to tracker,remoteAddr:{}", announceRequest.getRemoteAddr());
    }

}
