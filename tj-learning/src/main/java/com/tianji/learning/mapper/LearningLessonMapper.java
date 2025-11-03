package com.tianji.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianji.learning.domain.po.LearningLesson;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LearningLessonMapper extends BaseMapper<LearningLesson>{
}
