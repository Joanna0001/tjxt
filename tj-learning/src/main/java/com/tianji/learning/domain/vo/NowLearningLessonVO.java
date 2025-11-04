package com.tianji.learning.domain.vo;

import com.tianji.learning.enums.PlanStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "正在学习的课程")
public class NowLearningLessonVO {

    @ApiModelProperty("课程id")
    private Long courseId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程总课时数")
    private Integer sections;

    @ApiModelProperty("已学习课时数")
    private Integer learnedSections;

    @ApiModelProperty("课表中课程总数")
    private Long courseAmount;

    @ApiModelProperty("加入课表时间")
    private LocalDateTime createTime;

    @ApiModelProperty("过期时间")
    private LocalDateTime expireTime;

    @ApiModelProperty("最近一次学习的小节名称")
    private String latestSectionName;

    @ApiModelProperty("最近一次学习的小节序号")
    private Integer latestSectionIndex;

    @ApiModelProperty("课程封面url")
    private String courseCoverUrl;
}
