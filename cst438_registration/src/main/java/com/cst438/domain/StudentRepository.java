package com.cst438.domain;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository <Student, Integer> {
	
	 Student findByEmail(String email); 
	
	 Student[] findByNameStartsWith(String name);
	 
	 Student findByStatusCode(int statusCode);
	 
	 List<Student> findAll();
	 
	 //Page<Student> findAll(Pageable pageable);

}
