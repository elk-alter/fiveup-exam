package com.elk.exam.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.elk.exam.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 考试题目表 Mapper 接口
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
public interface QuestionMapper extends BaseMapper<Question> {

    @Select("select * from ex_question ${ew.customSqlSegment} ORDER BY RAND() LIMIT 0,${num}")
    List<Question> getRandomList(@Param(Constants.WRAPPER) Wrapper wrapper, @Param("num") int num);
}
