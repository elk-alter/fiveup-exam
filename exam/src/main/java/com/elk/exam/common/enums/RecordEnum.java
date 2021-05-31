package com.elk.exam.common.enums;

import lombok.Getter;

@Getter
public enum RecordEnum {

    /**
     * 记录类型
     */
    Excellent(1, "优秀"),
    Good(2, "良好"),
    Normal(3, "一般"),
    Pass(4, "及格"),
    Fail(5, "不及格");


    RecordEnum(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    private Integer id;
    private String type;
}
