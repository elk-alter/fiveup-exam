package com.elk.exam.vo;

import com.elk.exam.model.Exam;
import com.elk.exam.model.ExamRecord;
import com.elk.exam.model.User;
import lombok.Data;

@Data
public class ExamRecordVo {
    /**
     * 当前考试记录对应的考试
     */
    private Exam exam;

    /**
     * 当前考试对应的内容
     */
    private ExamRecord examRecord;

    /**
     * 参加考试的用户信息
     */
    private User user;
}
