package com.gym.server.service;

import com.gym.server.model.Course;

import java.util.List;

public interface CourseService {
    List<?> getAll();
    Course add(Course req);
    Course update(Course req);
    String delete(Long id);
    Course retrieve(Long id);

}
