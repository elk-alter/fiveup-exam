package com.elk.exam.service;

import com.elk.exam.model.QuestionOption;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 题目的选项 服务类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
public interface QuestionOptionService extends IService<QuestionOption> {

    List<QuestionOption> listOptionByIds(List<String> ids);

    boolean updateOptionByBatch(List<QuestionOption> questionOptions);

}
