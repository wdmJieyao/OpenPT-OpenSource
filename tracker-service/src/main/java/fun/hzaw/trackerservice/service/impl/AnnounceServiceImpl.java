package fun.hzaw.trackerservice.service.impl;

import fun.hzaw.trackerservice.cache.local.TorrentAnnounceLocalCache;
import fun.hzaw.trackerservice.pojo.po.AnnounceRequestPo;
import fun.hzaw.trackerservice.pojo.vo.PeerVo;
import fun.hzaw.trackerservice.pojo.vo.TrackerAnnounceCompatVo;
import fun.hzaw.trackerservice.service.AnnounceService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: OpenPT
 * @description: 种子汇报服务
 * @author: Luke
 * @create: 2024/4/12
 **/

@Slf4j
@Component
public class AnnounceServiceImpl implements AnnounceService {

    @Resource
    private TorrentAnnounceLocalCache torrentAnnounceLocalCache;

    @Override
    public TrackerAnnounceCompatVo trackerAnnounce(AnnounceRequestPo announceRequest) {
        // 缓存当前客户端
        torrentAnnounceLocalCache.add(announceRequest);
        // 返回同伴客户端
        Set<AnnounceRequestPo> requestPos = torrentAnnounceLocalCache.getExcludeSelf(announceRequest.getInfoHashHex(), announceRequest.getPeerIdHex());
        if (CollectionUtils.isEmpty(requestPos)) {
            if (announceRequest.getSeeder()) {
                return TrackerAnnounceCompatVo.noAnyPeers(1L, 0L);
            }
            return TrackerAnnounceCompatVo.noAnyPeers(0L, 1L);
        }

        // 已完成量
        AtomicLong completeNum = new AtomicLong(0);
        // 未完成量
        AtomicLong incomplete = new AtomicLong(0);
        // 产生客户端列表，按照汇报时间倒序返回
        List<PeerVo> peerList = requestPos.stream()
                .sorted(Comparator.comparing(AnnounceRequestPo::getAnnounceDateTime).reversed())
                .limit(announceRequest.getNumwant())
                .map(curr -> {
                    // 统计完成情况
                    if (curr.getSeeder()) {
                        completeNum.incrementAndGet();
                    } else {
                        incomplete.incrementAndGet();
                    }

                    PeerVo peerVo = new PeerVo();
                    peerVo.setPeerId(curr.getPeerIdHex());
                    peerVo.setIp(curr.getRemoteAddr());
                    peerVo.setPort(curr.getPort());
                    return peerVo;
                }).toList();

        // 产生返回值
        TrackerAnnounceCompatVo announceCompatVo = new TrackerAnnounceCompatVo();
        announceCompatVo.setInterval(15 * 60L);
        announceCompatVo.setMinInterval(30L);
        announceCompatVo.setTrackerId("47350bda-62ba-466d-b50b-da62ba266d41");
        announceCompatVo.setComplete(completeNum.get());
        announceCompatVo.setIncomplete(incomplete.get());
        // todo 跟种子信息挂钩
         announceCompatVo.setDownloaded(completeNum.get());
        announceCompatVo.setDownloaders((long) requestPos.size());
        announceCompatVo.setPeers(peerList);
        return announceCompatVo;
    }
}
