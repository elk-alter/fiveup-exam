package com.elk.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExamRandomVo {

    @JsonProperty("name")
    private String examName;

    @JsonProperty("avatar")
    private String examAvatar;

    @JsonProperty("desc")
    private String examDescription;

    @JsonProperty("categories")
    private List<Integer> categoryList;

    /**
     * 考试时长，单位分钟
     */
    @JsonProperty("elapse")
    private Integer examTimeLimit;

}
