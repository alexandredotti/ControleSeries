package br.edu.utfpr.pb.aula6.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.edu.utfpr.pb.aula6.model.Serie;
import br.edu.utfpr.pb.aula6.service.SerieService;

@Controller
@RequestMapping("watch")
@SessionAttributes("watchList")
public class WatchListController {

	@Autowired
	private SerieService serieService;

	@ModelAttribute("watchList")
	private List<Serie> getWatchList() {
		return new ArrayList<>();
	}

	@GetMapping
	public String list(Model model, @ModelAttribute("watchList") List<Serie> watchList) {

		model.addAttribute("watchList", watchList);

		return "watch/list";
	}

	@GetMapping("add/{id}")
	public String addSerie(@PathVariable Long id, Model model, @ModelAttribute("watchList") List<Serie> watchList) {

		watchList.add(serieService.findOne(id));
		System.out.println(serieService.findOne(id));
		return "redirect:/watch";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("add2/{id}")
	public String addSerie(@PathVariable Long id, HttpServletRequest request) {
		List<Serie> watchList;
		if (request.getSession().getAttribute("watchList") != null) {
			watchList = (List<Serie>) request.getSession().getAttribute("watchList");
			watchList.add(serieService.findOne(id));
		} else {
			watchList = new ArrayList<>();
			watchList.add(serieService.findOne(id));
			request.getSession().setAttribute("watchList", watchList);
		}

		return "redirect:/watch";
	}

	@GetMapping("remove/{id}")
	public String removeSerie(@PathVariable Long id, Model model, 
			@ModelAttribute("watchList") List<Serie> watchList) {
		watchList.remove(serieService.findOne(id));
		return "redirect:/watch";
	}
	
	@GetMapping("clear")
	public String clear(@ModelAttribute("watchList") List<Serie> watchList) {
		
		watchList = new ArrayList<>();
		
		return "redirect:/watch";
	}
	
	@GetMapping("clear2")
	public String clear(HttpServletRequest request) {
		
		// request.getSession().setAttribute("watchList", new ArrayList<>());
		request.getSession().removeAttribute("watchList");
		// request.getSession().invalidate();
		
		return "redirect:/watch";
	}
}
