package com.gobestsoft.api.config.bean;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gobestsoft.api.config.properties.ApiProperties;
import com.gobestsoft.common.modular.dao.mapper.AppBannerMapper;
import com.gobestsoft.common.modular.dao.model.AppBannerPojo;
import com.gobestsoft.core.util.WebSiteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 轮播图共通
 *
 * Created by duanmu on 2018/9/26.
 */
@Component
public class BannerUtil {

    @Autowired
    private AppBannerMapper appBannerMapper;

    @Autowired
    private ApiProperties apiProperties;

    /**
     * 获取banner信息
     */
    public List<Map<String, Object>> getBannerInfo(String category, String userId){

        List<Map<String, Object>> bannerListMap = new ArrayList<Map<String, Object>>();
        List<AppBannerPojo> banners = appBannerMapper
                    .selectList(new EntityWrapper<AppBannerPojo>().eq("category", category).eq("status", 0).orderBy("create_time", false));
        for(AppBannerPojo pojo : banners){
            Map<String, Object> bannerMap = new HashMap<String, Object>();
            bannerMap.put("target_id", pojo.getTargetId());
            if(pojo.getType() == 0){
                if(pojo.getJumpUrl().startsWith("https://www.yunkaoxun.com/")){
                    //bannerMap.put("jump_url", pojo.getJumpUrl() + "?token={0}");
                    bannerMap.put("jump_url", pojo.getJumpUrl() + "?userid=" + userId + "&token={0}");
                } else {
                    bannerMap.put("jump_url", pojo.getJumpUrl());
                }
            } else if(pojo.getType() == 1){
                bannerMap.put("jump_url", apiProperties.getReviewBaseUrl(pojo.getTargetId(), false));
            }
            bannerMap.put("type", pojo.getType());
            bannerMap.put("title", pojo.getTitle());
            bannerMap.put("cover", WebSiteUtil.getBrowseUrl(pojo.getCover()));
            bannerMap.put("auth",pojo.getAuth());//用户权限
            bannerListMap.add(bannerMap);
        }

        return bannerListMap;

    }
}
