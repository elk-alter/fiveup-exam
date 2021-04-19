package com.elk.exam.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elk.exam.common.enums.LoginTypeEnum;
import com.elk.exam.common.enums.RoleEnum;
import com.elk.exam.common.utils.JwtUtils;
import com.elk.exam.dto.LoginDTO;
import com.elk.exam.dto.RegisterDTO;
import com.elk.exam.mapper.UserMapper;
import com.elk.exam.model.Action;
import com.elk.exam.model.Page;
import com.elk.exam.model.Role;
import com.elk.exam.model.User;
import com.elk.exam.service.ActionService;
import com.elk.exam.service.PageService;
import com.elk.exam.service.RoleService;
import com.elk.exam.service.UserService;
import com.elk.exam.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PageService pageService;

    @Autowired
    private ActionService actionService;

    @Value("${user.default.avatar}")
    private String defaultAvatar;

    @Value("${user.default.username}")
    private String defaultUsername;

    @Override
    public User register(RegisterDTO registerDTO) {
        try {
            User user = new User();
            user.setUserId(IdUtil.simpleUUID());
            // 好像还缺少个用户名,用"exam_user_手机号"来注册：需要校验唯一性数据字段已经设置unique了，失败会异常地
            user.setUserUsername(defaultUsername + "_" + registerDTO.getMobile());
            // 初始化昵称和用户名相同
            user.setUserNickname(user.getUserUsername());
            // 这里还需要进行加密处理，后续解密用Base64.decode()
            user.setUserPassword(Base64.encode(registerDTO.getPassword()));
            // 默认设置为学生身份，需要老师和学生身份地话需要管理员修改
            user.setUserRoleId(RoleEnum.STUDENT.getId());
            // 设置头像图片地址, 先默认一个地址，后面用户可以自己再改
            user.setUserAvatar(defaultAvatar);
            // 设置描述信息，随便设置段默认的
            user.setUserDescription("welcome to online exam system");
            // 需要验证这个邮箱是不是已经存在：数据字段已经设置unique了，失败会异常地
            user.setUserEmail(registerDTO.getEmail());
            // 需要验证手机号是否已经存在：数据字段已经设置unique了，失败会异常地
            user.setUserPhone(registerDTO.getMobile());
            save(user);
            System.out.println(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace(); // 用户已经存在
            // 出异常，返回null，表示注册失败
            return null;
        }
    }

    @Override
    public String login(LoginDTO loginDTO) {
        User user;
        if (LoginTypeEnum.USERNAME.getType().equals(loginDTO.getLoginType())) {
            // 登陆者用地是用户名
            user = getUserById(loginDTO.getUserInfo());
        } else {
            // 登陆者用地是邮箱
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getUserEmail, loginDTO.getUserInfo());
            user = getOne(queryWrapper);
        }

        System.out.println("测试登录"+user);
        if (user != null) {
            // 如果user不是null即能找到，才能验证用户名和密码
            // 数据库存的密码
            String passwordDb = Base64.decodeStr(user.getUserPassword());
            // 用户请求参数中的密码
            String passwordQo = loginDTO.getPassword();
            System.out.println(passwordDb);
            System.out.println(passwordQo);
            if (passwordQo.equals(passwordDb)) {
                // 如果密码相等地话说明认证成功,返回生成的token，有效期为一天
                return JwtUtils.genJsonWebToken(user);
            }
        }
        return null;
    }

    @Override
    public UserVo getUserInfo(String userId) {
        User user = getUserById(userId);
        UserVo userVo = new UserVo();
        assert user != null;
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    public User getUserById(String id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUserId, id);
        return getOne(queryWrapper);
    }

    @Override
    public UserInfoVo getInfo(String userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUserId, userId);
        User user = getOne(queryWrapper);
        UserInfoVo userInfoVo = new UserInfoVo();

        if (user != null) {
            // 1.尽可能的拷贝属性
            BeanUtils.copyProperties(user, userInfoVo);
            Integer roleId = user.getUserRoleId();
            Role role = roleService.getById(roleId);

            if (role != null) {
                String roleName = role.getRoleName();

                // 2.设置角色名称
                userInfoVo.setRoleName(roleName);

                // 3.设置当前用户的角色细节
                RoleVo roleVo = new RoleVo();
                BeanUtils.copyProperties(role, roleVo);

                // 4.设置角色的可访问页面
                String rolePageIds = role.getRolePageIds();
                String[] pageIdArr = rolePageIds.split("-");
                List<PageVo> pageVoList = new ArrayList<>();
                for (String pageIdStr : pageIdArr) {
                    // 获取页面的id
                    Integer pageId = Integer.parseInt(pageIdStr);

                    // 4.1 向Role中添加Page
                    Page page = pageService.getById(pageId);
                    if (page != null) {
                        PageVo pageVo = new PageVo();
                        BeanUtils.copyProperties(page, pageVo);

                        // 4.2 向Page中添加action
                        List<ActionVo> actionVoList = new ArrayList<>();
                        String actionIdsStr = page.getActionIds();
                        String[] actionIdArr = actionIdsStr.split("-");
                        for (String actionIdStr : actionIdArr) {
                            Integer actionId = Integer.parseInt(actionIdStr);
                            Action action = actionService.getById(actionId);

                            if (action != null) {
                                ActionVo actionVo = new ActionVo();
                                BeanUtils.copyProperties(action, actionVo);
                                actionVoList.add(actionVo);
                            }
                        }
                        // 设置actionVoList到pageVo中，然后把pageVo加到pageVoList中
                        pageVo.setActionVoList(actionVoList);
                        // 设置pageVoList，下面再设置到RoleVo中
                        pageVoList.add(pageVo);
                    }
                }
                // 设置PageVo的集合到RoleVo中
                roleVo.setPageVoList(pageVoList);
                // 最终把PageVo设置到UserInfoVo中，这样就完成了拼接
                userInfoVo.setRoleVo(roleVo);
                return userInfoVo;
            }
        }

        return userInfoVo;
    }
}
