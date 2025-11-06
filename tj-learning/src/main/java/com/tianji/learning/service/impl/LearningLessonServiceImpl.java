package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.course.CatalogueClient;
import com.tianji.api.client.course.CourseClient;
import com.tianji.api.dto.course.CataSimpleInfoDTO;
import com.tianji.api.dto.course.CourseFullInfoDTO;
import com.tianji.api.dto.course.CourseSimpleInfoDTO;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.domain.query.PageQuery;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.learning.domain.po.LearningLesson;
import com.tianji.learning.domain.vo.LearningLessonVO;
import com.tianji.learning.domain.vo.NowLearningLessonVO;
import com.tianji.learning.enums.LessonStatus;
import com.tianji.learning.mapper.LearningLessonMapper;
import com.tianji.learning.service.ILearningLessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LearningLessonServiceImpl extends ServiceImpl<LearningLessonMapper, LearningLesson> implements ILearningLessonService {

    private final CourseClient courseClient;
    private final CatalogueClient catalogueClient;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserLessons(Long userId, List<Long> courseIds) {
        // 查询课程有效期
        List<CourseSimpleInfoDTO> cInfoList = courseClient.getSimpleInfoList((courseIds));
        if(CollUtils.isEmpty(cInfoList)) {
            log.error("课程信息不存在，无法添加到课表");
            return;
        }

        // 循环遍历，处理LearningLesson数据
        List<LearningLesson> list = new ArrayList<>(cInfoList.size());
        for(CourseSimpleInfoDTO cInfo : cInfoList) {
            LearningLesson lesson = new LearningLesson();
            // 获取过期时间
            Integer validDuration = cInfo.getValidDuration();
            if(validDuration != null && validDuration > 0) {
                LocalDateTime now = LocalDateTime.now();
                lesson.setCreateTime(now);
                lesson.setExpireTime(now.plusMonths(validDuration));
            }
            lesson.setUserId(userId);
            lesson.setCourseId(cInfo.getId());
            list.add(lesson);
        }
        // 批量新增
        saveBatch(list);
    }

    @Override
    public PageDTO<LearningLessonVO> queryMyLessons(PageQuery query) {
        Long userId = UserContext.getUser();

        Page<LearningLesson> page = lambdaQuery()
                .eq(LearningLesson::getUserId, userId)
                .page(query.toMpPage("latest_learn_time", false));

        List<LearningLesson> records = page.getRecords();
        if(CollUtils.isNotEmpty(records)) {
            return PageDTO.empty(page);
        }

        // 查询课程信息
        // 获取课程id
        Set<Long> cIds = records.stream().map(LearningLesson::getCourseId).collect(Collectors.toSet());
        // 查询课程信息
        List<CourseSimpleInfoDTO> cInfoList = courseClient.getSimpleInfoList(cIds);
        if(CollUtils.isEmpty(cInfoList)) {
            throw new BadRequestException("课程信息不存在！");
        }
        // 把课程集合处理成Map, key是courseId, 值是course本身
        Map<Long, CourseSimpleInfoDTO> cMap = cInfoList.stream().
                collect(Collectors.toMap(CourseSimpleInfoDTO::getId, c -> c));

        // 封装VO返回
        List<LearningLessonVO> list = new ArrayList<>(cInfoList.size());
        for(LearningLesson r : records) {
            // 拷贝基础属性到vo
            LearningLessonVO vo = BeanUtils.copyBean(r, LearningLessonVO.class);
            // 获取课程信息，填充到vo
            CourseSimpleInfoDTO cInfo = cMap.get(r.getCourseId());
            vo.setCourseName(cInfo.getName());
            vo.setCourseCoverUrl(cInfo.getCoverUrl());
            vo.setSections(cInfo.getSectionNum());
            list.add(vo);
        }
        return PageDTO.of(page, list);
    }

    @Override
    public LearningLesson queryLessonByCourseId(Long courseId) {
        if(courseId == null) {
            log.error("<UNK>");
            throw new IllegalArgumentException("课程id不能为空");
        }

        LambdaQueryWrapper<LearningLesson> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LearningLesson::getUserId, UserContext.getUser());
        queryWrapper.eq(LearningLesson::getCourseId, courseId);

        return getOne(queryWrapper);
    }

    @Override
    public NowLearningLessonVO queryNowLessons() {
        Long userId = UserContext.getUser();

        // 查询正在学习的课程
        LambdaQueryWrapper<LearningLesson> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LearningLesson::getUserId, userId);
        Long courseAmount = Long.valueOf(count(queryWrapper));
        queryWrapper.eq(LearningLesson::getStatus, LessonStatus.LEARNING);
        queryWrapper.orderByDesc(LearningLesson::getLatestLearnTime);

        LearningLesson lesson = getOne(queryWrapper);
        if(lesson == null) {
            return null;
        }

        // 查询课程信息
        NowLearningLessonVO vo = BeanUtils.copyBean(lesson, NowLearningLessonVO.class);
        CourseFullInfoDTO courseInfo = courseClient.getCourseInfoById(lesson.getCourseId(), true, false);
        vo.setCourseName(courseInfo.getName());
        vo.setSections(courseInfo.getSectionNum());
        vo.setCourseCoverUrl(courseInfo.getCoverUrl());
        vo.setCourseAmount(courseAmount);

        // 查询小节信息
        List<CataSimpleInfoDTO> cataInfos = catalogueClient.batchQueryCatalogue(CollUtils.singletonList((lesson.getLatestSectionId())));
        if(!CollUtils.isNotEmpty(cataInfos)) {
            CataSimpleInfoDTO cataInfo = cataInfos.get(0);
            vo.setLatestSectionIndex(cataInfo.getCIndex());
            vo.setLatestSectionName(cataInfo.getName());
        }
        log.info("查询最近学习的课程: {}", vo);
        return vo;
    }

    @Override
    public Integer countLearningLessonByCourse(Long courseId) {
        return lambdaQuery()
                .eq(LearningLesson::getCourseId, courseId)
                .count();
    }
}
