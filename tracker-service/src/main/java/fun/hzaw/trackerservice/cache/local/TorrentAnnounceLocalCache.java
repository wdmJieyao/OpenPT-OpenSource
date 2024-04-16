package fun.hzaw.trackerservice.cache.local;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import fun.hzaw.commonbean.date.DateTimeConvertUtils;
import fun.hzaw.trackerservice.cache.TorrentAnnounceCache;
import fun.hzaw.trackerservice.pojo.po.AnnounceRequestPo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: OpenPT
 * @description: 客户端本地缓存
 * @author: Luke
 * @create: 2024/4/12
 **/
// todo 使用配置化来决定是使用本地缓存还是使用redis缓存
@Component
public class TorrentAnnounceLocalCache implements TorrentAnnounceCache {

    private static final Cache<String, Set<AnnounceRequestPo>> LOCAL_ANNOUNCE_CACHE =
            CacheBuilder.newBuilder()
                    .initialCapacity(500) // 初始容量
                    .maximumSize(10000L)   // 设定最大容量
                    // 设定写入过期时间
                    .expireAfterAccess(Duration.ofDays(7))
                    // 设置最大并发写操作线程数
                    .concurrencyLevel(8)
                    // 开启缓存执行情况统计
                    .build();

    @Override
    public void add(AnnounceRequestPo announceRequestPo) {
        Set<AnnounceRequestPo> hadAnnounceSet = LOCAL_ANNOUNCE_CACHE.asMap()
                .getOrDefault(announceRequestPo.getInfoHashHex(), Sets.newHashSet());
        int oldSize = hadAnnounceSet.size();
        hadAnnounceSet.removeIf(curr ->
                StringUtils.equals(announceRequestPo.getPeerIdHex(), curr.getPeerIdHex()) &&
                        DateTimeConvertUtils.getTimestampFromLDT(announceRequestPo.getAnnounceDateTime()) > DateTimeConvertUtils.getTimestampFromLDT(curr.getAnnounceDateTime()));

        if (CollectionUtils.isEmpty(hadAnnounceSet) || oldSize > hadAnnounceSet.size()) {
            hadAnnounceSet.add(announceRequestPo);
        }
        LOCAL_ANNOUNCE_CACHE.put(announceRequestPo.getInfoHashHex(), hadAnnounceSet);
    }

    @Override
    public void remove(String infoHashHex) {
        if (!LOCAL_ANNOUNCE_CACHE.asMap().containsKey(infoHashHex)) {
            return;
        }

        LOCAL_ANNOUNCE_CACHE.invalidate(infoHashHex);
    }

    @Override
    public Set<AnnounceRequestPo> get(String infoHashHex) {
        return LOCAL_ANNOUNCE_CACHE.asMap().getOrDefault(infoHashHex, Sets.newHashSet());
    }

    @Override
    public Set<AnnounceRequestPo> getExcludeSelf(String infoHashHex, String peerIdHex) {
        // 获取并过滤当前能够拿到的客户端信息
        return Optional.ofNullable(LOCAL_ANNOUNCE_CACHE.getIfPresent(infoHashHex))
                .map(announceSet -> announceSet.stream()
                        .filter(curr -> !StringUtils.equals(curr.getPeerIdHex(), peerIdHex))
                        .collect(Collectors.toSet())
                )
                .orElse(Sets.newHashSet());
    }
}
