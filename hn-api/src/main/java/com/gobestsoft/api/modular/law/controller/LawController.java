package com.gobestsoft.api.modular.law.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gobestsoft.api.config.bean.BannerUtil;
import com.gobestsoft.api.modular.basic.BaseController;
import com.gobestsoft.api.modular.basic.BaseResult;
import com.gobestsoft.api.modular.cms.service.InformationService;
import com.gobestsoft.api.modular.srv.model.LawConsultParam;
import com.gobestsoft.api.modular.srv.service.LawService;
import com.gobestsoft.common.constant.CacheConstant;
import com.gobestsoft.common.modular.dao.model.SrvLawConsultationPojo;
import com.gobestsoft.common.modular.law.mapper.LawCommonProblemMapper;
import com.gobestsoft.core.reids.RedisCacheModel;
import com.gobestsoft.core.util.DateUtil;
import com.gobestsoft.core.util.ToolUtil;
import com.gobestsoft.core.util.WebSiteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务大厅
 * create by liD
 * on 2018/9/5 下午3:30
 */
@RestController
@RequestMapping("/lawApi")
public class LawController extends BaseController {

    @Autowired
    private LawService lawService;


    @Autowired
    private InformationService informationService;

    @Resource
    private LawCommonProblemMapper lawCommonProblemMapper;


    @Resource
    BannerUtil bannerUtil;


    /**
     * 提交法律援助问题
     *
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public BaseResult submit(@Valid LawConsultParam param, BindingResult result) {
        Integer deptId=null;
        if(getUserDto().getMember_info()!=null){
            deptId =  getUserDto().getMember_info().getDept_id();
        }
        lawService.submit(param, getUserId(),deptId);
        return success();
    }

    /**
     * 法律咨询和反应问题列表
     *
     * @param type (1:咨询, 2:反映问题)
     */
    @RequestMapping("/lawList")
    public BaseResult lawList(@RequestParam("type") int type) {
        List<Map<String, Object>> conAndqueMap = new ArrayList<>();
        List<SrvLawConsultationPojo> list = lawService.getConAndQues(getPageBounds(), getUserId(), type);
        for (SrvLawConsultationPojo param : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", param.getStatus());
            map.put("category", param.getCategory());
            map.put("createTime", param.getCreateTime());
            if (ToolUtil.isEmpty(param.getContent())) {
                map.put("content", "");
            } else {
                map.put("content", param.getContent());
            }
            if (ToolUtil.isEmpty(param.getReplyContent())) {
                map.put("replyContent", "");
            } else {
                map.put("replyContent", param.getReplyContent());
            }
            map.put("replyTime", DateUtil.parseAndFormat(param.getReplyTime(), "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm"));
            map.put("imageUrl", flwqConfig(type));
            conAndqueMap.add(map);
        }

        return success(conAndqueMap);
    }

    /**
     * 我的记录法律援助详情
     * create by xiashasha
     * on 2018/09/13 上午09:42
     *
     * @param id 法律援助id
     * @return
     */
    @RequestMapping("/lawSupportDetail")
    public BaseResult lawSupportDetail(@RequestParam("id") int id) {
        Map<String, Object> result = lawService.lawSupportDetail(id);
        if (result == null) {
            return fail("申请不存在");
        }
        return success(result);
    }

    /**
     * 我的记录法律援助
     * create by xiashasha
     * on 2018/09/13 上午09:42
     *
     * @return
     */
    @RequestMapping("/lawSupportList")
    public BaseResult lawSupportList(HttpServletRequest request) {

        Page page = getDefaultPage();


        List<Map<String, Object>> list = lawService.selectSupportListByUser(page, getUserId());


        for (Map m : list) {
            if (m.get("support_shape") == null) {
                continue;
            }
            try {
                String icon = getLawSupportImgage(Integer.valueOf(m.get("support_shape") + ""));
                m.put("icon", WebSiteUtil.getBrowseUrl(icon));
            } catch (Exception e) {
                return fail("法律援助形式的类型错误");
            }
        }
        return success(list);
    }

    /**
     * 法律讲堂首页
     *
     * @return
     */
    @RequestMapping("/forum")
    public BaseResult forum() {
        RedisCacheModel cacheModel = cacheFactory.getCacheModel(CacheConstant.APP_LAW_FORUM);
        if (cacheModel != null && !cacheModel.isExpired()) {
            return success(cacheModel.getData());
        }
        JSONArray categoryArray = ((JSONObject) getAppConfig("forum")).getJSONArray("category");
        List<Map<String, String>> category = new ArrayList<>();
        for (int i = 0; i < categoryArray.size(); i++) {
            JSONObject a = categoryArray.getJSONObject(i);
            Map<String, String> c = new HashMap<>();
            c.put("id", a.getString("id"));
            c.put("name", a.getString("name"));
            c.put("icon", WebSiteUtil.getBrowseUrl(a.getString("icon")));
            category.add(c);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("category", category);
        result.put("banners", bannerUtil.getBannerInfo("103", getUserId()));
        result.put("articles", informationService.getArticles(getPageBounds(), 117, null, null, 0, null));
        cacheFactory.cacheModel(CacheConstant.APP_LAW_FORUM, result, 180);
        return success(result);
    }

/**
 * 法律咨询常见问题
 *
 * @return
 */
 @RequestMapping("/commonProblem")
 public BaseResult commonProblem(){
        Page page = getDefaultPage();
        List<Map<String, Object>> data=lawCommonProblemMapper.selectLawCommonProblem(page);
       return success(data);
 }


}
