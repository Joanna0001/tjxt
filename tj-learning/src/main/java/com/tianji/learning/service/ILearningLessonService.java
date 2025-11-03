package com.tianji.learning.service;

import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.domain.query.PageQuery;
import com.tianji.learning.domain.vo.LearningLessonVO;

import java.util.List;

public interface ILearningLessonService {
    void addUserLessons(Long userId, List<Long> courseIds);

    PageDTO<LearningLessonVO> queryMyLessons(PageQuery query);
}
