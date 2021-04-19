package com.elk.exam.service.impl;

import com.elk.exam.model.QuestionType;
import com.elk.exam.mapper.QuestionTypeMapper;
import com.elk.exam.service.QuestionTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 问题类型 服务实现类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Service
public class QuestionTypeServiceImpl extends ServiceImpl<QuestionTypeMapper, QuestionType> implements QuestionTypeService {

}
