package com.elk.exam.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 考试题目表
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("ex_question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 题目的主键
     */
        private String questionId;

      /**
     * 题目的名字
     */
      private String questionName;

      /**
     * 题目的分数
     */
      private Integer questionScore;

      /**
     * 题目创建者的用户id
     */
      private String questionCreatorId;

      /**
     * 题目难易度级别
     */
      private Integer questionLevelId;

      /**
     * 题目的类型，比如单选、多选、判断等
     */
      private Integer questionTypeId;

      /**
     * 题目的类型，比如数学、英语、政治等
     */
      private Integer questionCategoryId;

      /**
     * 题目额外的描述
     */
      private String questionDescription;

      /**
     * 题目的选项，用选项的id用-连在一起表示答案
     */
      private String questionOptionIds;

      /**
     * 题目的答案，用选项的id用-连在一起表示答案
     */
      private String questionAnswerOptionIds;

      /**
     * 创建时间
     */
      private Date createTime;

      /**
     * 更新时间
     */
      private Date updateTime;


}
