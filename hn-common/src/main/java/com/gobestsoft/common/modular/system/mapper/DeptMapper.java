package com.gobestsoft.common.modular.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gobestsoft.common.modular.system.model.Dept;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  * 部门表 Mapper 接口
 * </p>
 *
 * @author gobestsoft
 * @since 2017-07-11
 */
@Repository
public interface DeptMapper extends BaseMapper<Dept> {

    List<String> selectSonDeptIdByPid(@Param("pid") Integer pid);

    List<Integer> selectSecondDeptIds();

    int selectSecondDeptIdByDeptId(Integer deptId);

    String selectSimplenameByLogFullname(String LogFullname);

    String selectSimplenameByLogDeptId(int deptid);

    String selectSimplenameByDeptId(int deptid);

    String selectSimplenameByUid(String uid);
}