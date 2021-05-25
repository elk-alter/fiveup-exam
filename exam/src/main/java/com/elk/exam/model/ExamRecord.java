package com.elk.exam.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 考试记录表
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("ex_exam_record")
public class ExamRecord implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 考试记录表的主键
     */
        private String examRecordId;

      /**
     * 考试参与者的用户id
     */
      private String examJoinerId;

      /**
     * 参加考试的时间
     */
      private Date examJoinDate;

      /**
     * 完成考试所用的时间,单位分钟
     */
      private Integer examTimeCost;

      /**
     * 参与考试的实际得分
     */
      private Integer examJoinScore;

      /**
     * 考试结果的等级
     */
      private Integer examResultLevel;

    private String answerOptionIds;

    private String examId;


}
