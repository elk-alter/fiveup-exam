package com.elk.exam.service.impl;

import com.elk.exam.model.Action;
import com.elk.exam.mapper.ActionMapper;
import com.elk.exam.service.ActionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 前端操作比如增删改查等的权限表 服务实现类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Service
public class ActionServiceImpl extends ServiceImpl<ActionMapper, Action> implements ActionService {

}
