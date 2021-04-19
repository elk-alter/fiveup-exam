package com.elk.exam.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 考试结果的等级
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("exam_record_level")
public class ExamRecordLevel implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 考试结果等级表的主键
     */
        @TableId(value = "exam_record_level_id", type = IdType.AUTO)
      private Integer examRecordLevelId;

      /**
     * 考试结果等级的名称
     */
      private String examRecordLevelName;

      /**
     * 考试结果等级的详细阐述
     */
      private String examRecordLevelDescription;


}
