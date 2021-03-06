package com.gobestsoft.admin.modular.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.gobestsoft.common.constant.DictCodeConstants;
import com.gobestsoft.common.constant.UploadConstants;
import com.gobestsoft.common.modular.system.dao.UserMgrDao;
import com.gobestsoft.common.modular.system.mapper.DeptMapper;
import com.gobestsoft.common.modular.system.mapper.UserMapper;
import com.gobestsoft.common.modular.system.model.Dept;
import com.gobestsoft.common.modular.system.model.Dict;
import com.gobestsoft.common.modular.system.model.User;
import com.gobestsoft.core.util.DateUtil;
import com.gobestsoft.core.util.MD5Util;
import com.gobestsoft.core.util.ToolUtil;
import com.gobestsoft.core.util.UUIDUtil;
import com.gobestsoft.mamage.basic.BaseController;
import com.gobestsoft.mamage.basic.tips.Tip;
import com.gobestsoft.mamage.constant.Const;
import com.gobestsoft.mamage.constant.factory.ConstantFactory;
import com.gobestsoft.mamage.constant.factory.IConstantFactory;
import com.gobestsoft.mamage.constant.state.ManagerStatus;
import com.gobestsoft.mamage.exception.BizExceptionEnum;
import com.gobestsoft.mamage.exception.BusinessException;
import com.gobestsoft.mamage.moudle.system.factory.UserFactory;
import com.gobestsoft.mamage.moudle.system.service.RoleService;
import com.gobestsoft.mamage.moudle.system.transfer.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.naming.NoPermissionException;
import javax.validation.Valid;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 系统管理员控制器
 *
 * @author gobestsoft
 * @Date 2017年1月11日 下午1:08:17
 */
@Controller
@RequestMapping("/mgr")
public class UserMgrController extends BaseController {

    private static String PREFIX = "/system/user/";

    @Resource
    private UserMgrDao managerDao;

    @Resource
    private UserMapper userMapper;

    @Resource
    DeptMapper deptMapper;

    @Resource
    IConstantFactory constant;

    @Resource
    RoleService roleService;

    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "user";
    }

    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("/user_add/{deptId}")
    public String addView(@PathVariable String deptId, Model model) {
        model.addAttribute("deptid", deptId);
        return PREFIX + "user_add";
    }

    /**
     * 跳转到角色分配页面
     */
    //@RequiresPermissions("/mgr/role_assign")  //利用shiro自带的权限检查
    @RequestMapping("/role_assign/{userId}")
    public String roleAssign(@PathVariable String userId, Model model) throws BusinessException {
        if (ToolUtil.isEmpty(userId)) {
            throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
        }
//        User user = (User) Db.create(UserMapper.class).selectOneByCon("uid", userId);
        User user = this.userMapper.selectById(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("userAccount", user.getAccount());
        return PREFIX + "user_roleassign";
    }

    /**
     * 跳转到编辑管理员页面
     */
    @RequestMapping("/user_edit/{userId}")
    public String userEdit(@PathVariable String userId, Model model) throws BusinessException {
        if (ToolUtil.isEmpty(userId)) {
            throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        User user = this.userMapper.selectById(userId);
        model.addAttribute(user);
        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));
        return PREFIX + "user_edit";
    }

    /**
     * 跳转到查看用户详情页面
     */
    @RequestMapping("/user_info")
    public String userInfo(Model model) throws BusinessException {
        String userId = getLoginUser().getId();
        if (ToolUtil.isEmpty(userId)) {
            throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
        }
        User user = this.userMapper.selectById(userId);
        model.addAttribute(user);
        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));
        return PREFIX + "user_view";
    }


    /**
     * 查询管理员列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) Integer deptid) {

        if (deptid == null) {
            deptid = getLoginUser().getDeptId();
        }
        Page<Map<String, Object>> page = defaultPage();
        if (StringUtils.isNotEmpty(beginTime)) {
            beginTime = beginTime.replace("-", "");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            endTime = endTime.replace("-", "");
        }

        // 获取一览数据
        List<Map<String, Object>> result = managerDao.selectUsersByCondition(page, name, beginTime, endTime, deptid);

        // 表示数据补全
        for (Map<String, Object> map : result) {
            // 审核状态
            if (ToolUtil.isNotEmpty(map.get("statusName"))) {
                Dict dict = constant.findInDictName(DictCodeConstants.LIB_USER_STATUS, map.get("statusName").toString());
                map.put("statusName", ToolUtil.isNotEmpty(dict) ? dict.getName() : "");
            }
            //性别
            if (ToolUtil.isNotEmpty(map.get("sexName"))) {
                Dict dict = constant.findInDictName(DictCodeConstants.LIB_SEX, map.get("sexName").toString());
                map.put("sexName", ToolUtil.isNotEmpty(dict) ? dict.getName() : "");
            }
        }
        page.setRecords(result);
        return super.packForBT(page);
    }

    /**
     * 添加管理员
     */
    @RequestMapping("/add")
//    @BussinessLog(value = "添加管理员", key = "account", dict = UserDict.class)
    @ResponseBody
    @Transactional
    public Tip add(@Valid UserDto user, @RequestParam("roleIds") String roleIds, @RequestParam(value = "userType", defaultValue = "0") String userType, BindingResult result) throws BusinessException {
        if (result.hasErrors()) {
            return fail(result.getFieldError().getDefaultMessage());
        }

        // 判断账号是否重复
        User theUser = managerDao.getByAccount(user.getAccount(), null);
        if (theUser != null) {
            return fail("创建账号已存在");
        }

        // 完善账号信息
        user.setSalt(UUIDUtil.getCharAndNumr(5));
        user.setPassword(MD5Util.encrypt(Const.DEFAULT_PWD + user.getSalt()));
        user.setStatus(ManagerStatus.OK.getCode());
        user.setCreatetime(DateUtil.getAllTime());
        user.setUserType(userType);
        if (ToolUtil.isEmpty(roleIds)) {
            roleIds = "10";
        }
        user.setRoleid(roleIds);
        //判断deptid对应的是部门还是组织 1(部门) 0(组织) 如果是部门，将他上级的id存入org_id ,组织则存当前deptid
        Dept selectDept = deptMapper.selectById(user.getDeptid());
        if (selectDept != null && selectDept.getDeptType() != null) {
            if (selectDept.getDeptType() == 0) {
                user.setOrgid(selectDept.getId());
            } else if (selectDept.getDeptType() == 1) {
                user.setOrgid(selectDept.getPid());
            }
        }
        User inserUser = UserFactory.createUser(user);
        this.userMapper.insert(inserUser);
        return success();
    }

    /**
     * 修改管理员
     *
     * @throws NoPermissionException
     */
    @RequestMapping("/edit")
//    @BussinessLog(value = "修改管理员", key = "account", dict = UserDict.class)
    @ResponseBody
    public Tip edit(@Valid UserDto user, @RequestParam("userId") String userId, @RequestParam("roleIds") String roleIds, BindingResult result) throws NoPermissionException, BusinessException {
        if (result.hasErrors()) {
            throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
        }
//        if (ShiroKit.hasRole(Const.ADMIN_NAME)) {
        //不能修改超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new BusinessException(BizExceptionEnum.CANT_CHANGE_ADMIN);
        }
        assertAuth(userId);
        if (ToolUtil.isEmpty(roleIds)) {
            roleIds = "10";
        }
        this.managerDao.setRoles(userId, roleIds);
        //非管理员，并且不是操作本人账户
//        	String loginaccount = ShiroKit.getUser().getAccount();
//        	String account = user.getAccount();
//        	if("admin".equals(loginaccount)){
//        		this.roleService.setAuthority(userId, menuIds);
//        	}else if(!loginaccount.equals(account)){
//        		this.roleService.setAuthority(userId, menuIds);
//        	}

        this.userMapper.updateById(UserFactory.updateUser(user));
        return success();
//        } else {
//            assertAuth(user.getId());
//            ShiroUser shiroUser = ShiroKit.getUser();
//            if (shiroUser.getId().equals(user.getId())) {
//            	this.managerDao.setRoles(userId, roleIds);
//            	//非管理员，并且不是操作本人账户
//            	String loginaccount = ShiroKit.getUser().getAccount();
//            	String account = user.getAccount();
//            	if("admin".equals(loginaccount)){
//            		this.roleService.setAuthority(userId, menuIds);
//            	}else if(!loginaccount.equals(account)){
//            		this.roleService.setAuthority(userId, menuIds);
//            	}
//                this.userMapper.updateById(UserFactory.createUser(user));
//                return SUCCESS_TIP;
//            } else {
//                throw new BussinessException(BizExceptionEnum.NO_PERMITION);
//            }
//        }
    }

    /**
     * 修改管理员
     *
     * @throws NoPermissionException
     */
    @RequestMapping("/editSelf")
//    @BussinessLog(value = "修改个人信息", key = "account", dict = UserDict.class)
    @ResponseBody
    public Tip editSelf(@Valid UserDto user, @RequestParam("userId") String userId, BindingResult result) throws NoPermissionException, BusinessException {
        if (result.hasErrors()) {
            throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
        }
        if (hasRole(Const.ADMIN_ROLE_ID)) {
            //不能修改超级管理员
            if (userId.equals(Const.ADMIN_ID)) {
                throw new BusinessException(BizExceptionEnum.CANT_CHANGE_ADMIN);
            }
            assertAuth(userId);
            this.userMapper.updateById(UserFactory.updateUser(user));
            return success();
        } else {
            assertAuth(user.getId());
            if (getLoginUser().getId().equals(user.getId())) {
                this.userMapper.updateById(UserFactory.createUser(user));
                return success();
            } else {
                throw new BusinessException(BizExceptionEnum.NO_PERMITION);
            }
        }
    }

    /**
     * 删除管理员（逻辑删除）
     */
    @RequestMapping("/delete")
//    @BussinessLog(value = "删除管理员", key = "userId", dict = UserDict.class)
    @ResponseBody
    public Tip delete(@RequestParam String userId) throws BusinessException {
        if (ToolUtil.isEmpty(userId)) {
            throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能删除超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new BusinessException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }
        assertAuth(userId);
        this.managerDao.setStatus(userId, ManagerStatus.DELETED.getCode());
        return success();
    }

    /**
     * 查看管理员详情
     */
    @RequestMapping("/view/{userId}")
    @ResponseBody
    public User view(@PathVariable String userId) throws BusinessException {
        if (ToolUtil.isEmpty(userId)) {
            throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        return this.userMapper.selectById(userId);
    }

    /**
     * 重置管理员的密码
     */
    @RequestMapping("/reset")
//    @BussinessLog(value = "重置管理员密码", key = "userId", dict = UserDict.class)
//    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip reset(@RequestParam String userId) throws BusinessException {
        if (ToolUtil.isEmpty(userId)) {
            throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        User user = this.userMapper.selectById(userId);
        user.setSalt(UUIDUtil.getCharAndNumr(5));
        user.setPassword(MD5Util.encrypt(Const.DEFAULT_PWD + user.getSalt()));
        this.userMapper.updateById(user);
        return success();
    }

    /**
     * 冻结用户
     */
    @RequestMapping("/freeze")
//    @BussinessLog(value = "冻结用户", key = "userId", dict = UserDict.class)
//    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip freeze(@RequestParam String userId) throws BusinessException {
        if (ToolUtil.isEmpty(userId)) {
            throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能冻结超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new BusinessException(BizExceptionEnum.CANT_FREEZE_ADMIN);
        }
        assertAuth(userId);
        this.managerDao.setStatus(userId, ManagerStatus.FREEZED.getCode());
        return success();
    }

    /**
     * 解除冻结用户
     */
    @RequestMapping("/unfreeze")
//    @BussinessLog(value = "解除冻结用户", key = "userId", dict = UserDict.class)
//    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip unfreeze(@RequestParam String userId) throws BusinessException {
        if (ToolUtil.isEmpty(userId)) {
            throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        this.managerDao.setStatus(userId, ManagerStatus.OK.getCode());
        return success();
    }

    /**
     * 分配角色
     */
    @RequestMapping("/setRole")
//    @BussinessLog(value = "分配角色", key = "userId,roleIds", dict = UserDict.class)
//    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip setRole(@RequestParam("userId") String userId, @RequestParam("roleIds") String roleIds) throws BusinessException {
        // sunbin 角色应该可以清除
//        if (ToolUtil.isOneEmpty(userId, roleIds)) {
//            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
//        }
        //不能修改超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new BusinessException(BizExceptionEnum.CANT_CHANGE_ADMIN);
        }
        assertAuth(userId);
        this.managerDao.setRoles(userId, roleIds);
        return success();
    }

    /**
     * 上传图片(上传到项目的webapp/static/img)
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("file") MultipartFile picture) throws BusinessException {
        String pictureName = UUID.randomUUID().toString() + ".jpg";
        try {
            String fileSavePath = manageProperties.getFileUploadPath() + UploadConstants.AVATAR;

            File p = new File(fileSavePath);
            if (!p.exists()) {
                p.mkdirs();
            }
            picture.transferTo(new File(fileSavePath + pictureName));
        } catch (Exception e) {
            throw new BusinessException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return UploadConstants.AVATAR + pictureName;
    }

    /**
     * 判断当前登录的用户是否有操作这个用户的权限
     */
    private void assertAuth(String userId) throws BusinessException {
        if (isAdmin()) {
            return;
        }
        List<Integer> deptDataScope = getDeptDataScope();
        User user = this.userMapper.selectById(userId);
        Integer deptid = user.getDeptid();
        if (deptDataScope.contains(deptid)) {
            return;
        } else {
            throw new BusinessException(BizExceptionEnum.NO_PERMITION);
        }

    }

    /**
     * 跳转到添加下级工会  0组织
     *
     * @return
     */
    @RequestMapping("/user_add_dept")
    public String userAddDept(Integer pid, Integer deptType, Model model) {
        String url = "user_add_dept";
        Dept dept = new Dept();
        dept.setPid(pid);
        dept.setDeptType(deptType);
        Dept pDept = deptMapper.selectById(pid);
        dept.setPDeptName(pDept.getSimplename());
        dept.setPDeptNo(pDept.getDeptNo());
        dept.setOrgType(pDept.getOrgType() == null ? 0 : pDept.getOrgType());
        if (deptType == 1) {
            url = "user_add_dept2";
            dept.setLevel(pDept.getLevel() + 1);
        } else {
            dept.setLevel(pDept.getLevel() + 1);
        }
        model.addAttribute(dept);
        //TODO 记录日志
//        LogObjectHolder.me().set(dept);

        return PREFIX + url;
    }

    /**
     * 跳转修改部门
     *
     * @param deptType 0：组织  1：部门
     * @param model
     * @return
     */
    @RequestMapping("/user_edit_dept")
    public String userEditDept(Integer deptId, Integer deptType, Model model) {
        Dept dept = deptMapper.selectById(deptId);
        model.addAttribute(dept);
        //TODO 记录日志
//        LogObjectHolder.me().set(dept);
        String url = "user_edit_dept";
        if (deptType == 1) {
            url = "user_edit_dept2";
        }
        return PREFIX + url;
    }

    /**
     * 用户界面部门ztree右键菜单修改信息
     *
     * @param dept
     * @return
     */
    @RequestMapping(value = "/updateDept")
    @ResponseBody
    public Object update(Dept dept) throws BusinessException {
        if (dept.getId() == null) {
            throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
        }
        Dept updateDept = deptMapper.selectById(dept.getId());
        if (updateDept == null) {
            throw new BusinessException(BizExceptionEnum.DB_RESOURCE_NULL);
        }
        updateDept.setSimplename(dept.getSimplename());
        if (dept.getDeptType() == 0) {
            updateDept.setAddr(dept.getAddr());
        }
        deptMapper.updateById(updateDept);
        return super.success();
    }

    /**
     * 获取登陆用户的deptid
     *
     * @return
     * @author liushuwei
     * @date 2017年12月5日-下午6:07:05
     */
    @RequestMapping("getLoginDeptId")
    @ResponseBody
    public Integer getLoginDeptId() {
        Integer deptId = getLoginUser().getDeptId();
        return deptId;
    }

    /**
     * 获取登陆用户的orgId
     *
     * @return
     */
    @RequestMapping("getLoginOrgId")
    @ResponseBody
    public Integer getLoginOrgId() {
        return getLoginUser().getDeptId();
    }

}
