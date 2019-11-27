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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.utfpr.pb.aula6.model.Usuario;
import br.edu.utfpr.pb.aula6.service.PermissaoService;
import br.edu.utfpr.pb.aula6.service.UsuarioService;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private PermissaoService permissaoService;
	
	protected String getURL() {
		return "usuario";
	}

	@GetMapping("new")
	protected ModelAndView form(Usuario entity) {
		ModelAndView modelAndView = new ModelAndView(getURL() + "/form");
		if (entity == null) {
			modelAndView.addObject("usuario", new Usuario());
		} else {
			modelAndView.addObject("usuario", entity);
		}
		return modelAndView;
	}
	
	@GetMapping("{id}")
	@ResponseBody
	public Usuario edit(@PathVariable Long id) {
		return usuarioService.findOne(id);
	}
	
	@PostMapping()
	public ResponseEntity<?> saveAjax(@Valid Usuario entity, 
			BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		
		entity.setPassword(
				entity.getEncodedPassword(entity.getPassword()));
		usuarioService.save(entity);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping
	public ModelAndView list(@RequestParam("page") Optional<Integer> page,
							 @RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(5);
		
		Page<Usuario> list = usuarioService.findAll( 
				PageRequest.of(currentPage -1, pageSize) );
		
		ModelAndView modelAndView = new ModelAndView(this.getURL() + "/list");
		modelAndView.addObject("list", list);
		
		modelAndView.addObject("permissoes", permissaoService.findAll());
		
		if( list.getTotalPages() > 0) {
			List<Integer> pageNumbers = IntStream
					.rangeClosed(1, list.getTotalPages())
					.boxed().collect(Collectors.toList());
			modelAndView.addObject("pageNumbers", pageNumbers);
		}
		return modelAndView;
	}

}


