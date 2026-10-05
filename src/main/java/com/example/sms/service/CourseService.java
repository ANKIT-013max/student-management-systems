package com.example.sms.service;

import com.example.sms.dto.CourseRequestDto;
import com.example.sms.dto.CourseResponseDto;



public interface CourseService {

    CourseResponseDto createCourse(CourseRequestDto dto);

    CourseResponseDto getCourseById(Long id);

    CourseResponseDto updateCourse(Long id, CourseRequestDto dto);

    void deleteCourse(Long id);
}