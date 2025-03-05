package com.gym.server.service.impliment;

import com.gym.server.model.Course;
import com.gym.server.repository.CourseRepository;
import com.gym.server.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<?> getAll() {
        List<Course> feeList = new ArrayList<>();
        courseRepository.findAll().forEach(f -> feeList.add(f));
        return feeList;
    }

    @Override
    public Course addFee(Course req) {
        Course fee = new Course();
        fee.setTitle(req.getTitle());
        fee.setDescription(req.getDescription());
        fee.setSessions(req.getSessions());
        fee.setAmount(req.getAmount());
        fee.setDiscount(req.getDiscount());
        return courseRepository.save(fee);
    }

    @Override
    @Transactional
    public Course updateFee(Course req) {
        Course existFee = courseRepository.findById(req.getId()).orElseThrow(() -> new NoSuchElementException("Not found"));
        existFee.setTitle(req.getTitle());
        existFee.setDescription(req.getDescription());
        existFee.setSessions(req.getSessions());
        existFee.setAmount(req.getAmount());
        existFee.setDiscount(req.getDiscount());
        return courseRepository.save(existFee);

    }

    @Override
    public String deleteFee(Long id) {
        Course existFee = courseRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not found"));
        courseRepository.delete(existFee);
        return "دوره حذف گردید";
    }

    @Override
    public Course retriveFee(Long id) {
        Course existFee = courseRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not found"));
        return existFee;
    }
}
