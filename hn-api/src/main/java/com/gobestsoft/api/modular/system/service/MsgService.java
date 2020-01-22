package com.gobestsoft.api.modular.system.service;

import com.gobestsoft.common.modular.mst.dao.MstOrganizationDao;
import com.gobestsoft.common.modular.system.dao.DeptDao;
import com.gobestsoft.common.modular.system.dao.MsgDao;
import com.gobestsoft.common.modular.system.mapper.DeptMapper;
import com.gobestsoft.common.modular.system.model.SysMsg;
import com.gobestsoft.core.util.DateUtil;
import com.baomidou.mybatisplus.plugins.Page;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消息服务
 *
 * @author li
 * @date 
 */
@Service
@Transactional
public class MsgService {
	
	@Resource
	MsgDao msgDao;

    @Resource
    DeptMapper deptMapper;

    @Resource
    DeptDao deptDao;

    @Resource
    MstOrganizationDao mstOrganizationDao;


    //工会组织树级别 （0：中国海南； 1：50个局级单位；2: 分公司；3: 项目部）
    private final static Integer[] LEVEL = {1, 2, 3};
    
    /**
     * 插入一条消息
     * @param toDeptId 推送组织ID
     * @param isOpen    是否推送
     * @param content  消息内容
     * @param type     消息类型：0：建会审批。1：入会审批。
     * @param 
     */
    public void insertMsg(Integer toDeptId
     	   				 ,String isOpen
     	   				 ,String content
     	   				 ,String type
     	   				 ) {
    	SysMsg sysMsg = new SysMsg();
    	sysMsg.setToDeptId(toDeptId);
    	sysMsg.setIsOpen(isOpen);
    	sysMsg.setContent(content);
    	sysMsg.setType(type);
    	sysMsg.setCreateTime(DateUtil.getAllTime());
    	msgDao.insert(sysMsg);
    }


}