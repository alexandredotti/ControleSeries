package br.edu.utfpr.pb.aula6.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.edu.utfpr.pb.aula6.model.Produtora;
import br.edu.utfpr.pb.aula6.repository.ProdutoraRepository;
import br.edu.utfpr.pb.aula6.service.ProdutoraService;

@Service
public class ProdutoraServiceImpl 
			extends CrudServiceImpl<Produtora, Long> 
			implements ProdutoraService{

	@Autowired
	private ProdutoraRepository produtoraRepository;
	
	@Override
	protected JpaRepository<Produtora, Long> getRepository() {
		return produtoraRepository;
	}
}
