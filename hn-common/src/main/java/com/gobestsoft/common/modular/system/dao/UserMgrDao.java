package com.gobestsoft.common.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.gobestsoft.common.modular.system.model.User;
import com.gobestsoft.core.node.UserNode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 管理员的dao
 *
 * @author gobestsoft
 * @date 2017年2月12日 下午8:43:52
 */
@Repository
public interface UserMgrDao {

    /**
     * 修改用户状态
     *
     * @date 2017年2月12日 下午8:42:31
     */
    public int setStatus(@Param("userId") String userId, @Param("status") int status);

    /**
     * 修改密码
     *
     * @param userId
     * @param pwd
     * @date 2017年2月12日 下午8:54:19
     */
    public int changePwd(@Param("userId") String userId, @Param("pwd") String pwd);

    /**
     * 设置用户的角色
     *
     * @return
     * @date 2017年2月13日 下午7:31:30
     */
    public int setRoles(@Param("userId") String userId, @Param("roleIds") String roleIds);

    /**
     * 通过账号获取用户
     *
     * @param account
     * @param userType
     * @return
     * @date 2017年2月17日 下午11:07:46
     */
    public User getByAccount(@Param("account") String account, @Param("userType") String userType);

    /**
     * 获取userTree的节点列表
     */
    public List<UserNode> userTree();

    /**
     * @param page
     * @param name
     * @param beginTime
     * @param endTime
     * @param deptid
     * @return
     * @author liushuwei
     * @date 2017年12月25日-下午3:54:22
     */
    public List<Map<String, Object>> selectUsersByCondition(@Param("page") Page<Map<String, Object>> page, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptid") Integer deptid);

    int queryAccountByUsername(String username);
}
