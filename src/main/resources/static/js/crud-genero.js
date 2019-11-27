"use strict";
$('#frm').submit(function(e){

	e.preventDefault();
    $('#frm').parsley().validate();
    
    if (!$('#frm').parsley().isValid()){
    	return false;
    }

    var genero = { // cria-se o objeto genero com os atributos id e nome
		id : ($("#id").val() != '' ? $("#id").val() : null),
		nome : $("#nome").val()
	}
	    
	// envia o objeto como json para o server
	$.ajax({
		type : $('#frm').attr('method'),
		url : $('#frm').attr('action'),
		contentType : 'application/json', 
		data : JSON.stringify(genero),
		success : function() {
			swal('Salvo!', 'Registro salvo com sucesso!', 'success');
			location.reload();
		},
		error : function() {
			swal('Erro!', 'Falha ao salvar registro!', 'error');
		}
	});// Fim ajax
});

function edit(id) {
	$.get('/genero/' + id, function(entity, status) {
		$('#id').val(entity.id);
		$('#nome').val(entity.nome);
	});
	$('#modal-form').modal();
}

"use strict";
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
            });// Fim ajax
        }
    );// FIM SWAWL
} // FIM REMOVE
