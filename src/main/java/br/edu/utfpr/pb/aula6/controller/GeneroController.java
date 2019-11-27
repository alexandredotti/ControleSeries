package br.edu.utfpr.pb.aula6.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.utfpr.pb.aula6.model.Genero;
import br.edu.utfpr.pb.aula6.service.GeneroService;

@Controller
@RequestMapping("genero")
public class GeneroController {

	@Autowired
	private GeneroService generoService;

	@GetMapping
	public String list(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
			Model model) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(5);

		Page<Genero> list = this.generoService.findAll(PageRequest.of(currentPage - 1, pageSize));

		model.addAttribute("list", list);

		if (list.getTotalPages() > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, list.getTotalPages()).boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "genero/list";
	}

	@PostMapping
	public ResponseEntity<?> save(
			@RequestBody @Valid Genero entity, 
			BindingResult result, Model model,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		generoService.save(entity);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("{id}")
	@ResponseBody
	public Genero edit(@PathVariable Long id) {
		return generoService.findOne(id);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id, RedirectAttributes attributes) {
		try {
			generoService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
