package br.edu.utfpr.pb.aula6.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("home")
public class HomeController {

	@GetMapping
	public String home() {
		return "index";
	}
	
	@GetMapping("teste")
	public String teste() {
		return "index";
	}
}
