package com.cst438.controller;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;

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
	public StudentDTO[]  getAllStudents() {
		System.out.println("Getting all students");
		 Iterable<Student> students = studentRepository.findAll();
		 int size= (int) students.spliterator().getExactSizeIfKnown();
		 StudentDTO[] studentDTOs = new StudentDTO[size];
		 int i = 0;
		  for (Student student : students) {
		        studentDTOs[i] = new StudentDTO(
		            student.getStudent_id(),
		            student.getEmail(),
		            student.getName(),
		            student.getStatusCode(),
		            student.getStatus()
		        );
		        i++;
		    }
		  return studentDTOs;
    }
	
	@GetMapping("/students/{id}")
	public Optional<Student> getByID(@PathVariable("id") int id) {
		return studentRepository.findById(id);
		
    }
	
	@PostMapping("/addStudent")
	public Student addStudent(@RequestBody()  Student student ) {
		studentRepository.save(student);
		return student; 
	}
	
	@PutMapping("/updateStudent/{id}")
	public Student updateStudent(@PathVariable int id, @RequestBody() Student student) { 
		Optional<Student> studentO = studentRepository.findById(id);
		Student studentToUpdate =studentO.get();
		if (studentO!= null) {
			studentToUpdate.setEmail(student.getEmail());
			studentToUpdate.setName(student.getName());
			studentToUpdate.setStatus(student.getStatus());
			studentToUpdate.setStatusCode(student.getStatusCode());
			studentRepository.save(studentToUpdate);
		}
		return studentRepository.findById(id).get();
	}
	
	
	@DeleteMapping("/deleteStudent/{id}")
	public ResponseEntity<String>  deleteStudent(@PathVariable("id")  int id,  @RequestParam ("force") Optional <String> force) {
		Student student = studentRepository.findById(id).orElse(null);
		List <Enrollment> enrolls = (List<Enrollment>) enrollmentRepository.findAll();
		  if (student != null) {
			  {
		  }
					for (int i =0;i<enrolls.size();i++) {
						if (enrolls.get(i).getStudent().getStudent_id() == id) {
							if (enrolls.get(i).getStudent().getStatusCode() == 1) {
								System.out.print("WARNING: This student is enrolled, are you sure you want to delete this student?(y/n)");
								Scanner scan = new Scanner(System.in);
								String ok = "n";
								ok = scan.next();
								if (ok.equals("n")) {
									return ResponseEntity.status(HttpStatus.CONFLICT).body("Student was not deleted because they are currently enrolled.");
								}
							}
						}
					}
					
		        studentRepository.delete(student);
		        return ResponseEntity.ok("Student deleted successfully");
		    } else {
		        return ResponseEntity.notFound().build();
		    }
	
	}
	
}
