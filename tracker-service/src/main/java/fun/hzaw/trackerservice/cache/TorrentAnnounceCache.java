package fun.hzaw.trackerservice.cache;

import fun.hzaw.trackerservice.pojo.po.AnnounceRequestPo;

import java.util.Set;

public interface TorrentAnnounceCache {
    /**
     * 添加缓存
     *
     * @param announceRequestPo
     */
    void add(AnnounceRequestPo announceRequestPo);

    /**
     * 移除缓存
     *
     * @param infoHashHex
     */
    void remove(String infoHashHex);

    /**
     * 根据种子hashhex获取客户端信息
     *
     * @param infoHashHex
     * @return
     */
    Set<AnnounceRequestPo> get(String infoHashHex);

    /**
     * 根据种子hashhex获取客户端信息(排除自身)
     *
     * @param infoHashHex
     * @param peerIdHex
     * @return
     */
    Set<AnnounceRequestPo> getExcludeSelf(String infoHashHex, String peerIdHex);

}
