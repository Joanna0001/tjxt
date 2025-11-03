package com.tianji.learning.controller;

import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.domain.query.PageQuery;
import com.tianji.learning.domain.vo.LearningLessonVO;
import com.tianji.learning.service.ILearningLessonService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lessons")
@Api(tags = "我的课程相关接口")
@RequiredArgsConstructor
public class LearningLessonController {

    private final ILearningLessonService learningLessonService;

    @GetMapping("/page")
    public PageDTO<LearningLessonVO> queryMyLessons(PageQuery query) {
        return learningLessonService.queryMyLessons(query);
    }
}
