package com.elk.exam.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.elk.exam.model.Question;
import com.elk.exam.mapper.QuestionMapper;
import com.elk.exam.model.QuestionOption;
import com.elk.exam.model.User;
import com.elk.exam.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elk.exam.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 考试题目表 服务实现类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private QuestionOptionService questionOptionService;
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionLevelService questionLevelService;
    @Autowired
    private QuestionTypeService questionTypeService;
    @Autowired
    private QuestionCategoryService questionCategoryService;

    @Override
    public List<QuestionVo> getQuestionAll() {
        List<Question> questionList = list();
        return getQuestionVos(questionList);
    }

    private List<QuestionVo> getQuestionVos(List<Question> questionList) {
        // 需要自定义的question列表
        List<QuestionVo> questionVoList = new ArrayList<>();
        // 循环完成每个属性的定制
        for (Question question : questionList) {
            QuestionVo questionVo = getQuestionVo(question);
            questionVoList.add(questionVo);
        }
        return questionVoList;
    }

    @Override
    public QuestionVo updateQuestion(QuestionVo questionVo) {
        // 1.把需要的属性都设置好
        StringBuilder questionAnswerOptionIds = new StringBuilder();
        List<QuestionOption> questionOptionList = new ArrayList<>();
        List<QuestionOptionVo> questionOptionVoList = questionVo.getQuestionOptionVoList();
        int size = questionOptionVoList.size();
        for (int i = 0; i < questionOptionVoList.size(); i++) {
            QuestionOptionVo questionOptionVo = questionOptionVoList.get(i);
            QuestionOption questionOption = new QuestionOption();
            BeanUtils.copyProperties(questionOptionVo, questionOption);
            questionOptionList.add(questionOption);
            if (questionOptionVo.getAnswer()) {
                if (i != size - 1) {
                    // 把更新后的答案的id加上去,记得用-连到一起
                    questionAnswerOptionIds.append(questionOptionVo.getQuestionOptionId()).append("-");
                } else {
                    // 最后一个不需要用-连接
                    questionAnswerOptionIds.append(questionOptionVo.getQuestionOptionId());
                }
            }
        }

        // 1.更新问题
        Question question = getQuestionById(questionVo.getQuestionId());
        BeanUtils.copyProperties(questionVo, question);
        question.setQuestionAnswerOptionIds(questionAnswerOptionIds.toString());
        updateById(question);

        // 2.更新所有的option
        questionOptionService.saveBatch(questionOptionList);

        // 返回更新后的问题，方便前端局部刷新
        return getQuestionVo(question);
    }

    @Override
    public void createQuestion(QuestionCreateVo questionCreateVo) {
        // 问题创建
        Question question = new Question();
        // 把能复制的属性都复制过来
        BeanUtils.copyProperties(questionCreateVo, question);
        // 设置下questionOptionIds和questionAnswerOptionIds，需要自己用Hutool生成下
        List<QuestionOption> questionOptionList = new ArrayList<>();
        List<QuestionOptionCreateVo> questionOptionCreateVoList = questionCreateVo.getQuestionOptionCreateVoList();
        for (QuestionOptionCreateVo questionOptionCreateVo : questionOptionCreateVoList) {
            QuestionOption questionOption = new QuestionOption();
            // 设置选项的的内容
            questionOption.setQuestionOptionContent(questionOptionCreateVo.getQuestionOptionContent());
            // 设置选项的id
            questionOption.setQuestionOptionId(IdUtil.simpleUUID());
            questionOptionList.add(questionOption);
        }
        // 把选项都存起来，然后才能用于下面设置Question的questionOptionIds和questionAnswerOptionIds
        questionOptionService.saveBatch(questionOptionList);
        String questionOptionIds = "";
        String questionAnswerOptionIds = "";
        // 经过上面的saveAll方法，所有的option的主键id都已经持久化了
        for (int i = 0; i < questionOptionCreateVoList.size(); i++) {
            // 获取指定选项
            QuestionOptionCreateVo questionOptionCreateVo = questionOptionCreateVoList.get(i);
            // 获取保存后的指定对象
            QuestionOption questionOption = questionOptionList.get(i);
            questionOptionIds += questionOption.getQuestionOptionId() + "-";
            if (questionOptionCreateVo.getAnswer()) {
                // 如果是答案的话
                questionAnswerOptionIds += questionOption.getQuestionOptionId() + "-";
            }
        }
        // 把字符串最后面的"-"给去掉
        questionAnswerOptionIds = replaceLastSeparator(questionAnswerOptionIds);
        questionOptionIds = replaceLastSeparator(questionOptionIds);
        // 设置选项id组成的字符串
        question.setQuestionOptionIds(questionOptionIds);
        // 设置答案选项id组成的字符串
        question.setQuestionAnswerOptionIds(questionAnswerOptionIds);
        // 自己生成问题的id
        question.setQuestionId(IdUtil.simpleUUID());
        // 先把创建时间和更新时间每次都取当前时间吧
        question.setCreateTime(new Date());
        question.setUpdateTime(new Date());
        // 保存问题到数据库
        save(question);
    }

    @Override
    public QuestionSelectionVo getSelections() {
        QuestionSelectionVo questionSelectionVo = new QuestionSelectionVo();
        questionSelectionVo.setQuestionCategoryList(questionCategoryService.list());
        questionSelectionVo.setQuestionLevelList(questionLevelService.list());
        questionSelectionVo.setQuestionTypeList(questionTypeService.list());

        return questionSelectionVo;
    }

    @Override
    public Question getQuestionById(String id) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Question::getQuestionId, id);
        return getOne(queryWrapper);
    }

    @Override
    public QuestionDetailVo getQuestionDetail(String id) {
        Question question = getQuestionById(id);
        QuestionDetailVo questionDetailVo = new QuestionDetailVo();
        questionDetailVo.setId(id);
        questionDetailVo.setName(question.getQuestionName());
        questionDetailVo.setDescription(question.getQuestionDescription());
        // 问题类型，单选题/多选题/判断题
        questionDetailVo.setType(
                Objects.requireNonNull(
                        questionTypeService.getById(
                                question.getQuestionTypeId()
                        )
                ).getQuestionTypeDescription()
        );
        // 获取当前问题的选项
        String optionIdsStr = trimMiddleLine(question.getQuestionOptionIds());
        String[] optionIds = optionIdsStr.split("-");
        // 获取选项列表
        List<QuestionOption> optionList = questionOptionService.listOptionByIds(Arrays.asList(optionIds));
        questionDetailVo.setOptions(optionList);
        return questionDetailVo;
    }

    @Override
    public List<Question> listQuestionByIds(List<String> ids) {
        List<Question> questionList = new ArrayList<>();

        for (String id : ids) {
            QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Question::getQuestionId, id);
            Question question = getOne(queryWrapper);
            questionList.add(question);
        }
        return questionList;
    }

    /**
     * 去除字符串最后的，防止split的时候出错
     *
     * @param str 原始字符串
     * @return
     */
    private String trimMiddleLine(String str) {
        if (str.charAt(str.length() - 1) == '-') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 把字符串最后一个字符-替换掉
     *
     * @param str 原始字符串
     * @return 替换掉最后一个-的字符串
     */
    private String replaceLastSeparator(String str) {
        String lastChar = str.substring(str.length() - 1);
        // 题目和题目之间用$分隔，题目有多个选项地话用-分隔,题目和选项之间用_分隔
        if ("-".equals(lastChar) || "_".equals(lastChar) || "$".equals(lastChar)) {
            str = StrUtil.sub(str, 0, str.length() - 1);
        }
        return str;
    }

    private QuestionVo getQuestionVo(Question question) {
        QuestionVo questionVo = new QuestionVo();
        // 先复制能复制的属性
        BeanUtils.copyProperties(question, questionVo);
        // 设置问题的创建者
        questionVo.setQuestionCreator(
                Objects.requireNonNull(
                        userService.getUserById(
                                question.getQuestionCreatorId()
                        )
                ).getUserUsername());

        // 设置问题的难度
        questionVo.setQuestionLevel(
                Objects.requireNonNull(
                        questionLevelService.getById(
                                question.getQuestionLevelId()
                        )
                ).getQuestionLevelDescription());

        // 设置题目的类别，比如单选、多选、判断等
        questionVo.setQuestionType(
                Objects.requireNonNull(
                        questionTypeService.getById(
                                question.getQuestionTypeId()
                        )
                ).getQuestionTypeDescription());

        // 设置题目分类，比如数学、语文、英语、生活、人文等
        questionVo.setQuestionCategory(
                Objects.requireNonNull(
                        questionCategoryService.getById(
                                question.getQuestionCategoryId()
                        )
                ).getQuestionCategoryName()
        );

        // 选项的自定义Vo列表
        List<QuestionOptionVo> optionVoList = new ArrayList<>();

        // 获得所有的选项列表
        List<QuestionOption> optionList = questionOptionService.listOptionByIds(
                Arrays.asList(question.getQuestionOptionIds().split("-"))
        );

        // 获取所有的答案列表optionList中每个option的isAnswer选项
        List<QuestionOption> answerList = questionOptionService.listOptionByIds(
                Arrays.asList(question.getQuestionAnswerOptionIds().split("-"))
        );

        // 根据选项和答案的id相同设置optionVo的isAnswer属性
        for (QuestionOption option : optionList) {
            QuestionOptionVo optionVo = new QuestionOptionVo();
            BeanUtils.copyProperties(option, optionVo);
            for (QuestionOption answer : answerList) {
                if (option.getQuestionOptionId().equals(answer.getQuestionOptionId())) {
                    optionVo.setAnswer(true);
                }
            }
            optionVoList.add(optionVo);
        }

        // 设置题目的所有选项
        questionVo.setQuestionOptionVoList(optionVoList);
        return questionVo;
    }
}
