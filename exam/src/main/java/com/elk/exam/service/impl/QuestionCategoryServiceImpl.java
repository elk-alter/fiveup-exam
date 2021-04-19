package com.elk.exam.service.impl;

import com.elk.exam.model.QuestionCategory;
import com.elk.exam.mapper.QuestionCategoryMapper;
import com.elk.exam.service.QuestionCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 题目类别表 服务实现类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Service
public class QuestionCategoryServiceImpl extends ServiceImpl<QuestionCategoryMapper, QuestionCategory> implements QuestionCategoryService {

}
