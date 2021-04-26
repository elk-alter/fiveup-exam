package com.elk.exam.service;

import com.elk.exam.model.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elk.exam.vo.QuestionCreateVo;
import com.elk.exam.vo.QuestionDetailVo;
import com.elk.exam.vo.QuestionSelectionVo;
import com.elk.exam.vo.QuestionVo;

import java.util.List;

/**
 * <p>
 * 考试题目表 服务类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
public interface QuestionService extends IService<Question> {

    /**
     * 获取所有的问题列表
     * @return 问题列表
     */
    List<QuestionVo> getQuestionAll();

    /**
     * 根据前端传过来的问题实体更新问题和选项
     * @param questionVo 问题实体
     * @return 是否成功
     */
    QuestionVo updateQuestion(QuestionVo questionVo);

    /**
     * 问题创建
     * @param questionCreateVo 问题创建实体类
     */
    void createQuestion(QuestionCreateVo questionCreateVo);

    /**
     * 获取问题的选项、分类和难度的下拉列表
     *
     * @return 选项、分类和难度的封装对象
     */
    QuestionSelectionVo getSelections();

    Question getQuestionById(String id);

    /**
     * 获取问题详情
     *
     * @param id 问题的id
     * @return 问题详情的封装VO
     */
    QuestionDetailVo getQuestionDetail(String id);

    List<Question> listQuestionByIds(List<String> asList);

    void updateQuestionById(Question question);

    void insertQuestionById(Question question);

}
