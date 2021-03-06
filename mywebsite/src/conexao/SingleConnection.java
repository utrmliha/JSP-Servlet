package conexao;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnection {
	private static Connection connection = null;
	private static String banco="jdbc:postgresql://localhost:5432/postgres?autoReconnect=true";
	private static String user="postgres";
	private static String pass="admin";
	
	static {
		conectar();
	}
	
	public SingleConnection() {
		conectar();
	}
	
	public static void conectar() {
		try {
			if(connection == null) {
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(banco, user, pass);
				connection.setAutoCommit(false);
				System.out.println("Conexao realizada");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection() {
		return connection;
	}
}