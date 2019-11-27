"use strict";
$('#frm').submit(function(e){

	e.preventDefault();
    $('#frm').parsley().validate();
    if (!$('#frm').parsley().isValid()){
    	return false;
    }
    
	var formData = new FormData($('#frm')[0]);
	$.ajax({
		type: $('#frm').attr('method'),
		url : $('#frm').attr('action'),
		data: formData,
		async: false,
		cache: false,
		contentType: false,
		processData: false,
		success: function() {
			swal('Salvo!', 'Registro salvo com sucesso!', 'success');
			location.reload();
		}, 
		error: function() {
			swal('Erro!', 'Falha ao salvar o registro', 'error');
		}
	}); //Fim ajax
	
});

function edit(id) {
	$.get('serie/' + id, function(entity, status){
		$('#id').val(entity.id);
		$('#nome').val(entity.nome);
		$('#nota').val(entity.nota);
		$('#resumo').val(entity.resumo);
		$('#genero').val(entity.genero.id);
		$('#produtora').val(entity.produtora.id);
		
		$('#dataEstreia').val( formatDate(entity.dataEstreia) );
		if (entity.dataEncerramento)
			$('#dataEncerramento').val( formatDate(entity.dataEncerramento) );
	});
	$('#modal-form').modal();
}

function formatDate(inputFormat){
	function pad(s){
		return (s < 10) ? '0' + s : s;
	}
	var d = new Date(inputFormat);
	return [ pad(d.getDate()), pad(d.getMonth()+1), d.getFullYear() ].join('/');
}