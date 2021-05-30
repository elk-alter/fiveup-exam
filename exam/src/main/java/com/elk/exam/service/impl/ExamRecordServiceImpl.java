package com.elk.exam.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.elk.exam.model.*;
import com.elk.exam.mapper.ExamRecordMapper;
import com.elk.exam.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elk.exam.vo.ExamDetailVo;
import com.elk.exam.vo.ExamRecordVo;
import com.elk.exam.vo.RecordDetailVo;
import com.google.common.collect.Lists;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 考试记录表 服务实现类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Service
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecord> implements ExamRecordService {

    @Autowired
    private ExamService examService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExamRecordLevelService examRecordLevelService;

    @Override
    public ExamRecord judge(String userId, String examId, HashMap<String, List<String>> answersMap) {
        // 开始考试判分啦~~~
        // 1.首先获取考试对象和选项数组
        ExamDetailVo examDetailVo = examService.getExamDetail(examId);
        Exam exam = examDetailVo.getExam();
        // 2.然后获取该考试下所有的题目信息
        List<String> questionIds = new ArrayList<>();
        // 2.1 题目id的数组
        List<String> radioIdList = Arrays.asList(examDetailVo.getRadioIds());
        List<String> checkIdList = Arrays.asList(examDetailVo.getCheckIds());
        List<String> judgeIdList = Arrays.asList(examDetailVo.getJudgeIds());
        questionIds.addAll(radioIdList);
        questionIds.addAll(checkIdList);
        questionIds.addAll(judgeIdList);
        // 2.2 每种题目的分数
        int radioScore = exam.getExamScoreRadio();
        int checkScore = exam.getExamScoreCheck();
        int judgeScore = exam.getExamScoreJudge();
        // 2.3 根据问题id的数组拿到所有的问题对象，供下面步骤用
        List<Question> questionList = questionService.listQuestionByIds(questionIds);
        Map<String, Question> questionMap = new HashMap<>();
        for (Question question : questionList) {
            questionMap.put(question.getQuestionId(), question);
        }
        // 3.根据正确答案和用户作答信息进行判分
        Set<String> questionIdsAnswer = answersMap.keySet();
        // 存储当前考试每个题目的得分情况
        Map<String, Integer> judgeMap = new HashMap<>();
        // 考生作答地每个题目的选项(题目和题目之间用$分隔，题目有多个选项地话用-分隔,题目和选项之间用_分隔),用于查看考试详情
        // 例子：题目1的id_作答选项1-作答选项2&题目2的id_作答选项1&题目3_作答选项1-作答选项2-作答选项3
        StringBuilder answerOptionIdsSb = new StringBuilder();
        // 用户此次考试的总分
        int totalScore = 0;
        for (String questionId : questionIdsAnswer) {
            // 获取用户作答地这个题的答案信息
            Question question = questionMap.get(questionId);
            // 获取答案选项
            String questionAnswerOptionIds = replaceLastSeparator(question.getQuestionAnswerOptionIds());
            List<String> questionAnswerOptionIdList = Arrays.asList(questionAnswerOptionIds.split("-"));
            Collections.sort(questionAnswerOptionIdList);
            String answerStr = listConcat(questionAnswerOptionIdList);
            // 获取用户作答
            List<String> questionUserOptionIdList = answersMap.get(questionId);
            Collections.sort(questionUserOptionIdList);
            String userStr = listConcat(questionUserOptionIdList);
            // 判断questionAnswerOptionIds和answersMap里面的答案是否相等
            if (answerStr.equals(userStr)) {
                // 说明题目作答正确,下面根据题型给分
                int score = 0;
                if (radioIdList.contains(questionId)) {
                    score = radioScore;
                }
                if (checkIdList.contains(questionId)) {
                    score = checkScore;
                }
                if (judgeIdList.contains(questionId)) {
                    score = judgeScore;
                }
                // 累计本次考试得分
                totalScore += score;
                // True代表题目答对
                answerOptionIdsSb.append(questionId + "@True_" + userStr + "$");
                judgeMap.put(questionId, score);
            } else {
                // 说明题目作答错误,直接判零分,False代表题目答错
                answerOptionIdsSb.append(questionId + "@False_" + userStr + "$");
                judgeMap.put(questionId, 0);
            }
        }
        // 4.计算得分，记录本次考试结果，存到ExamRecord中
        ExamRecord examRecord = new ExamRecord();
        examRecord.setExamRecordId(IdUtil.simpleUUID());
        examRecord.setExamId(examId);
        // 注意去掉最后可能有的&_-
        examRecord.setAnswerOptionIds(replaceLastSeparator(answerOptionIdsSb.toString()));
        examRecord.setExamJoinerId(userId);
        examRecord.setExamJoinDate(new Date());
        examRecord.setExamJoinScore(totalScore);
        // 判断不及格和情况
        int examScore = exam.getExamScore();
        double level = totalScore*1.0 / examScore;
        if (level < 0.6) {
            examRecord.setExamResultLevel(5);
        } else if (level < 0.7) {
            examRecord.setExamResultLevel(4);
        } else if (level < 0.8) {
            examRecord.setExamResultLevel(3);
        } else if (level < 0.9) {
            examRecord.setExamResultLevel(2);
        } else if (level < 0.95) {
            examRecord.setExamResultLevel(1);
        }

        save(examRecord);
        return examRecord;
    }

    private String listConcat(List<String> strList) {
        StringBuilder sb = new StringBuilder();
        for (String str : strList) {
            sb.append(str);
            sb.append("-");
        }
        return replaceLastSeparator(sb.toString());
    }

    private String replaceLastSeparator(String str) {
        String lastChar = str.substring(str.length() - 1);
        // 题目和题目之间用$分隔，题目有多个选项地话用-分隔,题目和选项之间用_分隔
        if ("-".equals(lastChar) || "_".equals(lastChar) || "$".equals(lastChar)) {
            str = StrUtil.sub(str, 0, str.length() - 1);
        }
        return str;
    }

    @Override
    public List<ExamRecordVo> getExamRecordList(String userId) {
        // 获取指定用户下的考试记录列表
        List<ExamRecord> examRecordList = findByExamJoinerIdOrderByExamJoinDateDesc(userId);
        List<ExamRecordVo> examRecordVoList = new ArrayList<>();
        for (ExamRecord examRecord : examRecordList) {
            ExamRecordVo examRecordVo = new ExamRecordVo();
            Exam exam = examService.getExamById(examRecord.getExamId());
            examRecordVo.setExam(exam);
            User user = userService.getUserById(userId);
            examRecordVo.setUser(user);
            examRecordVo.setExamRecord(examRecord);
            examRecordVoList.add(examRecordVo);
        }
        return examRecordVoList;
    }

    @Override
    public ExamRecord getRecordById(String id) {
        QueryWrapper<ExamRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExamRecord::getExamRecordId, id);
        return getOne(queryWrapper);
    }

    @Override
    public RecordDetailVo getRecordDetail(String recordId) {
        // 获取考试详情的封装对象
        ExamRecord record = getRecordById(recordId);
        RecordDetailVo recordDetailVo = new RecordDetailVo();
        recordDetailVo.setExamRecord(record);
        // 用户的答案，需要解析
        HashMap<String, List<String>> answersMap = new HashMap<>();
        HashMap<String, String> resultsMap = new HashMap<>();
        assert record != null;
        String answersStr = record.getAnswerOptionIds();
        // $分隔题目,因为$在正则中有特殊用途(行尾)，所以需要括起来
        String[] questionArr = answersStr.split("[$]");
        for (String questionStr : questionArr) {
            System.out.println(questionStr);
            // 区分开题目标题和选项
            String[] questionTitleResultAndOption = questionStr.split("_");
            String[] questionTitleAndResult = questionTitleResultAndOption[0].split("@");
            String[] questionOptions = questionTitleResultAndOption[1].split("-");
            // 题目：答案选项
            answersMap.put(questionTitleAndResult[0], Arrays.asList(questionOptions));
            // 题目：True / False
            resultsMap.put(questionTitleAndResult[0], questionTitleAndResult[1]);
        }
        recordDetailVo.setAnswersMap(answersMap);
        recordDetailVo.setResultsMap(resultsMap);
        // 下面再计算正确答案的map
        ExamDetailVo examDetailVo = examService.getExamDetail(record.getExamId());
        List<String> questionIdList = new ArrayList<>();
        questionIdList.addAll(Arrays.asList(examDetailVo.getRadioIds()));
        questionIdList.addAll(Arrays.asList(examDetailVo.getCheckIds()));
        questionIdList.addAll(Arrays.asList(examDetailVo.getJudgeIds()));
        // 获取所有的问题对象
        List<Question> questionList = questionService.listQuestionByIds(questionIdList);
        HashMap<String, List<String>> answersRightMap = new HashMap<>();
        for (Question question : questionList) {
            // 记得去掉最后可能出现的特殊字符
            String questionAnswerOptionIdsStr = replaceLastSeparator(question.getQuestionAnswerOptionIds());
            String[] questionAnswerOptionIds = questionAnswerOptionIdsStr.split("-");
            answersRightMap.put(question.getQuestionId(), Arrays.asList(questionAnswerOptionIds));
        }
        recordDetailVo.setAnswersRightMap(answersRightMap);
        return recordDetailVo;
    }

    @Override
    public List<ExamRecord> findByExamJoinerIdOrderByExamJoinDateDesc(String userId) {
        QueryWrapper<ExamRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExamRecord::getExamJoinerId, userId).orderByDesc(ExamRecord::getExamJoinDate);
        return list(queryWrapper);
    }

    @Override
    public List<ExamRecordVo> getExamResultList() {
        List<ExamRecord> examRecordList = list();
        List<ExamRecordVo> examRecordVoList = new ArrayList<>();
        for (ExamRecord examRecord : examRecordList) {
            ExamRecordVo examRecordVo = new ExamRecordVo();
            Exam exam = examService.getExamById(examRecord.getExamId());
            examRecordVo.setExam(exam);
            User user = userService.getUserById(examRecord.getExamJoinerId());
            examRecordVo.setUser(user);
            examRecordVo.setExamRecord(examRecord);
            examRecordVoList.add(examRecordVo);
        }
        return examRecordVoList;
    }

    @Override
    public List<ExamRecordVo> getExamRecordListByExam(String examId) {
        QueryWrapper<ExamRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExamRecord::getExamId, examId);
        List<ExamRecord> examRecordList = list(queryWrapper);
        List<ExamRecordVo> examRecordVoList = new ArrayList<>();
        for (ExamRecord examRecord : examRecordList) {
            ExamRecordVo examRecordVo = new ExamRecordVo();
            Exam exam = examService.getExamById(examRecord.getExamId());
            examRecordVo.setExam(exam);
            User user = userService.getUserById(examRecord.getExamJoinerId());
            examRecordVo.setUser(user);
            examRecordVo.setExamRecord(examRecord);
            examRecordVoList.add(examRecordVo);
        }
        return examRecordVoList;
    }

    @Override
    public double getRecordPercentage(String recordId) {
        ExamRecord record = getRecordById(recordId);
        String examId = record.getExamId();
        QueryWrapper<ExamRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExamRecord::getExamId, examId);
        List<ExamRecord> recordList = list(queryWrapper);
        List<Integer> scores = new ArrayList<>();
        for(ExamRecord record1 : recordList) {
            scores.add(record1.getExamJoinScore());
        }
        Collections.sort(scores);
        if (scores.size() == 0) return 0;
        if (scores.size() == 1) return 1;
        int level = 0;
        for (int i = 0; i < scores.size(); i++) {
            if (record.getExamJoinScore().equals(scores.get(i))) {
                level = i;
            }
        }
        System.out.println(scores);
        System.out.println(scores.size() + " " + level);
        return level*1.0 / scores.size();
    }

    @Override
    public String getRecordLevel(int id) {
        ExamRecordLevel level = examRecordLevelService.getById(id);

        return level.getExamRecordLevelDescription();
    }
}
