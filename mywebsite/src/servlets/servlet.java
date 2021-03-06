package servlets;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.Part;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import beans.beanUsuario;
import dao.daoUsuario;
import javax.xml.bind.DatatypeConverter;


@WebServlet("/servlet")
@MultipartConfig
public class servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public servlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// A tag href está passando parametros pela url entao caira no doGet
		// Pega os parametros passados nas opções: Excluir e Editar
		String acao = request.getParameter("acao");
		String user = request.getParameter("user");

		if (acao.equalsIgnoreCase("delete")) {
			daoUsuario daousuario = new daoUsuario();
			daousuario.delete(user);

			RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
			request.setAttribute("usuarios", daousuario.listar());
			view.forward(request, response);

		} else if (acao.equalsIgnoreCase("editar")) {
			beanUsuario bUser = new daoUsuario().consultar(user);
			daoUsuario daousuario = new daoUsuario();

			RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
			request.setAttribute("user", bUser);
			request.setAttribute("usuarios", daousuario.listar());
			view.forward(request, response);

		} else if (acao.equalsIgnoreCase("baixar")) {
			 beanUsuario bUser = new daoUsuario().consultar(user);
			if (bUser != null){
				response.setHeader("Content-Disposition", "attachment;filename=arquivo."
			   + bUser.getContentType().split("\\/")[1]);
				
				/*Converte a base64 do arquivo do banco para byte[]*/
				byte[] imageFotoBytes = Base64.decodeBase64(bUser.getFotoBase64());
				
				/*Coloca os bytes em um objeto de entrada para processar*/
				InputStream is = new ByteArrayInputStream(imageFotoBytes);
				
				/*inicio da resposta para o navegador*/
				int read= 0;
				byte[] bytes = new byte[1024];
				OutputStream os = response.getOutputStream();
				
				
				while ((read = is.read(bytes)) != -1) {
					os.write(bytes, 0, read);
				}
				
				os.flush();
				os.close();				
			}
		}
	}
	//

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// verifica que a ação foi o botão de cancelar e não execulta o restante do
		// código
		String acao = request.getParameter("acao");
		if (acao != null && acao.equalsIgnoreCase("reset")) {
			daoUsuario daousuario = new daoUsuario();

			RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
			request.setAttribute("usuarios", daousuario.listar());

			view.forward(request, response);
		} else {
			daoUsuario daousuario = new daoUsuario();
			String id = request.getParameter("id");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			String nome = request.getParameter("nome");
			String telefone = request.getParameter("telefone");
			String cep = request.getParameter("cep");
			String rua = request.getParameter("rua");
			String bairro = request.getParameter("bairro");
			String cidade = request.getParameter("cidade");
			String uf = request.getParameter("uf");
			String ibge = request.getParameter("ibge");
			
			// Caso haja um objeto retornado do banco para o form através da opção editar
			// Carrega os dados para um beanUsuario para edita-lo
			beanUsuario bUsuario = new beanUsuario();
			// Se o id for diferente de vazio ele converte, caso contrario ele atribui 0
			// ! = diferente ? = then : = else
			bUsuario.setId(!id.isEmpty() ? Long.parseLong(id) : 0);
			bUsuario.setLogin(login);
			bUsuario.setSenha(senha);
			bUsuario.setNome(nome);
			bUsuario.setTelefone(telefone);
			bUsuario.setCep(cep);
			bUsuario.setRua(rua);
			bUsuario.setBairro(bairro);
			bUsuario.setCidade(cidade);
			bUsuario.setUf(uf);
			bUsuario.setIbge(ibge);

			try {
				
				/* Inicio File upload de imagems e pdf */
				if (ServletFileUpload.isMultipartContent(request)) {

					Part imagemFoto = request.getPart("foto");
					
					//Se imagemfoto nao for nulo e tiver uma foto escolhida
					if(imagemFoto != null && imagemFoto.getInputStream().available() > 0 ) {		
					
						byte[] bytesImagem = converteStremParabyte(imagemFoto.getInputStream());
						
						String fotoBase64 = Base64.encodeBase64String(bytesImagem);
						
						bUsuario.setFotoBase64(fotoBase64);
						bUsuario.setContentType(imagemFoto.getContentType());
						
						/*Inicio miniatura imagem*/
						/*Transforma enum bufferedImage*/
						byte[] imageByteDecode = Base64.decodeBase64(fotoBase64);
						BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageByteDecode));
						
						/*Pega o tipo da imagem*/
						int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB: bufferedImage.getType();
						
						/*Cria imagem em miniatura*/
						BufferedImage resizedImage = new BufferedImage(100, 100, type);
						Graphics2D g = resizedImage.createGraphics();
						g.drawImage(bufferedImage, 0, 0, 100, 100, null);
						g.dispose();
						
						/*Escrever imagem novamente*/
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(resizedImage, "png", baos);
						
						String miniaturaBase64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
						 bUsuario.setFotoMinBase64(miniaturaBase64);
						 
						/*Fim miniatura imagem*/
					}else {//caso nao tenha uma imagem escolhida ele pega a imagem do banco para update
						bUsuario.setFotoBase64(daousuario.consultarBase64(bUsuario.getId()));
						bUsuario.setContentType(daousuario.consultarContent(bUsuario.getId()));
						bUsuario.setFotoMinBase64(daousuario.consultarMinBase64(bUsuario.getId()));
					}
				}

				/* FIM File upload de imagems e pdf */

				// Se existir um usuario no banco
				if (id == null || id.isEmpty() && !daousuario.validarLogin(login)) {
					request.setAttribute("msg", "Usuário já cadastrado");
					request.setAttribute("user", bUsuario);
				} // Se nao existir um usuario no banco
				else if (id == null || id.isEmpty() && daousuario.validarLogin(login)) {
					daousuario.inserir(bUsuario);
					request.setAttribute("msg", "Usuário Cadastrado com Sucesso");

				} else if (id != null || !id.isEmpty()) {// Se o campo id for diferente de nulo ou vazio, tem um objeto
															// para ser editado
					if (!daousuario.validarLoginUpdate(login, id)) {// Se existir um usuario no banco ele não atualiza
						request.setAttribute("msg", "Usuário já cadastrado");
						request.setAttribute("user", bUsuario);
					} else {// Caso contrário se não existir um usuario no banco ele atualiza
						daousuario.atualizar(bUsuario);
						request.setAttribute("msg", "Usuário Atualizado com Sucesso");
					}
				}

				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("usuarios", daousuario.listar());
				view.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/* Converte a entrada de fluxo de dados da imagem para byte[] */
	private byte[] converteStremParabyte(InputStream imagem) throws Exception {

		@SuppressWarnings("resource")
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = imagem.read();
		while (reads != -1) {
			baos.write(reads);
			reads = imagem.read();
		}

		return baos.toByteArray();

	}
}
