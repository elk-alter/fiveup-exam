package com.elk.exam.service;

import com.elk.exam.dto.LoginDTO;
import com.elk.exam.dto.RegisterDTO;
import com.elk.exam.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elk.exam.vo.UserInfoVo;
import com.elk.exam.vo.UserVo;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
public interface UserService extends IService<User> {

    /**
     * 注册
     * @param registerDTO 注册参数
     * @return 注册成功后的用户信息
     */
    User register(RegisterDTO registerDTO);

    /**
     * 登录接口，登录成功返回token
     *
     * @param loginDTO 登录参数
     * @return 成功返回token，失败返回null
     */
    String login(LoginDTO loginDTO);

    /**
     * 根据用户id获取用户信息
     *
     * @return 用户实体
     */
    UserVo getUserInfo(String userId);

    User getUserById(String id);

    /**
     * 获取用户详细信息(主要是权限相关的)
     * @param userId 用户的id
     * @return 用户信息组装的实体
     */
    UserInfoVo getInfo(String userId);

    /**
     * 获取用户列表
     * @return 用户列表
     */
    List<UserVo> listAll();

    /**
     *
     * @param userVo
     * @return
     */
    User updateUser(UserVo userVo);
}
