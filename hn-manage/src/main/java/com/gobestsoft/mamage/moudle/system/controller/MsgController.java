package com.gobestsoft.mamage.moudle.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.gobestsoft.common.modular.system.model.SysMsg;
import com.gobestsoft.core.util.ToolUtil;
import com.gobestsoft.mamage.basic.BaseController;
import com.gobestsoft.mamage.basic.page.PageInfoBT;
import com.gobestsoft.mamage.moudle.system.service.MsgService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/sysMsg")
public class MsgController extends BaseController {

    private String PREFIX = "/system/msg/";

    @Resource
    MsgService msgService;

    /**
     * 跳转到首页
     * @return
     */
    @RequestMapping("/page/{page}")
    public String index(@PathVariable String page,@RequestParam(required = false)Integer id, Model model) {
        if(ToolUtil.isNotEmpty(id)){
            model.addAttribute("item", msgService.selectById(id));
        }
        return PREFIX + page;
    }

    /**
     * 多条件分页查询
     *
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public PageInfoBT list(SysMsg dto) {
        Page<SysMsg> page = defaultPage();
        dto.setToDeptId(getLoginUser().getDeptId());
        List<SysMsg> msgList = msgService.msgList(page, dto);
        page.setRecords(msgList);
        return super.packForBT(page);
    }

    @GetMapping("/isRedPoint")
    @ResponseBody
    public int isRedPoint() {
        SysMsg dto = new SysMsg();
        dto.setIsOpen("0");
        PageInfoBT<SysMsg> ret = this.list(dto);
        return ret.getRows().size();

    }

    @GetMapping("/changeIsOpen")
    @ResponseBody
    public int changeIsOpen(int id){
        return this.msgService.changeIsOpen(id);
    }


}
