package com.elk.exam.service.impl;

import com.elk.exam.model.Role;
import com.elk.exam.mapper.RoleMapper;
import com.elk.exam.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
