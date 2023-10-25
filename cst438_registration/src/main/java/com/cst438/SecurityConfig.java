package com.cst438;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cst438.service.UserDetailsServiceImpl;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthenticationFilter authenticationFilter;

	@Autowired
	private AuthEntryPoint exceptionHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();
		    if (authentication != null) {
		        for (GrantedAuthority authority : ((org.springframework.security.core.Authentication) authentication).getAuthorities()) {
		            String role = authority.getAuthority();
		            System.out.println("Current user role is: " + role);
		          
		        }
		    }
		    else {
		    	 System.out.println("NULL");
		    }
		http.csrf().disable().cors().and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests()
		//.antMatchers(HttpMethod.GET, "/").permitAll()
		.antMatchers(HttpMethod.POST, "/login").permitAll()
		.antMatchers(HttpMethod.GET, "/students").permitAll()
		.antMatchers(HttpMethod.POST, "/addStudent").permitAll()
		.antMatchers(HttpMethod.PUT, "/updateStudent/**").permitAll()
		.antMatchers(HttpMethod.DELETE, "/deleteStudent/**").permitAll()
		.antMatchers("/schedule/**").permitAll()
		.antMatchers("/admin/**").permitAll()
		.antMatchers(HttpMethod.POST, "/h2-console/**").permitAll()
		.anyRequest().authenticated().and()
		.exceptionHandling()
		.authenticationEntryPoint(exceptionHandler).and()
		.addFilterBefore(authenticationFilter, 
				UsernamePasswordAuthenticationFilter.class);
	}	

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("*"));
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowCredentials(false);
		config.applyPermitDefaultValues();

		source.registerCorsConfiguration("/**", config);
		return source;
	}	

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception  {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	public AuthenticationManager getAuthenticationManager() throws 
	Exception {
		return authenticationManager();
	}
}
