package com.cst438.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cst438.domain.CourseRepository;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.cst438.domain.Student;

@RestController
@CrossOrigin 
public class StudentController {
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	GradebookService gradebookService;
	
	@GetMapping("/students")
	public Iterable<Student> getAllStudents() {
		System.out.println("Getting all students");
		return studentRepository.findAll();
    }
	
	@GetMapping("/students/{id}")
	public Optional<Student> getByID(@PathVariable int id) {
		return studentRepository.findById(id);
    }
	
	@PostMapping("/addStudent")
	public Student addStudent(@RequestBody()  Student student ) {
		studentRepository.save(student);
		return student; 
	}
	
	@PostMapping("/updateStudent/{id}")
	public Student updateStudent(@PathVariable int id, @RequestBody() Student student) { 
		if (student != null) {
			studentRepository.save(student);
		}
		return student;
	}
	
	
	@DeleteMapping("/deleteStudent/{id}")
	public ResponseEntity<String>  deleteStudent(@PathVariable  int id ) {
		Student student = studentRepository.findById(id).orElse(null);
		  if (student != null) {
		        studentRepository.delete(student);
		        return ResponseEntity.ok("Student deleted successfully");
		    } else {
		        return ResponseEntity.notFound().build();
		    }
	
	}
	
}
