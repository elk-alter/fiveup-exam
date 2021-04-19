package com.elk.exam.service.impl;

import com.elk.exam.model.Page;
import com.elk.exam.mapper.PageMapper;
import com.elk.exam.service.PageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 前端页面表 服务实现类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

}
