package com.cst438.domain;
public record StudentDTO(int id, String email, String name, int statusCode, String status)  {

	public int id() {
		return id;
	}

	public String email() {
		return email;
	}

	public String name() {
		return name;
	}

	public int statusCode() {
		return statusCode;
	}

	public String status() {
		return status;
	}
	
	
}
