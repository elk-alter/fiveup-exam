package com.elk.exam.controller;


import com.elk.exam.common.service.RedisService;
import com.elk.exam.model.Exam;
import com.elk.exam.model.ExamRecord;
import com.elk.exam.service.ExamRecordService;
import com.elk.exam.service.ExamService;
import com.elk.exam.vo.*;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 考试的详细信息表 前端控制器
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamRecordService recordService;

    @Autowired
    private RedisService redisService;

    @GetMapping("/all")
    @ApiOperation("获取全部考试的列表")
    ResultVO<List<ExamVo>> getExamAll() {
        // 需要拼接前端需要的考试列表对象
        ResultVO<List<ExamVo>> resultVO;
        try {
            List<ExamVo> examVos = examService.getExamAll();
            resultVO = new ResultVO<>(0, "获取全部考试的列表成功", examVos);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取全部考试的列表失败", null);
        }
        return resultVO;
    }

    @PostMapping("/create")
    @ApiOperation("创建考试")
    ResultVO<Exam> createExam(@RequestBody ExamCreateVo examCreateVo) {
        // 从前端传参数过来，在这里完成考试的入库
        ResultVO<Exam> resultVO;
        String userId = (String) redisService.get("id");
        try {
            Exam exam = examService.create(examCreateVo, userId);
            resultVO = new ResultVO<>(0, "创建考试成功", exam);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "创建考试失败", null);
        }
        return resultVO;
    }

    @PostMapping("/update")
    @ApiOperation("更新考试")
    ResultVO<Exam> updateExam(@RequestBody ExamVo examVo) {
        // 从前端传参数过来，在这里完成考试的入库
        ResultVO<Exam> resultVO;
        String userId = (String) redisService.get("id");
        try {
            Exam exam = examService.update(examVo, userId);
            resultVO = new ResultVO<>(0, "更新考试成功", exam);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "更新考试失败", null);
        }
        return resultVO;
    }

    @GetMapping("/card/list")
    @ApiOperation("获取考试列表，适配前端卡片列表")
    ResultVO<List<ExamCardVo>> getExamCardList() {
        // 获取考试列表卡片
        ResultVO<List<ExamCardVo>> resultVO;
        try {
            List<ExamCardVo> examCardVoList = examService.getExamCardList();
            resultVO = new ResultVO<>(0, "获取考试列表卡片成功", examCardVoList);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取考试列表卡片失败", null);
        }
        return resultVO;
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("根据考试的id，获取考试详情")
    ResultVO<ExamDetailVo> getExamDetail(@PathVariable String id) {
        // 根据id获取考试详情
        ResultVO<ExamDetailVo> resultVO;
        try {
            ExamDetailVo examDetail = examService.getExamDetail(id);
            resultVO = new ResultVO<>(0, "获取考试详情成功", examDetail);
        } catch (Exception e) {
            resultVO = new ResultVO<>(-1, "获取考试详情失败", null);
        }
        return resultVO;
    }

    @PostMapping("/finish/{examId}")
    @ApiOperation("根据用户提交的答案对指定id的考试判分")
    ResultVO<ExamRecord> finishExam(@PathVariable String examId, @RequestBody HashMap<String, List<String>> answersMap) {
        ResultVO<ExamRecord> resultVO;
        try {
            // 拦截器里设置上的用户id
            String userId = (String) redisService.get("id");
            // 下面根据用户提交的信息进行判分,返回用户的得分情况
            ExamRecord examRecord = recordService.judge(userId, examId, answersMap);
            resultVO = new ResultVO<>(0, "考卷提交成功", examRecord);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "考卷提交失败", null);
        }
        return resultVO;
    }
}

