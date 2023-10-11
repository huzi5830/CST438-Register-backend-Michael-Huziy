package com.cst438.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;

@Service
@ConditionalOnProperty(prefix = "gradebook", name = "service", havingValue = "rest")
@RestController
public class GradebookServiceREST implements GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	private String gradebook_url;

	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		//gradebook_url = "http://localhost:8081/enrollment";
		System.out.println(gradebook_url);
		System.out.println("Start Message "+ student_email +" " + course_id);
		EnrollmentDTO dto = new EnrollmentDTO(0, student_email, student_name, course_id);
		ResponseEntity<EnrollmentDTO> response = restTemplate.postForEntity(gradebook_url,dto, EnrollmentDTO.class);
		/*HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EnrollmentDTO> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<EnrollmentDTO> response = restTemplate.postForEntity(gradebook_url, requestEntity, EnrollmentDTO.class);*/
		// TODO use RestTemplate to send message to gradebook service
	}
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	/*
	 * endpoint for final course grades
	 */
	@PutMapping("/course/{course_id}")
	@Transactional
	public void updateCourseGrades( @RequestBody FinalGradeDTO[] grades, @PathVariable("course_id") int course_id) {
		System.out.println("Grades received "+grades.length);
		List<Enrollment> enrollments = (List)enrollmentRepository.findAll();
		for (Enrollment e :enrollments) {
			System.out.println(e.toString());
		}
		
		for(int i=0;i<grades.length;i++) {
			System.out.println("\n\n" + grades[i].toString() + "\n");
			System.out.println(grades[i].studentEmail() + "   " + String.valueOf(course_id) );
			Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(grades[i].studentEmail(), course_id);
			if (enrollment == null) {
				System.out.println("NULL");
			}
			else {
				
				enrollment.setCourseGrade(grades[i].grade());
				
				enrollmentRepository.save(enrollment);
			}
			}
		//TODO update grades in enrollment records with grades received from gradebook service
		}
		
		
	
}
