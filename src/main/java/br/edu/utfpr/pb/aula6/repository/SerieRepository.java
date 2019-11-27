package br.edu.utfpr.pb.aula6.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.utfpr.pb.aula6.model.Serie;

public interface SerieRepository 
			extends JpaRepository<Serie, Long>{

	@Query(value = "SELECT genero.nome as genero, count(*) as total "
			+ "FROM Serie INNER JOIN Genero ON serie.genero_id = genero.id "
			+ "GROUP BY genero", 
			nativeQuery = true)
	List<Object[]> findSerieGroupByGenero();
	
}
