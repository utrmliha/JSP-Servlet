<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
    <!-- Adicionando JQuery -->
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"
    integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
    crossorigin="anonymous"></script>
</head>
<body>
<h1>Insira o cep</h1>
      <form method="get" action=".">
        <label>Cep:
        <input name="cep" type="text" id="cep" onblur="consultarCep()"/></label><br />
        <label>Rua:
        <input name="rua" type="text" id="rua" /></label><br />
        <label>Bairro:
        <input name="bairro" type="text" id="bairro" /></label><br />
        <label>Cidade:
        <input name="cidade" type="text" id="cidade" /></label><br />
        <label>Estado:
        <input name="uf" type="text" id="uf" size="2" /></label><br />
        <label>IBGE:
        <input name="ibge" type="text" id="ibge" /></label><br />
      </form>
	
<script>
	function consultarCep(){
		var cep = $("#cep").val();//armazena o cep do formulario

		//Consulta o webservice viacep.com.br/
        $.getJSON("https://viacep.com.br/ws/"+ cep +"/json/?callback=?", function(dados) {

            if (!("erro" in dados)) {
                //Atualiza os campos com os valores da consulta.
                $("#rua").val(dados.logradouro);
                $("#bairro").val(dados.bairro);
                $("#cidade").val(dados.localidade);
                $("#uf").val(dados.uf);
                $("#ibge").val(dados.ibge);
            } //end if.
            else {
                //CEP pesquisado não foi encontrado.
                limpa_formulário_cep();
                alert("CEP não encontrado.");
            }
        });
	}
</script>
</body>
</html>
