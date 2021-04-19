package com.elk.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.elk.exam.model.QuestionOption;
import com.elk.exam.mapper.QuestionOptionMapper;
import com.elk.exam.service.QuestionOptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 题目的选项 服务实现类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Service
public class QuestionOptionServiceImpl extends ServiceImpl<QuestionOptionMapper, QuestionOption> implements QuestionOptionService {

    @Override
    public List<QuestionOption> listOptionByIds(List<String> ids) {
        List<QuestionOption> questionOptionList = new ArrayList<>();

        for (String id : ids) {
            QueryWrapper<QuestionOption> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(QuestionOption::getQuestionOptionId, id);
            QuestionOption questionOption = getOne(queryWrapper);
            questionOptionList.add(questionOption);
        }

        return questionOptionList;
    }

}
