package br.edu.utfpr.pb.aula6.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.edu.utfpr.pb.aula6.model.Serie;
import br.edu.utfpr.pb.aula6.service.SerieService;

@Controller
@RequestMapping("grafico")
public class GraficoController {
	
	@Autowired
	private SerieService serieService;
	
	
	@GetMapping("serie")
	public ModelAndView graficoSerieGenero(Model model) {
		// Este gráfico irá exibir o número de séries cadastradas em cada gênero.
		ModelAndView modelAndView = new ModelAndView("grafico/serie");
		
		// o método findSerieGroupByGenero() vai trazer um count() com o total de séries agrupadas por gênero.
		List<Object[]> list = serieService.findSerieGroupByGenero();
		
		// cria o cabeçalho do gráfico
		Object[] header = {"Genero","Total"};
		list.add(0, header);
		
		modelAndView.addObject("data", list);
		return modelAndView;
	}
	
	@GetMapping("serie/valor")
	public ModelAndView graficoSerieEpisodios(Model model) {
		ModelAndView modelAndView = new ModelAndView("grafico/serie-valor");
		List<Object[]> list = new ArrayList<>();
		
		int nCols = serieService.findAll().size() + 1;
		
		// Cria o cabeçalho com o ano e adicionando todas as séries
		Object[] header = new Object[nCols];
		header[0] = "Ano";
		int i = 1;
		for (Serie s : serieService.findAll()) {
			header[i++] = s.getNome();
		}
		list.add(0, header);
		
		// como as séries não possuem valores, eles serão adicionados randomicamente na lista
		Random gerador = new Random();
		Integer ano;
		for (int j = 1; j <= 10; j++) {
			Object[] row = new Object[nCols];
			ano = 2000 + j;
			row[0] = ano.toString();
			for (int k = 1; k < row.length; k++) {
				row[k] = gerador.nextInt(10) * 1000;
			}
			list.add(j, row);
		}
		
		
		modelAndView.addObject("data", list);
		return modelAndView;
	}

}
