package fun.hzaw.trackerservice.service;

import fun.hzaw.trackerservice.pojo.po.AnnounceRequestPo;
import fun.hzaw.trackerservice.pojo.vo.TrackerAnnounceCompatVo;

/**
 * @program: OpenPT
 * @description: 种子汇报服务
 * @author: Luke
 * @create: 2024/4/12
 **/
public interface AnnounceService {

    /**
     * 种子汇报
     * @param announceRequest
     * @return
     */
    TrackerAnnounceCompatVo trackerAnnounce(AnnounceRequestPo announceRequest);
}
