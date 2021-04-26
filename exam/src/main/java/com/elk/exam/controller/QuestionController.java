package com.elk.exam.controller;


import com.elk.exam.common.service.RedisService;
import com.elk.exam.service.ExamService;
import com.elk.exam.service.QuestionService;
import com.elk.exam.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 考试题目表 前端控制器
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamService examService;

    @Autowired
    private RedisService redisService;

    @ApiOperation("获取所有问题的列表")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResultVO<List<QuestionVo>> getQuestionList() {
        ResultVO<List<QuestionVo>> resultVO;
        try {
            List<QuestionVo> questionAll = questionService.getQuestionAll();
            resultVO = new ResultVO<>(0, "获取全部问题列表成功", questionAll);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取全部问题列表失败", null);
        }
        return resultVO;
    }

    @ApiOperation("更新问题")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO<QuestionVo> update(@RequestBody QuestionVo questionVo) {
        System.out.println(questionVo);
        try {
            QuestionVo question = questionService.updateQuestion(questionVo);
            return new ResultVO<>(0, "更新问题成功", question);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(-1, "更新问题失败", null);
        }
    }

    @ApiOperation("创建问题")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO<String> questionCreate(@RequestBody QuestionCreateSimplifyVo questionCreateSimplifyVo) {
        QuestionCreateVo questionCreateVo = new QuestionCreateVo();
        // 把能拷贝过来的属性都拷贝过来
        BeanUtils.copyProperties(questionCreateSimplifyVo, questionCreateVo);
        // 设置创建者信息
        String userId = (String) redisService.get("id");
        questionCreateVo.setQuestionCreatorId(userId);
        System.out.println(questionCreateVo);
        try {
            questionService.createQuestion(questionCreateVo);
            return new ResultVO<>(0, "问题创建成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(-1, "创建问题失败", null);
        }
    }

    @ApiOperation("获取问题分类的相关选项")
    @RequestMapping(value = "/selection", method = RequestMethod.GET)
    @ResponseBody
    public ResultVO<QuestionSelectionVo> getSelections() {
        QuestionSelectionVo questionSelectionVo = questionService.getSelections();
        if (questionSelectionVo != null) {
            return new ResultVO<>(0, "获取问题分类选项成功", questionSelectionVo);
        } else {
            return new ResultVO<>(-1, "获取问题分类选项失败", null);
        }
    }

    @ApiOperation("根据问题的id获取问题的详细信息")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultVO<QuestionDetailVo> getQuestionDetail(@PathVariable String id) {
        //  根据问题id获取问题的详细信息
        System.out.println(id);
        try {
            QuestionDetailVo questionDetailVo = questionService.getQuestionDetail(id);
            return new ResultVO<>(0, "获取问题详情成功", questionDetailVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(-1, "获取问题详情失败", null);
        }
    }

    @GetMapping("/type/list")
    @ApiOperation("获取问题列表，按照单选、多选和判断题分类返回")
    public ResultVO<ExamQuestionTypeVo> getExamQuestionTypeList() {
        // 获取问题的分类列表
        ResultVO<ExamQuestionTypeVo> resultVO;
        try {
            ExamQuestionTypeVo examQuestionTypeVo = examService.getExamQuestionType();
            resultVO = new ResultVO<>(0, "获取问题列表成功", examQuestionTypeVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取问题列表失败", null);
        }
        return resultVO;
    }
}

