package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.daoLogin;
import dao.daoUsuario;

@WebServlet("/sLogin")
public class sLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public sLogin() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			daoLogin dLogin = new daoLogin();
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			if (login != null && !login.isEmpty() && senha != null && !senha.isEmpty()) {
				if (dLogin.validarLogin(login, senha)) {
					RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
					daoUsuario daousuario = new daoUsuario();
					request.setAttribute("usuarios", daousuario.listar());
					view.forward(request, response);
				} else {
					RequestDispatcher view = request.getRequestDispatcher("/acessonegado.jsp");
					view.forward(request, response);
				}
			}else {
				RequestDispatcher view = request.getRequestDispatcher("/index.jsp");
				view.forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
