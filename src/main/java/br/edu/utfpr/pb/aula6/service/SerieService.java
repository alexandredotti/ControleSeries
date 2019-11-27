package br.edu.utfpr.pb.aula6.service;

import java.util.List;

import br.edu.utfpr.pb.aula6.model.Serie;

public interface SerieService 
	extends CrudService<Serie, Long>{

	List<Object[]> findSerieGroupByGenero();
	
}
