package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.beanUsuario;
import conexao.SingleConnection;

public class daoUsuario {
	Connection connection;
	public daoUsuario() {
		connection = SingleConnection.getConnection();
	}
	
	public String consultarMinBase64(Long id) {
		try {
		String sql ="select fotominbase64 from usuarios where id ="+id;
		PreparedStatement select = connection.prepareStatement(sql);
		ResultSet resultSet = select.executeQuery();
		while(resultSet.next()) {
			return resultSet.getString("fotominbase64");
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public String consultarBase64(Long id) {
		try {
		String sql ="select fotobase64 from usuarios where id ="+id;
		PreparedStatement select = connection.prepareStatement(sql);
		ResultSet resultSet = select.executeQuery();
		while(resultSet.next()) {
			return resultSet.getString("fotobase64");
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public String consultarContent(Long id) {
		try {
		String sql ="select contenttype from usuarios where id ="+id;
		PreparedStatement select = connection.prepareStatement(sql);
		ResultSet resultSet = select.executeQuery();
		while(resultSet.next()) {
			return resultSet.getString("contenttype");
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public List<beanUsuario> listar(){
		List<beanUsuario> users=new ArrayList<beanUsuario>();
		try {
		String sql = "select * from usuarios";
		PreparedStatement select=connection.prepareStatement(sql);
		ResultSet resultSet = select.executeQuery();
		
		while(resultSet.next()) {
			beanUsuario user = new beanUsuario();
			user.setId(resultSet.getLong("id"));
			user.setLogin(resultSet.getString("login"));
			user.setSenha(resultSet.getString("senha"));
			user.setNome(resultSet.getString("nome"));
			user.setTelefone(resultSet.getString("telefone"));
			user.setCep(resultSet.getString("cep"));
			user.setRua(resultSet.getString("rua"));
			user.setBairro(resultSet.getString("bairro"));
			user.setCidade(resultSet.getString("cidade"));
			user.setUf(resultSet.getString("uf"));
			user.setIbge(resultSet.getString("ibge"));
			//user.setContentType(resultSet.getString("contenttype"));
			//user.setFotoBase64(resultSet.getString("fotobase64"));
			user.setFotoMinBase64(resultSet.getString("fotominbase64"));
			
			users.add(user);			
		}
		return users;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void delete(String id) {
		try {
		String sql="delete from usuarios where id='"+id+"'";
		PreparedStatement delete=connection.prepareStatement(sql);
		delete.execute();
		connection.commit();
		}catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void inserir(beanUsuario user){
		try {
		String sql="INSERT INTO usuarios(login, senha, nome, telefone, cep, rua, bairro, cidade, uf, ibge, "
				  + "contenttype, fotobase64, fotominbase64) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement insert=connection.prepareStatement(sql);
		insert.setString(1, user.getLogin());
		insert.setString(2, user.getSenha());
		insert.setString(3, user.getNome());
		insert.setString(4, user.getTelefone());
		insert.setString(5, user.getCep());
		insert.setString(6, user.getRua());
		insert.setString(7, user.getBairro());
		insert.setString(8, user.getCidade());
		insert.setString(9, user.getUf());
		insert.setString(10, user.getIbge());
		insert.setString(11, user.getContentType());
		insert.setString(12, user.getFotoBase64());
		insert.setString(13, user.getFotoMinBase64());
		
		insert.execute();
		connection.commit();
		}catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public beanUsuario consultar(String id){
		try {
		String sql="select * from usuarios where id='"+id+"'";
		PreparedStatement select =  connection.prepareStatement(sql);
		ResultSet resultSet = select.executeQuery();
		if(resultSet.next()) {
			beanUsuario bUser = new beanUsuario();
			bUser.setId(resultSet.getLong("id"));
			bUser.setLogin(resultSet.getString("login"));
			bUser.setSenha(resultSet.getString("senha"));
			bUser.setNome(resultSet.getString("nome"));
			bUser.setTelefone(resultSet.getString("telefone"));
			bUser.setCep(resultSet.getString("cep"));
			bUser.setRua(resultSet.getString("rua"));
			bUser.setBairro(resultSet.getString("bairro"));
			bUser.setCidade(resultSet.getString("cidade"));
			bUser.setUf(resultSet.getString("uf"));
			bUser.setIbge(resultSet.getString("ibge"));
			bUser.setContentType(resultSet.getString("contenttype"));
			bUser.setFotoBase64(resultSet.getString("fotobase64"));
			bUser.setFotoMinBase64(resultSet.getString("fotominbase64"));
			
			return bUser;
		}
		return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean validarLogin(String login) throws SQLException {
	/*Conta até 1, quantos usuarios tem login igual ao login passado no parametro do metodo
	Se tiver algum login no banco ele contará 1, se nao tiver ele contará 0
	O valor do count ficara em uma variável chamada qtd*/
		String sql="select count(1) as qtd from usuarios where login='"+login+"'";
		PreparedStatement select =  connection.prepareStatement(sql);
		ResultSet result = select.executeQuery();
		if(result.next()) {
			//retornar, valor de qtd se for menor ou igual a 0 será true
			return result.getInt("qtd") <=0;
		}
		return false;
	}
	
	//Para nao deixar a modificação de um usuario com o login igual á um existente no banco
	public boolean validarLoginUpdate(String login, String id) throws SQLException {
		String sql="select count(1) as qtd from usuarios where login='"+login+"' and id <> "+id;
		PreparedStatement select =  connection.prepareStatement(sql);
		ResultSet result = select.executeQuery();
		if(result.next()) {
			//retornar, valor de qtd se for menor ou igual a 0 será true
			return result.getInt("qtd") <=0;
		}
		return false;
	}
	
	public void atualizar(beanUsuario user){
		try {
		String sql="UPDATE usuarios SET login=?, senha=?, nome=?, telefone=?, cep=?, rua=?, bairro=?, cidade=?,"
				 + " uf=?, ibge=?, contenttype=?, fotobase64=?, fotominbase64=? WHERE id = "+user.getId();
		PreparedStatement update = connection.prepareStatement(sql);
		update.setString(1, user.getLogin());
		update.setString(2, user.getSenha());
		update.setString(3, user.getNome());
		update.setString(4, user.getTelefone());
		update.setString(5, user.getCep());
		update.setString(6, user.getRua());
		update.setString(7, user.getBairro());
		update.setString(8, user.getCidade());
		update.setString(9, user.getUf());
		update.setString(10, user.getIbge());
		update.setString(11, user.getContentType());
		update.setString(12, user.getFotoBase64());
		update.setString(13, user.getFotoMinBase64());
		
		update.executeUpdate();
		connection.commit();
		}catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
	}

}
