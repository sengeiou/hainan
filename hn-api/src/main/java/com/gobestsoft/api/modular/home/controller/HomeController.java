package com.gobestsoft.api.modular.home.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gobestsoft.api.config.bean.BannerUtil;
import com.gobestsoft.api.config.cache.DictDto;
import com.gobestsoft.api.config.properties.ApiProperties;
import com.gobestsoft.api.modular.appuser.service.AppUserService;
import com.gobestsoft.api.modular.basic.BaseController;
import com.gobestsoft.api.modular.basic.BaseResult;
import com.gobestsoft.api.modular.basic.BasicRowBounds;
import com.gobestsoft.api.modular.cms.service.CourseService;
import com.gobestsoft.api.modular.cms.service.InformationService;
import com.gobestsoft.common.constant.CacheConstant;
import com.gobestsoft.common.constant.DictCodeConstants;
import com.gobestsoft.common.modular.dao.mapper.AppHomeDialogMapper;
import com.gobestsoft.common.modular.dao.mapper.AppVersionMapper;
import com.gobestsoft.common.modular.dao.model.AppVersionPojo;
import com.gobestsoft.common.modular.homepage.model.AppHomeDialog;
import com.gobestsoft.common.modular.system.dao.DeptDao;
import com.gobestsoft.common.modular.system.mapper.DictMapper;
import com.gobestsoft.core.node.ZTreeNode;
import com.gobestsoft.core.reids.RedisCacheModel;
import com.gobestsoft.core.util.AMapWeatherUtil;
import com.gobestsoft.core.util.HttpUtil;
import com.gobestsoft.core.util.ToolUtil;
import com.gobestsoft.core.util.WebSiteUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("")
@Controller
public class HomeController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * 默认城市CODE
     */
    private static final String DEF_CITY_CODE = "460100";

    @Autowired
    private InformationService informationService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AppVersionMapper appVersionMapper;

    @Resource
    DictMapper dictMapper;

    @Resource
    ApiProperties apiProperties;

    @Resource
    AppHomeDialogMapper appHomeDialogMapper;

    @Resource
    BannerUtil bannerUtil;

    @Resource
    DeptDao deptDao;


    /**
     * 获取首页
     *
     * @return
     */
    @RequestMapping("/home")
    @ResponseBody
    public BaseResult index(@RequestParam(value = "cityCode") String cityCode) throws IOException {
        Map<String, Object> result = new HashMap<>();
        // 天气状况取得
        Map<String, Object> weather;


        if (ToolUtil.isEmpty(cityCode)) {
            weather = AMapWeatherUtil.getWeatherV2(DEF_CITY_CODE);
        } else {
            weather = AMapWeatherUtil.getWeatherV2(cityCode);
        }

        // 天气
        result.put("weather", weather);
        // 头部banner
        //获取 顶部banners
        //result.put("topBanner", bannerUtil.getBannerInfo("0"));
        // 腰部banner
        //result.put("midBanner", bannerUtil.getBannerInfo("101"));

        List<String> cityList = cityList();

        // 工会要闻
        if (!cityList.contains(cityCode)) {
            cityCode = DEF_CITY_CODE;
        }
        result.put("articleList", informationService.getArticles(new BasicRowBounds(1, 5), 103, null, cityCode, 0, null));// 103 代表工会要闻

        //消息数量
        result.put("unreadMsg", unreadMsg());
        //获取腰部栏目1
        //获取腰部栏目2
        homeConfig(result);
        return success(result);
    }

    /**
     * 获取首页
     *
     * @return
     */
    @RequestMapping("/homeV2")
    @ResponseBody
    public BaseResult indexV2(@RequestParam(value = "cityCode") String cityCode) {
        RedisCacheModel homeV2 = cacheFactory.getCacheModel(CacheConstant.API_HOME_V2);
        Map<String, Object> result = new HashMap<>();
        if (homeV2 != null) {
            result = (Map<String, Object>) homeV2.getData();
            result.put("topBanner", bannerUtil.getBannerInfo("0", getUserId()));
            //腰部banner
            result.put("midBanner", bannerUtil.getBannerInfo("101", getUserId()));
            result.put("unreadMsg", unreadMsg());
            return success(result);
        }

        // 头部banner
        //获取 顶部banners
//        result.put("topBanner", bannerUtil.getBannerInfo("0", getUserId()));
        // 腰部banner
//        result.put("midBanner", bannerUtil.getBannerInfo("101", getUserId()));

        List<String> cityList = cityList();
        // 工会要闻
        // 103 代表工会要闻
        if (!cityList.contains(cityCode)) {
            cityCode = DEF_CITY_CODE;
        }
        result.put("articleList", informationService.getArticles(new BasicRowBounds(1, 5), 103, null, cityCode, 0, null));
        //消息数量
        result.put("unreadMsg", unreadMsg());
        //获取腰部栏目1
        //获取腰部栏目2
        homeConfig(result);

        cacheFactory.cacheModel(CacheConstant.API_HOME_V2, result, 180);
        result.put("topBanner", bannerUtil.getBannerInfo("0", getUserId()));
        // 腰部banner
        result.put("midBanner", bannerUtil.getBannerInfo("101", getUserId()));
        return success(result);
    }

    /**
     * 获取地区
     *
     * @return
     */
    private List<String> cityList() {
        List<DictDto> dtos = cacheFactory.getDictsByGroupCodes(DictCodeConstants.LIB_ADMINISTRATIVE_AREA);
        List<String> cityList = new ArrayList<>();
        List<DictDto> list = dtos.get(0).getDict();
        for (DictDto dict : list) {
            cityList.add(dict.getCode());
        }
        return cityList;
    }

    /**
     * 获取腰部栏目1
     *
     * @return
     */
    private void homeConfig(Map<String, Object> result) {
        JSONObject appHome = (JSONObject) getAppConfig("appHome");
        JSONArray firstMap = appHome.getJSONArray("firstListMap");
        //获取腰部栏目1
        result.put("firstListMap", getConfigArray(firstMap, 0));

        JSONArray secondMap = appHome.getJSONArray("secondListMap");
        //获取腰部栏目2
        result.put("secondListMap", getConfigArray(secondMap, 1));
    }


    /**
     * 获取数组
     *
     * @param jsonArray
     * @return
     */
    private List<Map<String, Object>> getConfigArray(JSONArray jsonArray, int index) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject f = jsonArray.getJSONObject(i);
            Map<String, Object> m = new HashMap<>();
            m.put("id", f.getString("id"));
            m.put("name", f.getString("name"));
            m.put("icon", WebSiteUtil.getBrowseUrl(f.getString("icon")));
            if (index == 1) {
                m.put("label", f.getString("label"));
            }
            result.add(m);
        }
        return result;
    }

    /**
     * 未读消息数量
     *
     * @return
     */
    private int unreadMsg() {
        if (isLogin()) {
            return appUserService.newMessageCount(getUserId());
        }
        return 0;
    }

    private String getActivityCount() {
        try {
            String token = StringUtils.isEmpty(getToken()) ? "" : getToken();
            String jsonData = HttpUtil.sendGet(apiProperties.getPhpSite() + "index.php?s=/w1/Train/Api/check_unread_act&token=" + token);
            JSONObject json = JSON.parseObject(jsonData);
            if (json.getInteger("code") == 200) {
                return "1";
            }
        } catch (Exception e) {
            return "0";
        }
        return "0";
    }

    /**
     * 获取首页中部3
     *
     * @return
     */
    private List<Map<String, String>> getWaist() {
        List<Map<String, String>> result = new ArrayList<>();
        String waistKey = "index-waist";
        if (redisUtils.exists(waistKey)) {
            result = (List<Map<String, String>>) redisUtils.get(waistKey);
        } else {
            Map<String, String> p1 = new HashMap<>();
            p1.put("position", "0");
            p1.put("title", "资讯天地");
            p1.put("description", "头条、动图、视频");
            p1.put("icon", "index/icon/zxtd.png");
            Map<String, String> p2 = new HashMap<>();
            p2.put("position", "1");
            p2.put("title", "互动乐园");
            p2.put("description", "活动赛事、婚恋交友、游戏娱乐");
            p2.put("icon", "index/icon/hdly.png");
            Map<String, String> p3 = new HashMap<>();
            p3.put("position", "2");
            p3.put("title", "职工社区");
            p3.put("description", "兴趣协会、职工秀吧");
            p3.put("icon", "index/icon/zgsq.png");
            result.add(p1);
            result.add(p2);
            result.add(p3);
            redisUtils.set("index-waist", result);
        }

        result.forEach(r -> {
            if (r.containsKey("icon")) {
                r.put("icon", WebSiteUtil.getBrowseUrl(r.get("icon")));
            }
        });
        return result;
    }

    /**
     * 全局查询
     *
     * @param keyword 关键字
     * @return
     */
    @RequestMapping(value = "/home/search", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult search(@RequestParam(value = "keyword", required = false) String keyword) {
        //根据全局搜索输入的关键字,查询并绑定矩阵、资讯、活动信息至list数组中
        List<Map<String, Object>> list = informationService.bindMatrixAndArticlesAndActivityDataByKeyword(
                keyword, getUserId(), getToken());
        return success(list);
    }


    /**
     * 收藏/点赞操作
     *
     * @param targetId  针对操作ID
     * @param which     针对哪种类型【0：资讯】【1：课程】【2：秀吧】
     * @param operation 操作类型【0：收藏】【1：点赞】
     * @return
     */
    @RequestMapping("/collectOrThumbsUp")
    @ResponseBody
    public BaseResult collectOrThumbsUp(@RequestParam("targetId") String targetId,
                                        @RequestParam("which") int which,
                                        @RequestParam("operation") int operation) {
        if (which == 0) {
            if (operation == 0) {
                return articleCollection(targetId);
            }
            if (operation == 1) {
                return articleGiveThumbsUp(targetId);
            }
        } else if (which == 1) {
            if (operation == 0) {
                return courseCollect(Integer.valueOf(targetId));
            }
            if (operation == 1) {
                return courseGiveThumbsUp(Integer.valueOf(targetId));
            }
        }

        return fail("无此操作");
    }

    /**
     * 资讯收藏
     *
     * @param articleId 资讯ID
     * @return
     */
    private BaseResult articleCollection(@RequestParam(name = "articleId") String articleId) {
        int code = informationService.articleOperation(articleId, getUserId(), 3);
        if (code == 200) {
            //'7', '资讯收藏' 添加积分
            appUserService.completeTaskGiveIntegral(getUserDto(), 7);
            return baseResult(200, "收藏成功", null);
        }
        return baseResult(201, "取消收藏成功", null);
    }


    /**
     * 资讯点赞操作
     *
     * @param articleId 资讯ID
     * @return
     */
    private BaseResult articleGiveThumbsUp(@RequestParam(name = "articleId") String articleId) {
        int code = informationService.articleOperation(articleId, getUserId(), 0);
        if (code == 200) {
            // '6', '资讯点赞' 添加积分
            appUserService.completeTaskGiveIntegral(getUserDto(), 6);
            return baseResult(200, "点赞成功", null);
        }
        return baseResult(201, "取消点赞成功", null);
    }

    /**
     * 课程点赞
     *
     * @param courseId
     * @return
     */
    private BaseResult courseGiveThumbsUp(@RequestParam(name = "courseId") int courseId) {
        int code = courseService.articleOperation(courseId, getUserId(), 0);
        if (code == 200) {
            return baseResult(200, "点赞成功", null);
        }
        return baseResult(200, "取消点赞成功", null);
    }

    /**
     * 课程收藏
     *
     * @param courseId
     * @return
     */
    private BaseResult courseCollect(@RequestParam(name = "courseId") int courseId) {
        int code = courseService.articleOperation(courseId, getUserId(), 1);
        if (code == 200) {
            return baseResult(200, "收藏成功", null);
        }
        return baseResult(200, "取消收藏成功", null);
    }

    /**
     * 验证设备版本
     *
     * @param versionCode 版本登记
     * @param type        设备
     * @return
     */
    @RequestMapping("/home/validAppVersion")
    @ResponseBody
    public BaseResult validAppVersion(@RequestParam("versionCode") int versionCode, @RequestParam("type") int type) {
        AppVersionPojo appVersion = appVersionMapper.getLastAppVersion(type);
        if (appVersion == null || versionCode >= appVersion.getVersionCode()) {
            return baseResult(200, "当前已是最新版本", null);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("version_code", appVersion.getVersionCode());
        result.put("version_name", appVersion.getVersionName());
        result.put("description", appVersion.getDescription());
        result.put("must", "02".equals(appVersion.getForceUpgrade()));
        result.put("download_address", WebSiteUtil.getBrowseUrl(appVersion.getDownloadAddress()));
        if (result != null) {
            return baseResult(201, "请更新版本", result);
        }
        return baseResult(200, "当前已是最新版本", null);
    }


    /**
     * 获取字典
     *
     * @param groupCodes
     * @return
     */
    @RequestMapping("/getDicts")
    @ResponseBody
    public BaseResult getDicts(@RequestParam("groupCodes") String groupCodes,HttpServletResponse response) {
        //response.setHeader("Access-Control-Allow-Origin", "*");
        return success(cacheFactory.getDictsByGroupCodes(groupCodes));
    }


    @RequestMapping("/getBigData/getDicts")
    @ResponseBody
    public BaseResult getDicts2(@RequestParam("groupCodes") String groupCodes,HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return success(cacheFactory.getDictsByGroupCodes(groupCodes));
    }

    /**
     * 首页弹框
     */
    @RequestMapping("/getDialog")
    @ResponseBody
    public BaseResult getDialog() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<AppHomeDialog> appHomeDialog = appHomeDialogMapper.selectList(new EntityWrapper().orderBy("create_time", false));
        AppHomeDialog result = appHomeDialog.get(0);
        result.setShowImg(WebSiteUtil.getBrowseUrl(result.getShowImg()));

        map.put("id", result.getId());
        map.put("title", result.getTitle());
        map.put("showImg", result.getShowImg());
        map.put("jumpUrl", result.getJumpUrl());
        map.put("isShow", result.getIsShow());
        map.put("isForce", result.getIsForce());
        return success(map);
    }


    /**
     * @author yjw
     * @Description:保存留言信息
     * @date: 14:36 2018/10/17
     */
    @RequestMapping(value = "/insertLeaveMessage", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult insertLeaveMessage
    (@RequestParam("content") String content, @RequestParam("contact") String contact) {
        Map map = new HashMap();
        map.put("content", content);
        map.put("contact", contact);
        map.put("auid", getUserId());
        appUserService.insertLeaveMessage(map);
        return baseResult(200, "保存成功", null);
    }


    /**
     * 用户注册协议/用户服务协议
     *
     * @return
     */
    @RequestMapping("/yhzcxy")
    public String yhzcxy() {
        return "yhzcxy";
    }

    /**
     * 联系我们
     *
     * @return
     */
    @RequestMapping("/lxwm")
    public String lxwm() {
        return "lxwm";
    }


    /**
     * 操作手册
     *
     * @return
     */
    @RequestMapping("/czsc")
    public String czsc() {
        return "czsc";
    }



    /**
     * 获取组织信息
     */
    @RequestMapping(value = "/getDeptInfo", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getDeptInfo(){
        List<ZTreeNode> tree = deptDao.belowTreeByInfo(1);
        return success(tree);
    }

    /**
     * 获取注销协议
     */
    @RequestMapping(value = "/getSignOutTip", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getSignOutTip(){

        try {
            JSONObject json = (JSONObject) getAppConfig("signOut_content");
            return success(json);

        }catch (Exception e){
            throw new RuntimeException("解析配置异常，无法获取协议内容");
        }
    }


}
