package com.gobestsoft.common.modular.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gobestsoft.common.modular.dao.model.SrvLawSupportPojo;
import com.gobestsoft.common.modular.system.model.Dept;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SrvLawSupportMapper extends BaseMapper<SrvLawSupportPojo> {


	/**
	 * 根据条件查询法律援助列表
	 * @param page
	 * @param name
	 * @param status
	 * @param type
	 * @return
	 */
	public List<Map<String,Object>> selectByCondition(
			@Param("page") Page<Map<String,Object>> page, 
			@Param("name") String name,
			@Param("status") String status,
			@Param("type") Integer type,
			@Param("workUnit") String workUnit,
			@Param("examine") String examine,
			@Param("deptLevel") Integer deptLevel,
			@Param("deptId") Integer deptId,
			@Param("deptIds") List<Integer> deptIds);

	/**
	 * 法律援助详情
	 * 
	 * create by xiashasha
	 * on 2018/09/13 上午09:57
	 * 
	 * @param id 法律援助id
	 * @return
	 */
	Map<String, Object> lawSupportDetail(@Param("id") int id);


	List<Map<String,Object>> selectSupportListByUser(@Param("page") Page<Map<String,Object>> page, @Param("uid") String uid);

	/**
	 * 查询所有子部门
	 * @param pid
	 * @return
	 */
    List<Map> selectSonDept(@Param("pid") Integer pid);

	/**
	 * 更新待审核审核人流水号，转办
	 * @param support_id
	 * @param transfer_dept_id
	 * @param transfer_reason
	 */
    int updateAuditDeptId(@Param("support_id")Integer support_id
			,@Param("transfer_dept_id")Integer transfer_dept_id
			,@Param("transfer_reason")String transfer_reason);

	/**
	 * 查询指定天数前申请而且至今没有操作审核的记录
	 * @param day
	 * @return
	 */
	List<Map<String,Object>> selectExpireAudit(@Param("day") int day);

	void updateExpireAudit(List<String> list);

	void updateExpireLog(List<String> list);

	Dept selectCityDept (@Param("id") Integer deptId);


}