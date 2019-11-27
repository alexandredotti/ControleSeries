# Aula 6 da disciplina PW25S - 2019/2
## Conteúdo
No conteúdo do projeto são abordados conceitos de **JavaScript** e **Ajax** e **POST com JSON**.

## Aula
Os arquivos utilizados na primeira parte do projeto serão: dentro de *src/main/java/* o arquivo **GeneroController.java** e dentro de *src/main/resources/* **templates/genero/list.html** e **/static/js/crud-genero.js**.

### GeneroController
O controller será responsável por atender as requisições HTTP vindas do cliente.

```java
package br.edu.utfpr.pb.aula6.controller;

// imports ...
@Controller
@RequestMapping("genero")
public class GeneroController {

	@Autowired
	private GeneroService generoService;

	/** 
		O método list() será responsável por atender as requisições http 
        retornando um objeto Page (que será utilizado para carregar uma
        tabela paginada para o usuário).
        Serão necessários dois parâmetros vindos no request, a página e 
        o tamanho de página, ou seja, será realizada uma consulta paginada
        no banco de dados e serão carregados para o usuário apenas os registros
        da página solicitada de acordo com o totald e registros por página.
    **/
	@GetMapping
	public String list(@RequestParam("page") Optional<Integer> page, 
					   @RequestParam("size") Optional<Integer> size,
					   Model model) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(5);

		Page<Genero> list = this.generoService.findAll(PageRequest.of(currentPage - 1, pageSize));
		/**
			Um objeto com o nome 'list' é adicionado no Model, esse objeto será recuperado
			no arquivo list.html para percorrer a lista de gêneros que será exibida na tabela.
		**/
		model.addAttribute("list", list);

		/** 
			Caso a lista retorne resultados, é montada a lista com o número de páginas de gêneros
			de acordo com o número de registros existentes no banco de dados.
			Esses dados serão utilizados no list.html para montar os links de paginação.
		**/
		if (list.getTotalPages() > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, list.getTotalPages()).boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		/** 
			Retorna a página que contém a lista, ou seja, dentro dos resources da aplicação,
			a pasta templates contém todos os arquivos da camada de visão da aplicação.
			O arquivo que será exibido está dentro da pasta /genero e chama-se list.html.
		**/
		return "genero/list"; 
	}

	/**
		O método save será executado ao realizar uma requisição do tipo HTTP POST para a URL
		/genero.
		O método espera um objeto Genero no formato JSON, isso é indicado pela anotação
		@RequestBody antes do objeto Genero.
		O retorno do Método será um Http response com '200 OK' no caso de sucesso ou um
		'400 BAD REQUEST' no caso de erro.
	**/
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

	/**
		O método edit será executado ao realizar uma requisição do tipo HTTP GET para a URL
		/genero/{id} sendo que {id} deve ser o código de algum gênero existente no banco de dados .
		O método retorna um objeto Genero no formato JSON, isso é indicado pela anotação
		@ResponseBody.
	**/
	@GetMapping("{id}")
	@ResponseBody
	public Genero edit(@PathVariable Long id) {
		return generoService.findOne(id);
	}

	/**
		O método delete será executado ao realizar uma requisição do tipo HTTP DELTE para a URL
		/genero/{id} sendo que {id} deve ser o código de algum gênero existente no banco de dados .
		O retorno do Método será um Http response com '200 OK' no caso de sucesso ou um
		'400 BAD REQUEST' no caso de erro.
	**/
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
```
### list.html
O arquivo HTML será utilizado para exibir a lista de gêneros para o usuário, e possui também a tela modal que será utilizada para cadastro/edição de gêneros no banco de dados.
```html
<!DOCTYPE html>
<html lang="pt" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{layout/layout-padrao}"> 
<head>
</head>
<body>
	<section layout:fragment="conteudo">
		<div class="page-header">
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-8">
						<h2>Lista de Gêneros</h2>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-4">
						<!-- 
						Ao clicar no link para um novo registro será exibido uma janela modal
						com os campos para o usuário preencher. 
						Foi utilizado o modal da biblioteca Boostrap.
						O atributo data-target indica que será exibido o modal com o id "#modal-form", que
						está localizado no fim deste arquivo.
						-->
						<a class="btn btn-primary" data-toggle="modal" data-target="#modal-form"> 
							<i class="fa fa-plus-square"></i> Novo Registro
						</a>
					</div>
				</div>
			</div>
		</div>

		<div class="container-fluid">
			<div class="table-responsive">
				<!--
					Essa é a tabela que será utilizada para exibir os registros de gênero 
					vindos do GeneroController
				-->
				<table id="tabela" class="table  table-striped  table-bordered  table-hover  table-condensed">
					<thead class="ut-table-header-solid">
						<tr>
							<th>Código</th>
							<th>Nome</th>
							<th>Ações</th>
						</tr>
					</thead>
					<tbody>
						<!--
							O atributo th:each="entity: ${list}" dentro da tag <tr/> será reponsável
							por exibir a lista vinda do GeneroController (método list()).
							entity : será o nome de cada objeto Genero percorrido pela lista.
							${list}: o list é o nome da lista adicionada como atributo no 
									 GeneroController
							Também pode ser obervado que cada linha da tabela terá um id único, formado
							pela expressão 'row_' concatenado com o id do gênero (th:id="'row_' + ${entity.id}").	
						-->
						<tr th:id="'row_' + ${entity.id}" th:each="entity: ${list}">
							<!-- Na primeira coluna é exibido o 'id' do Gênero-->
							<td style="width:150px" class="text-center" th:text="${entity.id}">0</td>
							<!-- Na segunda coluna é exibido o 'nome' do Gênero.
						 	     E também é edicionado um link para a função javascritp edit(),
						 	     passando como parâmetro o 'id' do Gênero. Essa função irá 
						 	     abrir a janela Modal e irá carregar o registro selecionado
						 	     para edição. -->
							<td><a th:href="'javascript:edit('+ ${entity.id} + ');'"
								th:text="${entity.nome}">...</a></td>

							<td style="width:200px" class="text-center">
								<!-- 
									Na última coluna são apresentados os atalhos para Editar e Remover.
									O atalho de editar tem a mesma implementação que o existente na coluna 
									com o 'nome' do Gênero.
									A função remover também é semelhante, sendo relizada uma chamada para
									a função javascript remover(), passando como parâmetro o 'id' do
									Gênero.
								-->
								<a th:href="'javascript:edit('+ ${entity.id} + ');'"
									class="btn btn-primary btn-xs"> <i class="fa fa-edit"></i>
								</a> &nbsp;
								<a th:href="'javascript:remove(' + ${entity.id} + ');'"
									class="btn btn-danger btn-xs"><i class="fa fa-trash"></i></a>
							</td>
						</tr>
						<!--
							Caso a lista venha vazia do GeneroController é exibida a mensagem:
							'Nenhum registro encontrado.'
						-->
						<tr th:if="${list.empty}">
							<td colspan="3">Nenhum registro encontrado.</td>
						</tr>
					</tbody>
				</table>
				<!--
					Na tag 'nav' será montada a lista de paginação de acordo com os dados vindos do
					GeneroController.
				-->
				<nav aria-label="Page navigation example">
					<ul class="pagination" th:if="${list.totalPages > 0}" >
						<li class="page-item" 
							th:classappend="${pageNumber==list.number + 1} ? active" 
							th:each="pageNumber : ${pageNumbers}">
					    	<a class="page-link" 
					    	    th:href="@{/genero(size=${list.size}, page=${pageNumber})}"
					        	th:text="${pageNumber}"></a>
				        </li>
					</ul>
				</nav>
			</div>
		</div>
		<!--
			Na div abaixo está a janela Modal utilizada para o Cadastro de Gênero.
			Essa janela modal é foi desenvolvida utilizando o exmeplo do Boostrap:
			[https://getbootstrap.com.br/docs/4.1/components/modal/](https://getbootstrap.com.br/docs/4.1/components/modal/)
			Na tag form estão o id = 'frm', a action, e o método http que será utilizado para
			envio dos dados ao servidor. Será realizado um POST para URL /genero.
			O envio do formulário será realizado via Ajax ao clicar no botão salvar
			será chamada a função save() presente no arquivo crud-genero.js.
		-->
		<div class="modal modal-form" th:id="modal-form">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h3 id="delModalLabel">Genero</h3>
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">×</button>
					</div>
					<div class="modal-body">
						<form id="frm" name="frm" method="POST" class="form-vertical" th:action="@{/genero}">
							<input type="hidden" id="id" name="id" />
							<div class="form-group">
								<label for="nome">Nome</label> 
								<input type="text" class="form-control" id="nome" name="nome" />
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn" data-dismiss="modal" aria-hidden="true">Cancelar</button>
						<button type="button" onclick="save()" class="btn btn-success">Salvar</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<script type="text/javascript" th:src="@{/js/crud-genero.js}"></script>
	</section>
</body>
</html>
```
### crud-genero.js
O arquivo JavaScript será utilizado para carregar os dados ao editar um registro, para remover um registro e também para salvar um registro.

```javascript
"use strict";
/*
	A function edit() recebe o id do registro como parâmtro e faz uma requisição do tipo HTTP GET
	para a URL /genero/id. Isso irá chamar o método edit() no GeneroController. Trazendo um objeto
	no formato JSON com o registro solicidato. Esses dados são carregados nos campos de id e nome,
	então o modal é exibido.
*/
function edit(id) {
	$.get('/genero/' + id, function(entity, status) {
		$('#id').val(entity.id);
		$('#nome').val(entity.nome);
	});
	$('#modal-form').modal();
}

/*
	Na function save() realizada a chamada HTTP POST para a URL presente no form de cadastro.
*/
function save() {
	var genero = { // cria-se o objeto genero com os atributos id e nome
		id : ($("#id").val() != '' ? $("#id").val() : null),
		nome : $("#nome").val()
	}

	// faz a requisição via ajax (esse método é da biblioteca jQuery)
	$.ajax({
		type : $('#frm').attr('method'), // recupera o valor do atributo method do form, ou seja o método HTTP
		url : $('#frm').attr('action'), // recupera o valor do atributo action do form, ou seja a URL para qual será realizada a requisição
		contentType : 'application/json', // é informado que será enviado via JSON
		data : JSON.stringify(genero), // Cria o JSON com o objeto de Genero
		success : function() {
			swal('Salvo!', 'Registro salvo com sucesso!', 'success'); // em caso de sucesso exibe uma mensagem utilizando a bibliotec SweetAlert
			location.reload(); // então recarrega a página
		},
		error : function() {
			swal('Erro!', 'Falha ao salvar registro!', 'error'); // em caso de erro exibe uma mensagem utilizando a bibliotec SweetAlert
		}
	});// Fim ajax
}

/*
	A function remove recebe como parâmetro o id do registro que será removido e utilizada a biblioteca
	SweetAlert para exibir uma mensagem de confirmação. 
	Caso o usuário clique em 'Confirmar' uma requisição Ajax é realizada utilizando HTTP Delete.
	Em caso de sucesso a linha da tabela contendo o registro que foi removido é apagada. Caso contrário
	será exibida uma mensagem de erro.
*/
function remove(id){
    swal({
        title: "Confirma a remoção do registro?",
         text: "Esta ação não poderá ser desfeita!",
         type: "warning",
         showCancelButton: true,
         confirmButtonColor: "#DD6B55",
         cancelButtonText: "Cancelar",
         confirmButtonText: "Confirmar",
         closeOnConfirm: false
        }, function(){
            var destino = '/genero/' + id;
            $.ajax({
                type: 'DELETE',
                 url: destino,
                 success: function(){
                	 $('#row_' + id).remove();
                     swal('Removido!',
                           'Registro removido com sucesso!',
                           'success');
                 },
                 error: function(){
                     swal('Erro!',
                           'Falha ao remover registro!',
                           'error');
                 }
            });//Fim ajax
        }
    );//FIM SWAWL
} // FIM REMOVE

```
