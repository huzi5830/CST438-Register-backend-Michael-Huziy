package com.cst438;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import ch.qos.logback.core.boolex.Matcher;

import com.cst438.domain.*;
@SpringBootTest
@AutoConfigureMockMvc


public class JunitStudentTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	

	public String setUpdateEmail(Scanner scan) {
	    Boolean ok = false;
	    String email;
	    do {
	        System.out.print("Enter or update email: ");
	        email = scan.nextLine();
	        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
	        Pattern pattern = Pattern.compile(emailRegex);
	        java.util.regex.Matcher matcher = pattern.matcher(email);
	        ok = matcher.matches();
	        
	        if (!ok) {
	            System.out.println("Invalid format, please re-enter email");
	        }
	        
	    } while (!ok);
	    return email;
	}

	@Test
	public void addStudentTest() throws Exception {
	    MockHttpServletResponse response;
	    Scanner scan = new Scanner(System.in);
	    
	    System.out.print("\n\n\n\nHow many students do you wish to add?: ");
	    int num = scan.nextInt();
	    scan.nextLine(); // Consume the newline character
	    
	    for (int i = 0; i < num; i++) {
	        Student student = new Student();
	        System.out.print("Enter name: ");
	        student.setName(scan.nextLine());
	        
	        // Use the setUpdateEmail method to validate and set the email
	        String email = setUpdateEmail(scan);
	        student.setEmail(email);
	        student.setStatusCode(1);
	        student.setStatus("Enrolled");
	        String studentJson = asJsonString(student);
		    
			response = mvc.perform(
					MockMvcRequestBuilders
					.post("/addStudent")
					.contentType(MediaType.APPLICATION_JSON)
					.content(studentJson)
				    .accept(MediaType.APPLICATION_JSON))
					.andReturn().getResponse();
			assertEquals(200, response.getStatus());
			
			Student result = fromJsonString(response.getContentAsString(),Student.class);
			assertNotEquals(0,result.getStudent_id());
			assertEquals(result.getName(),student.getName());
			assertEquals(result.getEmail(),student.getEmail());
			assertEquals(result.getStatus(),student.getStatus());
		}
		scan.close();
	}
	
	@Test
	public void getAllStudentsTest() throws Exception {
		MockHttpServletResponse response;
		int i = 0;
		while (i <3) {
			Student student = new Student();
			student.setName("Name " + (char)(i+65) );
			student.setEmail((char)(i+65) + "@email.com");
			student.setStatusCode(i+100);
			student.setStatus("Enrolled, added for getAllStudents Test");
			String studentJson =asJsonString(student); 
			
			response = mvc.perform(
					MockMvcRequestBuilders
					.post("/addStudent")
					.contentType(MediaType.APPLICATION_JSON)
					.content(studentJson)
				    .accept(MediaType.APPLICATION_JSON))
					.andReturn().getResponse();
			assertEquals(200, response.getStatus());
			i++;
		}
		i = 0;

		response = mvc.perform(
				MockMvcRequestBuilders
				.get("/students")
				.contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(200, response.getStatus());
		
		ObjectMapper objectMapper = new ObjectMapper();
	    StudentDTO[] students = objectMapper.readValue(response.getContentAsString(), StudentDTO[].class);
	    Scanner scan = new Scanner(System.in);
	    System.out.print("Test: " +  ("Name " + (char) (i+65)));
	    System.out.print("Test2: " + (char)(i+65) + "@email.com");
	   // scan.nextInt();
	    scan.close();
	    for (StudentDTO student: students) {
	    	switch(student.statusCode()) {
	    	case 100:
	    		assertEquals(student.name(),"Name A" );
	    		assertEquals(student.email(),"A@email.com");
	    		break;
	    	case 101:
	    		assertEquals(student.name(), "Name B");
	    		assertEquals(student.email(),"B@email.com");
	    		break;
	    	case 102:
	    		assertEquals(student.name(), "Name C");
	    		assertEquals(student.email(),"C@email.com");
	    		break;
	    	default:
	    	assertNotNull(student.id());
	    	assertNotNull(student.name());
	    	break;
	    	}
	    }
	}

	@Test
	public void updateStudentTest() throws Exception {
		Scanner scan = new Scanner(System.in);
		MockHttpServletResponse response;
		int exit = 0;
		List<Student> students = studentRepository.findAll();
		
		for(int i = 0;i<students.size();i++) {
			System.out.println("ID:" +  students.get(i).getStudent_id());
		}
	
		while (exit != 1) {
			System.out.print("Enter the Id of the student to update:");
			int id = scan.nextInt();
			scan.nextLine();
			Optional<Student> studentO = studentRepository.findById(id);//check for existence here to not 
			                                                            //waste time entering data for an empty student in repo
			String email = "0";
			
			if (studentO.isPresent() ) {
				Student student = studentO.get();
				System.out.print("Enter new name:" );
				student.setName(scan.nextLine());
				setUpdateEmail(scan);
				student.setEmail(email);
				System.out.println("Enter new status: ");
				student.setStatus(scan.nextLine());
				String studentJson =asJsonString(student); 
				
				response = mvc.perform(
							MockMvcRequestBuilders
							.put("/updateStudent/"+ id)
							.contentType(MediaType.APPLICATION_JSON)
							.content(studentJson)
							.accept(MediaType.APPLICATION_JSON))
							.andReturn().getResponse();
				
				assertEquals(200, response.getStatus());
				 Student result = fromJsonString(response.getContentAsString(),Student.class);
				assertEquals(result.getStudent_id(),student.getStudent_id());
				assertEquals(result.getName(),student.getName());
				assertEquals(result.getEmail(),student.getEmail());
			
				System.out.println(String.format("Enter %d if you wish to update another student, or %d if you wish to exit: ",0,1) );
				exit = scan.nextInt();
			}
			else {
				System.out.println("ERROR: Id not found\n");
				continue;
			}
		}
	scan.close();
	}
	
	@Test
	public void deleteStudentTest() throws Exception {
		Scanner scan = new Scanner(System.in);
		MockHttpServletResponse response;
		List <Enrollment> enrolls = (List<Enrollment>) enrollmentRepository.findAll();
	
		/*Student studentToDelete = new Student();
		studentToDelete.setName("DeleteName");
		studentToDelete.setStatusCode(1);
		studentToDelete.setEmail("delete@email.com");
		studentToDelete.setStatus("Testing for deleteStudent()");
		String studentJsonTest = asJsonString(studentToDelete); 
		
		response = mvc.perform(
				MockMvcRequestBuilders
				.post("/addStudent")
				.contentType(MediaType.APPLICATION_JSON)
				.content(studentJsonTest)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		Student resultTest = fromJsonString(response.getContentAsString(),Student.class);
		int testID = resultTest.getStudent_id();
		System.out.println("IDS to simulate: " + testID);*/
		int exit = 0;
		
		while (exit != 1) {
			List<Student> students = studentRepository.findAll();//Simulate access to student id's on hand
			for(int i = 0;i<students.size();i++) {
				System.out.println("ID:" +  students.get(i).getStudent_id());
				System.out.println(students.get(i).getName());
			}
			String ok = "y";
			int id =0;
			System.out.print("\nEnter the id of the student to delete:");
			id = scan.nextInt();
				response = mvc.perform(
					MockMvcRequestBuilders
					.delete("/deleteStudent/" + id)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andReturn().getResponse();
				
			if (response.getStatus() == 200) {
				Optional<Student> student = studentRepository.findById(id);
				assert(student.isEmpty());
				System.out.print(String.format("Enter %d if you wish to delete another student, or %d if you wish to exit: ",0,1) );
				exit = scan.nextInt();
			}
			else if (response.getStatus() == 409) {
				System.out.print(String.format("Do you wish to delete another student? Enter %d if you wish to delete another student, or %d if you wish to exit: ",0,1) );
				exit = scan.nextInt();
			}
			else {
				System.out.println("ERROR: Student not found in repository");
				continue;
			}
		
		}
		scan.close();
	}
	
private static <T> T  fromJsonString(String str, Class<T> valueType ) {
	try {
		return new ObjectMapper().readValue(str, valueType);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}
private static String asJsonString(final Object obj) {
	try {
		return new ObjectMapper().writeValueAsString(obj);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	}
}