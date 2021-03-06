package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexao.SingleConnection;

public class daoLogin {
	Connection connection;
	
	public daoLogin(){
		connection = SingleConnection.getConnection();
	}
	
	public boolean validarLogin(String login, String senha) throws SQLException {
		String sql="select * from usuarios where login='"+login+"' and senha ='"+senha+"'";
		PreparedStatement select =  connection.prepareStatement(sql);
		ResultSet result = select.executeQuery();
		if(result.next()) {
			return true;
		}else {
			return false;
		}
	}

}
