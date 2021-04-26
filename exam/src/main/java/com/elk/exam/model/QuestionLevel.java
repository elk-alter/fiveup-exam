package com.elk.exam.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 问题的难易度级别
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("question_level")
public class QuestionLevel implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 题目难易度的主键
     */
        @TableId(value = "question_level_id", type = IdType.AUTO)
        @JsonProperty("id")
      private Integer questionLevelId;

      /**
     * 题目难易度名称
     */
      @JsonProperty("name")
      private String questionLevelName;

      /**
     * 题目难易度的描述
     */
      @JsonProperty("description")
      private String questionLevelDescription;


}
