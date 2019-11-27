package br.edu.utfpr.pb.aula6.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.edu.utfpr.pb.aula6.model.Serie;
import br.edu.utfpr.pb.aula6.service.GeneroService;
import br.edu.utfpr.pb.aula6.service.ProdutoraService;
import br.edu.utfpr.pb.aula6.service.SerieService;

@Controller
@RequestMapping("serie")
public class SerieController {
	
	@Autowired
	private SerieService serieService;
	@Autowired
	private GeneroService generoService;
	@Autowired
	private ProdutoraService produtoraService;
	
	@GetMapping
	public String list(@RequestParam("page") Optional<Integer> page, 
					   @RequestParam("size") Optional<Integer> size,
					   Model model) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(5);
		Page<Serie> list = this.serieService
					.findAll(PageRequest.of(currentPage - 1, pageSize));
		model.addAttribute("list", list);

		model.addAttribute("generos", generoService.findAll());
		model.addAttribute("produtoras", produtoraService.findAll());
		
		if (list.getTotalPages() > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, list.getTotalPages()).boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return "serie/list";
	}
	
	@PostMapping
	public ResponseEntity<?> save(@Valid Serie entity,
								  BindingResult result,
				  @RequestParam("anexo") MultipartFile anexo,
				  @RequestParam("anexos") MultipartFile[] anexos,
				  HttpServletRequest request) {
		
		if ( result.hasErrors() ) {
			return new ResponseEntity<>(result.getAllErrors(),
						HttpStatus.BAD_REQUEST);
		}
		serieService.save(entity);
		
		if (anexo != null && !anexo.getOriginalFilename().isEmpty()) {
			saveFile(entity.getId(), anexo, request);
		}
		if (anexos != null 
				&& anexos.length > 0 
				&& !anexos[0].getOriginalFilename().isEmpty()) {
			saveFiles(entity.getId(), anexos, request);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private void saveFile(Long id, MultipartFile anexo, HttpServletRequest request) {
		File dir = new File(request.getServletContext()
									.getRealPath("/images/"));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String caminhoAnexo = request.getServletContext()
										.getRealPath("/images/");
		String extensao = anexo.getOriginalFilename().substring(
				anexo.getOriginalFilename().lastIndexOf(".")
				);
		
		String nomeArquivo = id + extensao;
		
		try {
			FileOutputStream fileOut = new FileOutputStream(
					new File(caminhoAnexo + nomeArquivo)
					);
			BufferedOutputStream stream = new BufferedOutputStream(fileOut);
			stream.write(anexo.getBytes());
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	private void saveFiles(Long id, 
						   MultipartFile[] anexos, 
						   HttpServletRequest request) {
		File dir = new File(request.getServletContext()
									.getRealPath("/images/"));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String caminhoAnexo = request.getServletContext()
										.getRealPath("/images/");
		
		int i = 0; //vai ser o identificador do anexo salvo
		for (MultipartFile anexo : anexos) {
			i++;
			String extensao = anexo.getOriginalFilename().substring(
					anexo.getOriginalFilename().lastIndexOf(".")
					);
			
			String nomeArquivo = id + "_" + i + extensao;
			
			try {
				FileOutputStream fileOut = new FileOutputStream(
						new File(caminhoAnexo + nomeArquivo)
						);
				BufferedOutputStream stream = new BufferedOutputStream(fileOut);
				stream.write(anexo.getBytes());
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}	

	@GetMapping("{id}")
	@ResponseBody
	public Serie edit(@PathVariable Long id) {
		return serieService.findOne(id);
	}
}




