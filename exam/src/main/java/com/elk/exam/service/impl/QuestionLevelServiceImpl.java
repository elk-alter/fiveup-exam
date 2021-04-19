package com.elk.exam.service.impl;

import com.elk.exam.model.QuestionLevel;
import com.elk.exam.mapper.QuestionLevelMapper;
import com.elk.exam.service.QuestionLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 问题的难易度级别 服务实现类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Service
public class QuestionLevelServiceImpl extends ServiceImpl<QuestionLevelMapper, QuestionLevel> implements QuestionLevelService {

}
