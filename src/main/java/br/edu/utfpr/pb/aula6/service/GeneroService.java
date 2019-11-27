package br.edu.utfpr.pb.aula6.service;

import java.util.List;

import br.edu.utfpr.pb.aula6.model.Genero;

public interface GeneroService extends CrudService<Genero, Long>{

	List<Genero> findByNomeLike(String nome);
}
