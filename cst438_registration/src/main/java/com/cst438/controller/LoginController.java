package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cst438.domain.User;
import com.cst438.domain.UserRepository;
import com.cst438.dto.AccountCredentials;
import com.cst438.service.JwtService;


@RestController
public class LoginController {
	@Autowired
	private JwtService jwtService;

	@Autowired	
	AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
		UsernamePasswordAuthenticationToken creds =
				new UsernamePasswordAuthenticationToken(
						credentials.username(),
						credentials.password());

		Authentication auth = authenticationManager.authenticate(creds);
		
		     BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		     String password = "test";
		     String encrpted_password = encoder.encode(password);
		     System.out.println(encrpted_password);
		 
		

		// Generate token
		String jwts = jwtService.getToken(auth.getName());
		User currentUser = repository.findByAlias(credentials.username()); 
		String role = currentUser.getRole();
		System.out.println("USER ROLE IS : " + role);
		
		/*return ResponseEntity.ok()
		        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
		        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
		        .build();*/
		 HttpHeaders headers = new HttpHeaders();
		 headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwts);
		 headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization, Role");
		 headers.add("Role", role);

		    // Build response with the generated token and the user's role in the header
		    return ResponseEntity.ok()
		            .headers(headers)
		            .build();

	}
}
