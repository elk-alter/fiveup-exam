package com.elk.exam.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 题目类别表
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("question_category")
public class QuestionCategory implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 问题类别表的主键
     */
        @TableId(value = "question_category_id", type = IdType.AUTO)
      private Integer questionCategoryId;

      /**
     * 问题类别名称
     */
      private String questionCategoryName;

      /**
     * 问题类别的描述
     */
      private String questionCategoryDescription;


}
