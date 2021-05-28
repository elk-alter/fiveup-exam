package com.elk.exam.service;

import com.elk.exam.model.Exam;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elk.exam.vo.*;

import java.util.List;

/**
 * <p>
 * 考试的详细信息表 服务类
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
public interface ExamService extends IService<Exam> {

    /**
     * 获取全部考试的列表
     */
    List<ExamVo> getExamAll();

    /**
     * 获取所有问题的下拉列表，方便前端创建考试时筛选
     *
     * @return 适配前端的问题下拉列表
     */
    ExamQuestionTypeVo getExamQuestionType();

    Exam getExamById(String id);

    /**
     * 根据前端组装的参数进行考试创建
     *
     * @param examCreateVo 前端组装的考试对象
     * @param userId       用户id
     * @return 创建好的考试
     */
    Exam create(ExamCreateVo examCreateVo, String userId);

    /**
     * 根据前端组装的参数进行考试创建 随机问题
     * @param examRandomVo 考试对象
     * @param userId 用户id
     * @return 随机的考试
     */
    Exam createRandom(ExamRandomVo examRandomVo, String userId);

    /**
     * 更新考试
     *
     * @param examVo 获取所有考试的接口中返回的考试信息结构
     * @param userId 当前的用户
     * @return 更新后的考试详情
     */
    Exam update(ExamVo examVo, String userId);

    /**
     * 获取考试卡片列表
     *
     * @return 考试卡片列表
     */
    List<ExamCardVo> getExamCardList();

    /**
     * 根据考试的id获取考试的详情
     *
     * @param id exam表的主键
     * @return 考试详情的封装的VO对象
     */
    ExamDetailVo getExamDetail(String id);
}
