package com.elk.exam.controller;


import com.elk.exam.common.service.RedisService;
import com.elk.exam.service.ExamRecordService;
import com.elk.exam.vo.ExamRecordVo;
import com.elk.exam.vo.RecordDetailVo;
import com.elk.exam.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 考试记录表 前端控制器
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@RestController
@RequestMapping("/record")
public class ExamRecordController {

    @Autowired
    private ExamRecordService recordService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/list")
    @ApiOperation("获取当前用户的考试记录")
    ResultVO<List<ExamRecordVo>> getExamRecordList() {
        ResultVO<List<ExamRecordVo>> resultVO;
        try {
            // 拦截器里设置上的用户id
            String userId = (String) redisService.get("id");
            System.out.println("userId = "+ userId);
            // 下面根据用户账号拿到他(她所有的考试信息)，注意要用VO封装下
            List<ExamRecordVo> examRecordVoList = recordService.getExamRecordList(userId);
            resultVO = new ResultVO<>(0, "获取考试记录成功", examRecordVoList);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取考试记录失败", null);
        }
        return resultVO;
    }

    @GetMapping("/detail/{recordId}")
    @ApiOperation("根据考试记录id获取考试记录详情")
    ResultVO<RecordDetailVo> getExamRecordDetail(@PathVariable String recordId) {
        ResultVO<RecordDetailVo> resultVO;
        try {
            RecordDetailVo recordDetailVo = recordService.getRecordDetail(recordId);
            resultVO = new ResultVO<>(0, "获取考试记录详情成功", recordDetailVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取考试记录详情失败", null);
        }
        return resultVO;
    }
}

