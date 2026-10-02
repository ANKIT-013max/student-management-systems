package com.example.sms.service.impl;

import com.example.sms.dto.StudentPatchRequestDto;
import com.example.sms.dto.StudentRequestDto;
import com.example.sms.dto.StudentResponseDto;
import com.example.sms.entity.Student;
import com.example.sms.exception.DuplicateResourceException;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.StudentRepository;
import com.example.sms.service.StudentService;
import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

//    public static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);


    // CREATE
    @Override
    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto studentRequestDto) {

        log.warn("Creating student with email: {}", studentRequestDto.getEmail());

        if (studentRepository.existsByEmail(studentRequestDto.getEmail())) {

            log.warn("Student creation failed. Email already exists: {}", studentRequestDto.getEmail());

            throw new DuplicateResourceException("Student already exists with email : " + studentRequestDto.getEmail());
        }

        Student student = dtoToEntity(studentRequestDto);

        Student savedStudent = studentRepository.save(student);

        log.info("Student created successfully with id: {}", savedStudent.getId());

        return entityToDto(savedStudent);
    }


    // GET BY ID
    @Override
    public StudentResponseDto getStudentById(Long id) {

        log.debug("Fetching student with id: {}", id);

        Student student = findStudent(id);

        log.debug("Student found with id: {}", id);

        return entityToDto(student);
    }


    // GET ALL
    @Override
    public List<StudentResponseDto> getAllStudents() {

        log.debug("Fetching all students");

        List<StudentResponseDto> students = studentRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .toList();

        log.info("Fetched {} students", students.size());

        return students;
    }


    // UPDATE
    @Override
    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto studentRequestDto) {

        log.info("Updating student with id: {}", id);

        Student existingStudent = findStudent(id);

        if (studentRepository.existsByEmailAndIdNot(studentRequestDto.getEmail(), id)) {

            log.warn("Student update failed. Email {} is already used by another student", studentRequestDto.getEmail());

            throw new DuplicateResourceException("Another student already exists with email : " + studentRequestDto.getEmail());

        }

        // Update existing entity
        existingStudent.setFirstName(studentRequestDto.getFirstName());
        existingStudent.setLastName(studentRequestDto.getLastName());
        existingStudent.setEmail(studentRequestDto.getEmail());
        existingStudent.setPhoneNumber(studentRequestDto.getPhoneNumber());
        existingStudent.setDateOfBirth(studentRequestDto.getDateOfBirth());

        Student updatedStudent = studentRepository.save(existingStudent);

        log.info("Student updated successfully with id: {}", updatedStudent.getId());

        return entityToDto(updatedStudent);
    }


    // PATCH
    @Override
    @Transactional
    public StudentResponseDto patchStudent(Long id, StudentPatchRequestDto dto) {

        log.info("Partially updating student with id: {}", id);

        Student existingStudent = findStudent(id);

        boolean updated = false;

        if (dto.getFirstName() != null) {
            existingStudent.setFirstName(dto.getFirstName());
            updated = true;
            log.debug("First name updated for student id: {}", id);
        }

        if (dto.getLastName() != null) {
            existingStudent.setLastName(dto.getLastName());
            updated = true;
            log.debug("Last name updated for student id: {}", id);
        }

        if (dto.getEmail() != null) {

            if (studentRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {

                log.warn("Student patch failed. Email {} is already used by another student", dto.getEmail());

                throw new DuplicateResourceException("Another student already exists with email : " + dto.getEmail());
            }

            existingStudent.setEmail(dto.getEmail());
            updated = true;

            log.debug("Email updated for student id: {}", id);
        }


        if (dto.getPhoneNumber() != null) {
            existingStudent.setPhoneNumber(dto.getPhoneNumber());
            updated = true;
            log.debug("Phone number updated for student id: {}", id);
        }

        if (dto.getDateOfBirth() != null) {
            existingStudent.setDateOfBirth(dto.getDateOfBirth());
            updated = true;
            log.debug("Date of birth updated for student id: {}", id);
        }

        if (!updated) {
            log.warn("No fields provided for patch operation. Student id: {}", id);

            // It would be better to reject an empty PATCH request.
            throw new IllegalArgumentException(
                    "At least one field must be provided for update"
            );
        }

        Student patchStudent = studentRepository.save(existingStudent);
        log.info("Student patched successfully with id: {}", patchStudent.getId());
        return entityToDto(patchStudent);
    }


    // DELETE
    @Override
    @Transactional
    public void deleteStudent(Long id) {
        log.info("Deleting student with id: {}", id);

        Student existingStudent = findStudent(id);

        studentRepository.delete(existingStudent);

        log.info("Student deleted successfully with id: {}", id);
    }


    // HELPER METHODS

    // Helper method to find student
    public Student findStudent(Long id) {

        log.debug("Searching for student with id: {}", id);

        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Student not found with id: {}", id);

                    return new ResourceNotFoundException("Student not found : " + id);
                });
    }

    // Helper method: DTO → Entity
    private Student dtoToEntity(StudentRequestDto studentRequestDto) {
        log.debug("Converting StudentRequestDto to Student entity");
        return Student.builder()
                .firstName(studentRequestDto.getFirstName())
                .lastName(studentRequestDto.getLastName())
                .email(studentRequestDto.getEmail())
                .phoneNumber(studentRequestDto.getPhoneNumber())
                .dateOfBirth(studentRequestDto.getDateOfBirth())
                .build();
    }

    // Helper method: Entity → DTO
    private StudentResponseDto entityToDto(Student student) {
        log.debug("Converting Student entity to StudentResponseDto. id: {}", student.getId());
        return StudentResponseDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .dateOfBirth(student.getDateOfBirth())
                .build();
    }



}









/*
-> Without Builder, you can use a constructor or setters.

    private StudentResponseDto entityToDto(Student student) {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setPhoneNumber(student.getPhoneNumber());
        dto.setDateOfBirth(student.getDateOfBirth());
        return dto;
    }
*/

