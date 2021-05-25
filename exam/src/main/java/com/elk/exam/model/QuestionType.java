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
 * 问题类型
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("ex_question_type")
public class QuestionType implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 题目类型表的主键
     */
        @TableId(value = "question_type_id", type = IdType.AUTO)
        @JsonProperty("id")
      private Integer questionTypeId;

      /**
     * 题目类型名称
     */
      @JsonProperty("name")
      private String questionTypeName;

      /**
     * 题目类型的描述
     */
      @JsonProperty("description")
      private String questionTypeDescription;


}
