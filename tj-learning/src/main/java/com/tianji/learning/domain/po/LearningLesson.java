package com.tianji.learning.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学员课表（学籍）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("learning_lesson")
public class LearningLesson implements Serializable {
  private static final long serialVersionUID = 1L;

  /** 主键id */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 学员id */
  private Long userId;

  /** 课程id */
  private Long courseId;

  /** 学习状态：0-未学习，1-学习中，2-已学完，3-已过期 */
  private Integer status;

  /** 每周学习频率 */
  private Integer weekFreq;

  /** 学习计划状态：0-无计划，1-计划进行中 */
  private Integer planStatus;

  /** 已学习章节数 */
  private Integer learnedSections;

  /** 最近一次学习的章节id */
  private Long latestSectionId;

  /** 最近一次学习时间 */
  private LocalDateTime latestLearnTime;

  /** 创建时间 */
  private LocalDateTime createTime;

  /** 过期时间 */
  private LocalDateTime expireTime;

  /** 更新时间 */
  private LocalDateTime updateTime;
}
