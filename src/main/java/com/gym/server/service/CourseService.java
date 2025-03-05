package com.gym.server.service;

import com.gym.server.model.Course;

import java.util.List;

public interface CourseService {
    List<?> getAll();
    Course addFee(Course req);
    Course updateFee(Course req);
    String deleteFee(Long id);
    Course retriveFee(Long id);

}
