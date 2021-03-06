<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/_css/cadastro.css" />
    
</head>
<body>
		<center>
		<h1>Cadastro de Usuário</h1>
		<h3 style="color:orange">${msg}</h3>
	    </center>
<div class="container" align="center">
	<!-- No "enctype="multipart/form-data"" Estamos dizendo que o formulario enviará tambem arquivos-->
	<form action="servlet" method="post" id="formulario"
		onsubmit="return validarCampos()? true : false;"
		enctype="multipart/form-data">
			<table>
				<tr>
					<td>ID:</td>
					<td><input type="text" readonly="readonly" id="id" name="id" value="${user.id}" /></td>
					<td>CEP:</td>
					<td><input type="text" id="cep" name="cep" value="${user.cep}" onblur="consultarCep()" value="${user.cep}" placeholder ="Insira seu Cep" /></td>
				</tr>
				<tr>
					<td>Login:</td>
					<td><input type="text" id="login" name="login" value="${user.login}" placeholder ="Informe um Login" /></td>
					<td>Rua:</td>
					<td><input type="text" id="rua" name="rua" value="${user.rua}" /></td>
				</tr>
				<tr>
					<td>Senha:</td>
					<td><input type="password" id="senha" name="senha" value="${user.senha}" placeholder ="Digite sua Senha" /></td>
					<td>Bairro:</td>
					<td><input type="text" id="bairro" name="bairro" value="${user.bairro}" /></td>
				</tr>
				<tr>
					<td>Nome:</td>
					<td><input type="text" id="nome" name="nome" value="${user.nome}" placeholder ="Informe o nome"/></td>
					<td>Cidade:</td>
					<td><input type="text" id="cidade" name="cidade" value="${user.cidade}" /></td>
				</tr>
				<tr>
					<td>Telefone:</td>
					<td><input type="text" id="telefone" name="telefone" value="${user.telefone}" placeholder ="Coloque seu Telefone(DDD) x xxxx xxxx" /></td>
					<td>Estado:</td>
					<td><input type="text" id="uf" name="uf" value="${user.uf}" /></td>
				</tr>
				<tr>
					<td>IBGE:</td>
					<td><input type="text" id="ibge" name="ibge" value="${user.ibge}" /></td>
				</tr>
				<tr>
				<td>Foto:</td><!-- input oculto para armazenar o base64 em tempo de execução para manter na hora de atualizar -->
				<td><input type="file" name="foto" ></td>
				</tr>
					
				<tr>
				<td></td><!-- No evento Onclick do botao cancelar ele pegará o formulario, redirencionará o action para a servlet e passará um parametro -->
				<td><input type="submit" value="Salvar" />  <input type="submit" value="Cancelar" onclick="document.getElementById('formulario').action = 'servlet?acao=reset'" /></td>
			</tr>
		</table>
	</form>
</div>
<div class="container" align="center">
		<table class="responsive-table">
			<caption>Usuários Cadastrados</caption>
			<tr>
				<th>Id</th>
				<th>Nome</th>
				<th>Foto</th>
				<th>Login</th>
				<th>Senha</th>
				<th>Cep</th>
				<th>Rua</th>
				<th>Bairro</th>
				<th>Cidade</th>
				<th>Estado</th>
				<th>IBGE</th>
				<th>Ações</th>
			</tr>
			<c:forEach items="${usuarios}" var="user">
				<tr>
				<td><c:out value="${user.id}"></c:out></td>
				<td><c:out value="${user.nome}"></c:out></td>
					
					<!-- se contem algo armazenado na base64 ele coloca a imagem -->
					<c:if test="${user.fotoMinBase64.isEmpty() == false }">
						<td><a href="servlet?acao=baixar&user=${user.id}">
						<img src='<c:out value="${user.fotoMinBase64}"></c:out>' width="32" height="32"></a></td>
					</c:if><!-- caso seja nulo ou seja, usuario nao possua imagem, ele coloca uma imagem padrao -->
					<c:if test="${user.fotoMinBase64 == null }">
						<td><img src="_pictures/semfoto.png" width="32" height="32"></td>
					</c:if>
						
				<td><c:out value="${user.login}"></c:out></td>
				<td><c:out value="${user.telefone}"></c:out></td>
				<td><c:out value="${user.cep}"></c:out></td>
				<td><c:out value="${user.rua}"></c:out></td>
				<td><c:out value="${user.bairro}"></c:out></td>
				<td><c:out value="${user.cidade}"></c:out></td>
				<td><c:out value="${user.uf}"></c:out></td>
				<td><c:out value="${user.ibge}"></c:out></td>
				<td><a href="servlet?acao=delete&user=${user.id}">Excluir</a></td>
				<td><a href="servlet?acao=editar&user=${user.id}">Editar</a></td>
				</tr>
			</c:forEach>
		</table>
	</div>

	<script type="text/javascript">
		function validarCampos() {
			if (document.getElementById("login").value == '') {
				alert("Informe o login");
				return false;
			} else if (document.getElementById("senha").value == '') {
				alert("Informe a senha");
				return false;
			} else {
				return true;
			}
		}
		
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