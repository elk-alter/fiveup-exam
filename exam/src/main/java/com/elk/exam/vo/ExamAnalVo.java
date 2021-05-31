package com.elk.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExamAnalVo {

    @JsonProperty("type")
    String type;

    @JsonProperty("value")
    Integer nums;
}
