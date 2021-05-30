package com.elk.exam.service;

import com.elk.exam.model.ExamRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elk.exam.vo.ExamRecordVo;
import com.elk.exam.vo.RecordDetailVo;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 考试记录表 服务类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
public interface ExamRecordService extends IService<ExamRecord> {

    /**
     * 根据用户提交的作答信息进行判分
     *
     * @param userId     考试人
     * @param examId     参与的考试
     * @param answersMap 作答情况
     * @return 本次考试记录
     */
    ExamRecord judge(String userId, String examId, HashMap<String, List<String>> answersMap);

    /**
     * 根据用户id获取此用户的所有考试信息
     *
     * @param userId 用户id
     * @return 该用户的所有考试记录
     */
    List<ExamRecordVo> getExamRecordList(String userId);

    ExamRecord getRecordById(String id);

    /**
     * 获取指定某次考试记录的详情
     *
     * @param recordId 考试记录的id
     * @return 考试详情
     */
    RecordDetailVo getRecordDetail(String recordId);

    /**
     * 获取指定用户参加过的所有考试
     *
     * @param userId 用户id
     * @return 用户参加过的所有考试
     */
    List<ExamRecord> findByExamJoinerIdOrderByExamJoinDateDesc(String userId);

    /**
     * 所有考试结果
     * @return 所有考试结果
     */
    List<ExamRecordVo> getExamResultList();

    /**
     * 根据考试返回记录
     * @param examId 考试id
     * @return 记录
     */
    List<ExamRecordVo> getExamRecordListByExam(String examId);

    /**
     * 获得百分比
     * @param recordId 记录
     * @return 百分比
     */
    double getRecordPercentage(String recordId);

    /**
     * 获得等级
     * @param id 记录
     * @return 百分比
     */
    String getRecordLevel(int id);
}
