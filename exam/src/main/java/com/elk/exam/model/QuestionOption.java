package com.elk.exam.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 题目的选项
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("ex_question_option")
public class QuestionOption implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 题目选项表的主键
     */
        private String questionOptionId;

      /**
     * 选项的内容
     */
      private String questionOptionContent;

      /**
     * 选项的额外描述，可以用于题目答案解析
     */
      private String questionOptionDescription;


}
