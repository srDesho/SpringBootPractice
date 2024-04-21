package com.cristianml.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class JpaController {

	@GetMapping("/jpa-repository")
	public String home() {
		return "jpa_repository/home";
	}
}
