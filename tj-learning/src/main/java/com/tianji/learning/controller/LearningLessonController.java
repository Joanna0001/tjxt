package com.tianji.learning.controller;

import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.domain.query.PageQuery;
import com.tianji.learning.domain.po.LearningLesson;
import com.tianji.learning.domain.vo.LearningLessonVO;
import com.tianji.learning.domain.vo.NowLearningLessonVO;
import com.tianji.learning.service.ILearningLessonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("查询我的课程")
    public PageDTO<LearningLessonVO> queryMyLessons(PageQuery query) {
        return learningLessonService.queryMyLessons(query);
    }

    @GetMapping("/now")
    @ApiOperation("查询当前正在学习的课程")
    public NowLearningLessonVO queryNowLesson() {
        return learningLessonService.queryNowLessons();
    }

    @GetMapping("/{courseId}")
    @ApiOperation("通过课程id查询课程")
    public LearningLesson queryLessonByCourseId(Long courseId) {
        return learningLessonService.queryLessonByCourseId(courseId);
    }
}
