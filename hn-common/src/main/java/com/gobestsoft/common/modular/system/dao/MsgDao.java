package com.gobestsoft.common.modular.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gobestsoft.common.modular.system.model.SysMsg;

/**
 * 消息的dao
 * @author li
 * @date 2018年9月4日 
 */
@Repository
public interface MsgDao extends BaseMapper<SysMsg>{
	
	public List<SysMsg> msgList(@Param("page") Page<SysMsg> page,@Param("dto") SysMsg dto);

	@Update(" UPDATE sys_message SET is_open = 1 WHERE id = #{id} ")
	public int changeIsOpen(@Param("id") int id);

}
