package com.elk.exam.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.elk.exam.common.enums.QuestionEnum;
import com.elk.exam.model.Exam;
import com.elk.exam.mapper.ExamMapper;
import com.elk.exam.model.Question;
import com.elk.exam.service.ExamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elk.exam.service.QuestionService;
import com.elk.exam.service.UserService;
import com.elk.exam.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 考试的详细信息表 服务实现类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Override
    public List<ExamVo> getExamAll() {
        List<Exam> examList = list();
        return getExamVos(examList);
    }

    private List<ExamVo> getExamVos(List<Exam> examList) {
        // 需要自定义的exam列表
        List<ExamVo> examVoList = new ArrayList<>();
        // 循环完成每个属性的定制
        for (Exam exam : examList) {
            ExamVo examVo = new ExamVo();
            // 先尽量复制能复制的所有属性
            BeanUtils.copyProperties(exam, examVo);
            // 设置问题的创建者
            examVo.setExamCreator(
                    Objects.requireNonNull(
                            userService.getUserById(
                                    exam.getExamCreatorId()
                            )
                    ).getUserUsername()
            );

            // 获取所有单选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoRadioList上
            List<ExamQuestionSelectVo> radioQuestionVoList = new ArrayList<>();
            List<Question> radioQuestionList = questionService.listQuestionByIds(
                    Arrays.asList(exam.getExamQuestionIdsRadio().split("-"))
            );
            for (Question question : radioQuestionList) {
                ExamQuestionSelectVo radioQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, radioQuestionVo);
                radioQuestionVo.setChecked(true); // 考试中的问题肯定被选中的
                radioQuestionVoList.add(radioQuestionVo);
            }
            examVo.setExamQuestionSelectVoRadioList(radioQuestionVoList);

            // 获取所有多选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoCheckList上
            List<ExamQuestionSelectVo> checkQuestionVoList = new ArrayList<>();
            List<Question> checkQuestionList = questionService.listQuestionByIds(
                    Arrays.asList(exam.getExamQuestionIdsCheck().split("-"))
            );
            for (Question question : checkQuestionList) {
                ExamQuestionSelectVo checkQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, checkQuestionVo);
                checkQuestionVo.setChecked(true); // 考试中的问题肯定被选中的
                checkQuestionVoList.add(checkQuestionVo);
            }
            examVo.setExamQuestionSelectVoCheckList(checkQuestionVoList);

            // 获取所有多选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoJudgeList上
            List<ExamQuestionSelectVo> judgeQuestionVoList = new ArrayList<>();
            List<Question> judgeQuestionList = questionService.listQuestionByIds(
                    Arrays.asList(exam.getExamQuestionIdsJudge().split("-"))
            );
            for (Question question : judgeQuestionList) {
                ExamQuestionSelectVo judgeQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, judgeQuestionVo);
                judgeQuestionVo.setChecked(true); // 考试中的问题肯定被选中的
                judgeQuestionVoList.add(judgeQuestionVo);
            }
            examVo.setExamQuestionSelectVoJudgeList(judgeQuestionVoList);

            // 把examVo加到examVoList中
            examVoList.add(examVo);
        }
        return examVoList;
    }

    @Override
    public ExamQuestionTypeVo getExamQuestionType() {
        ExamQuestionTypeVo examQuestionTypeVo = new ExamQuestionTypeVo();
        // 获取所有单选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoRadioList上
        List<ExamQuestionSelectVo> radioQuestionVoList = new ArrayList<>();
        QueryWrapper<Question> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(Question::getQuestionTypeId, QuestionEnum.RADIO.getId());
        List<Question> radioQuestionList = questionService.list(queryWrapper1);
        for (Question question : radioQuestionList) {
            ExamQuestionSelectVo radioQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, radioQuestionVo);
            radioQuestionVoList.add(radioQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoRadioList(radioQuestionVoList);

        // 获取所有多选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoCheckList上
        List<ExamQuestionSelectVo> checkQuestionVoList = new ArrayList<>();
        QueryWrapper<Question> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.lambda().eq(Question::getQuestionTypeId, QuestionEnum.CHECK.getId());
        List<Question> checkQuestionList = questionService.list(queryWrapper2);
        for (Question question : checkQuestionList) {
            ExamQuestionSelectVo checkQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, checkQuestionVo);
            checkQuestionVoList.add(checkQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoCheckList(checkQuestionVoList);

        // 获取所有多选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoJudgeList上
        List<ExamQuestionSelectVo> judgeQuestionVoList = new ArrayList<>();
        QueryWrapper<Question> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.lambda().eq(Question::getQuestionTypeId, QuestionEnum.JUDGE.getId());
        List<Question> judgeQuestionList = questionService.list(queryWrapper3);
        for (Question question : judgeQuestionList) {
            ExamQuestionSelectVo judgeQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, judgeQuestionVo);
            judgeQuestionVoList.add(judgeQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoJudgeList(judgeQuestionVoList);
        return examQuestionTypeVo;
    }

    @Override
    public Exam getExamById(String id) {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Exam::getExamId, id);
        return getOne(queryWrapper);
    }

    @Override
    public Exam create(ExamCreateVo examCreateVo, String userId) {
        // 在线考试系统创建
        Exam exam = new Exam();
        BeanUtils.copyProperties(examCreateVo, exam);
        exam.setExamId(IdUtil.simpleUUID());
        exam.setExamCreatorId(userId);
        exam.setCreateTime(new Date());
        exam.setUpdateTime(new Date());
        // Todo:这两个日志后面是要在前端传入的，这里暂时定为当前日期
        exam.setExamStartDate(new Date());
        exam.setExamEndDate(new Date());
        String radioIdsStr = "";
        String checkIdsStr = "";
        String judgeIdsStr = "";
        System.out.println("examCreateVo.getChecks():" + examCreateVo.getChecks());
        List<ExamQuestionSelectVo> radios = examCreateVo.getRadios();
        List<ExamQuestionSelectVo> checks = examCreateVo.getChecks();
        List<ExamQuestionSelectVo> judges = examCreateVo.getJudges();
        int radioCnt = 0, checkCnt = 0, judgeCnt = 0;
        for (ExamQuestionSelectVo radio : radios) {
            if (radio.getChecked()) {
                radioIdsStr += radio.getQuestionId() + "-";
                radioCnt++;
            }
        }
        System.out.println("radioIdsStr:"+radioIdsStr);
        radioIdsStr = replaceLastSeparator(radioIdsStr);
        for (ExamQuestionSelectVo check : checks) {
            if (check.getChecked()) {
                checkIdsStr += check.getQuestionId() + "-";
                checkCnt++;
            }
        }
        checkIdsStr = replaceLastSeparator(checkIdsStr);
        for (ExamQuestionSelectVo judge : judges) {
            if (judge.getChecked()) {
                judgeIdsStr += judge.getQuestionId() + "-";
                judgeCnt++;
            }
        }
        judgeIdsStr = replaceLastSeparator(judgeIdsStr);
        exam.setExamQuestionIds(radioIdsStr + "-" + checkIdsStr + "-" + judgeIdsStr);
        // 设置各个题目的id
        exam.setExamQuestionIdsRadio(radioIdsStr);
        exam.setExamQuestionIdsCheck(checkIdsStr);
        exam.setExamQuestionIdsJudge(judgeIdsStr);

        // 计算总分数
        int examScore = radioCnt * exam.getExamScoreRadio() + checkCnt * exam.getExamScoreCheck() + judgeCnt * exam.getExamScoreJudge();
        exam.setExamScore(examScore);
        save(exam);
        return exam;
    }

    private String replaceLastSeparator(String str) {
        if (str.length() < 2) return str;
        String lastChar = str.substring(str.length() - 1);
        // 题目和题目之间用$分隔，题目有多个选项地话用-分隔,题目和选项之间用_分隔
        if ("-".equals(lastChar) || "_".equals(lastChar) || "$".equals(lastChar)) {
            str = StrUtil.sub(str, 0, str.length() - 1);
        }
        return str;
    }

    @Override
    public Exam update(ExamVo examVo, String userId) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examVo, exam);
        exam.setExamCreatorId(userId); // 考试的更新人为最新的创建人
        exam.setUpdateTime(new Date()); // 考试的更新日期要记录下

        String radioIdsStr = "";
        String checkIdsStr = "";
        String judgeIdsStr = "";
        List<ExamQuestionSelectVo> radios = examVo.getExamQuestionSelectVoRadioList();
        List<ExamQuestionSelectVo> checks = examVo.getExamQuestionSelectVoCheckList();
        List<ExamQuestionSelectVo> judges = examVo.getExamQuestionSelectVoJudgeList();
        int radioCnt = 0, checkCnt = 0, judgeCnt = 0;
        for (ExamQuestionSelectVo radio : radios) {
            if (radio.getChecked()) {
                radioIdsStr += radio.getQuestionId() + "-";
                radioCnt++;
            }
        }
        radioIdsStr = replaceLastSeparator(radioIdsStr);
        for (ExamQuestionSelectVo check : checks) {
            if (check.getChecked()) {
                checkIdsStr += check.getQuestionId() + "-";
                checkCnt++;
            }
        }
        checkIdsStr = replaceLastSeparator(checkIdsStr);
        for (ExamQuestionSelectVo judge : judges) {
            if (judge.getChecked()) {
                judgeIdsStr += judge.getQuestionId() + "-";
                judgeCnt++;
            }
        }
        judgeIdsStr = replaceLastSeparator(judgeIdsStr);
        exam.setExamQuestionIds(radioIdsStr + "-" + checkIdsStr + "-" + judgeIdsStr);
        // 设置各个题目的id
        exam.setExamQuestionIdsRadio(radioIdsStr);
        exam.setExamQuestionIdsCheck(checkIdsStr);
        exam.setExamQuestionIdsJudge(judgeIdsStr);

        // 计算总分数
        int examScore = radioCnt * exam.getExamScoreRadio() + checkCnt * exam.getExamScoreCheck() + judgeCnt * exam.getExamScoreJudge();
        exam.setExamScore(examScore);
        save(exam);
        return exam;
    }

    @Override
    public List<ExamCardVo> getExamCardList() {
        List<Exam> examList = list();
        List<ExamCardVo> examCardVoList = new ArrayList<>();
        for (Exam exam : examList) {
            ExamCardVo examCardVo = new ExamCardVo();
            BeanUtils.copyProperties(exam, examCardVo);
            examCardVoList.add(examCardVo);
        }
        return examCardVoList;
    }

    @Override
    public ExamDetailVo getExamDetail(String id) {
        Exam exam = getExamById(id);
        ExamDetailVo examDetailVo = new ExamDetailVo();
        examDetailVo.setExam(exam);
        assert exam != null;
        examDetailVo.setRadioIds(exam.getExamQuestionIdsRadio().split("-"));
        examDetailVo.setCheckIds(exam.getExamQuestionIdsCheck().split("-"));
        examDetailVo.setJudgeIds(exam.getExamQuestionIdsJudge().split("-"));
        return examDetailVo;
    }
}
