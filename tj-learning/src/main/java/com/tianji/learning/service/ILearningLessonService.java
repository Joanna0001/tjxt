package com.tianji.learning.service;

import java.util.List;

public interface ILearningLessonService {
    void addUserLessons(Long userId, List<Long> courseIds);
}
