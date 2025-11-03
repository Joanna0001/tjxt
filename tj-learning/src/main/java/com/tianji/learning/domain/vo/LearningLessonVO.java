package com.tianji.learning.domain.vo;

import com.tianji.learning.enums.LessonStatus;
import com.tianji.learning.enums.PlanStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "课程表信息")
public class LearningLessonVO {

    @ApiModelProperty("主键lessonId")
    private Long id;

    @ApiModelProperty("课程id")
    private Long courseId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程封面url")
    private String courseCoverUrl;

    @ApiModelProperty("课程章节数量")
    private Integer sections;

    @ApiModelProperty("课程状态, 0-未开始, 1-学习中, 2-已结束")
    private LessonStatus status;

    // @ApiModelProperty("已学习章节数量")
    // private Integer learnedSections;

    // @ApiModelProperty("课程总章节数量")
    // private Integer courseAmount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("过期时间")
    private LocalDateTime expireTime;

    @ApiModelProperty("计划状态, 0-未开始, 1-学习中, 2-已结束")
    private PlanStatus planStatus;

    // @ApiModelProperty("最近学习的小节名")
    // private String latestSectionId;
}
