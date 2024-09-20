package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.ProdutoDAO;
import model.Produto;
import spark.Request;
import spark.Response;


public class ProdutoService {

	private ProdutoDAO produtoDAO = new ProdutoDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_DESCRICAO = 2;
	private final int FORM_ORDERBY_PRECO = 3;
	
	
	public ProdutoService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Produto(), FORM_ORDERBY_DESCRICAO);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Produto(), orderBy);
	}

	
	public void makeForm(int tipo, Produto produto, int orderBy) {
		String nomeArquivo = "form.html";
		StringBuilder formBuilder = new StringBuilder();
	
		try (Scanner entrada = new Scanner(new File(nomeArquivo))) {
			while (entrada.hasNext()) {
				formBuilder.append(entrada.nextLine()).append("\n");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	
		StringBuilder umProdutoBuilder = new StringBuilder();
		
		// Adding "Novo Produto" button for non-insert forms
		if (tipo != FORM_INSERT) {
			umProdutoBuilder.append("\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">")
						   .append("\t\t<tr>")
						   .append("\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/produto/list/1\">Novo Produto</a></b></font></td>")
						   .append("\t\t</tr>")
						   .append("\t</table>")
						   .append("\t<br>");
		}
	
		if (tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			// Preparing for form insertion or update
			String action = tipo == FORM_INSERT ? "/produto/insert" : "/produto/update/" + produto.getID();
			String name = tipo == FORM_INSERT ? "Inserir Produto" : "Atualizar Produto (ID " + produto.getID() + ")";
			String descricao = tipo == FORM_INSERT ? "leite, pão, ..." : produto.getDescricao();
			String buttonLabel = tipo == FORM_INSERT ? "Inserir" : "Atualizar";
	
			// Building the form for insert/update
			umProdutoBuilder.append("\t<form class=\"form--register\" action=\"").append(action).append("\" method=\"post\" id=\"form-add\">")
						   .append("\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">")
						   .append("\t\t<tr>")
						   .append("\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;").append(name).append("</b></font></td>")
						   .append("\t\t</tr>")
						   .append("\t\t<tr><td colspan=\"3\" align=\"left\">&nbsp;</td></tr>")
						   .append("\t\t<tr>")
						   .append("\t\t\t<td>&nbsp;Descrição: <input class=\"input--register\" type=\"text\" name=\"descricao\" value=\"").append(descricao).append("\"></td>")
						   .append("\t\t\t<td>Preco: <input class=\"input--register\" type=\"text\" name=\"preco\" value=\"").append(produto.getPreco()).append("\"></td>")
						   .append("\t\t\t<td>Quantidade: <input class=\"input--register\" type=\"text\" name=\"quantidade\" value=\"").append(produto.getQuantidade()).append("\"></td>")
						   .append("\t\t</tr>")
						   .append("\t\t<tr>")
						   .append("\t\t\t<td>&nbsp;Data de fabricação: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\"").append(produto.getDataFabricacao()).append("\"></td>")
						   .append("\t\t\t<td>Data de validade: <input class=\"input--register\" type=\"text\" name=\"dataValidade\" value=\"").append(produto.getDataValidade()).append("\"></td>")
						   .append("\t\t\t<td align=\"center\"><input type=\"submit\" value=\"").append(buttonLabel).append("\" class=\"input--main__style input--button\"></td>")
						   .append("\t\t</tr>")
						   .append("\t</table>")
						   .append("\t</form>");
		} else if (tipo == FORM_DETAIL) {
			// Building the detail form
			umProdutoBuilder.append("\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">")
						   .append("\t\t<tr>")
						   .append("\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Produto (ID ").append(produto.getID()).append(")</b></font></td>")
						   .append("\t\t</tr>")
						   .append("\t\t<tr><td colspan=\"3\" align=\"left\">&nbsp;</td></tr>")
						   .append("\t\t<tr>")
						   .append("\t\t\t<td>&nbsp;Descrição: ").append(produto.getDescricao()).append("</td>")
						   .append("\t\t\t<td>Preco: ").append(produto.getPreco()).append("</td>")
						   .append("\t\t\t<td>Quantidade: ").append(produto.getQuantidade()).append("</td>")
						   .append("\t\t</tr>")
						   .append("\t\t<tr>")
						   .append("\t\t\t<td>&nbsp;Data de fabricação: ").append(produto.getDataFabricacao()).append("</td>")
						   .append("\t\t\t<td>Data de validade: ").append(produto.getDataValidade()).append("</td>")
						   .append("\t\t\t<td>&nbsp;</td>")
						   .append("\t\t</tr>")
						   .append("\t</table>");
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
	
		formBuilder = new StringBuilder(formBuilder.toString().replaceFirst("<UM-PRODUTO>", umProdutoBuilder.toString()));
	
		// Creating the product list
		StringBuilder listBuilder = new StringBuilder("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">")
			.append("\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Produtos</b></font></td></tr>\n")
			.append("\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n")
			.append("\n<tr>")
			.append("\t<td><a href=\"/produto/list/").append(FORM_ORDERBY_ID).append("\"><b>ID</b></a></td>\n")
			.append("\t<td><a href=\"/produto/list/").append(FORM_ORDERBY_DESCRICAO).append("\"><b>Descrição</b></a></td>\n")
			.append("\t<td><a href=\"/produto/list/").append(FORM_ORDERBY_PRECO).append("\"><b>Preço</b></a></td>\n")
			.append("\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n")
			.append("\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n")
			.append("\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n")
			.append("</tr>\n");
	
		// Retrieving ordered products
		List<Produto> produtos;
		if (orderBy == FORM_ORDERBY_ID) {
			produtos = produtoDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_DESCRICAO) {
			produtos = produtoDAO.getOrderByDescricao();
		} else if (orderBy == FORM_ORDERBY_PRECO) {
			produtos = produtoDAO.getOrderByPreco();
		} else {
			produtos = produtoDAO.get();
		}
	
		int i = 0;
		for (Produto p : produtos) {
			String bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			listBuilder.append("\n<tr bgcolor=\"").append(bgcolor).append("\">\n")
					   .append("\t<td>").append(p.getID()).append("</td>\n")
					   .append("\t<td>").append(p.getDescricao()).append("</td>\n")
					   .append("\t<td>").append(p.getPreco()).append("</td>\n")
					   .append("\t<td align=\"center\" valign=\"middle\"><a href=\"/produto/").append(p.getID()).append("\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n")
					   .append("\t<td align=\"center\" valign=\"middle\"><a href=\"/produto/update/").append(p.getID()).append("\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n")
					   .append("\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmProductDelete('").append(p.getID()).append("', '").append(p.getDescricao()).append("', '").append(p.getPreco()).append("');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n")
					   .append("</tr>\n");
		}
	
		listBuilder.append("</table>");
		formBuilder = new StringBuilder(formBuilder.toString().replaceFirst("<LISTAR-PRODUTO>", listBuilder.toString()));
		
		form = formBuilder.toString();
	}
	
	
	
	public Object insert(Request request, Response response) {
		String descricao = request.queryParams("descricao");
		float preco = Float.parseFloat(request.queryParams("preco"));
		int quantidade = Integer.parseInt(request.queryParams("quantidade"));
		LocalDateTime dataFabricacao = LocalDateTime.parse(request.queryParams("dataFabricacao"));
		LocalDate dataValidade = LocalDate.parse(request.queryParams("dataValidade"));
		
		String resp = "";
		
		Produto produto = new Produto(-1, descricao, preco, quantidade, dataFabricacao, dataValidade);
		
		if(produtoDAO.insert(produto) == true) {
            resp = "Produto (" + descricao + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Produto (" + descricao + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Produto produto = (Produto) produtoDAO.get(id);
		
		if (produto != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, produto, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Produto " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Produto produto = (Produto) produtoDAO.get(id);
		
		if (produto != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, produto, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Produto " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Produto produto = produtoDAO.get(id);
        String resp = "";       

        if (produto != null) {
        	produto.setDescricao(request.queryParams("descricao"));
        	produto.setPreco(Float.parseFloat(request.queryParams("preco")));
        	produto.setQuantidade(Integer.parseInt(request.queryParams("quantidade")));
        	produto.setDataFabricacao(LocalDateTime.parse(request.queryParams("dataFabricacao")));
        	produto.setDataValidade(LocalDate.parse(request.queryParams("dataValidade")));
        	produtoDAO.update(produto);
        	response.status(200); // success
            resp = "Produto (ID " + produto.getID() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Produto (ID \" + produto.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = produtoDAO.get(id);
        String resp = "";       

        if (produto != null) {
            produtoDAO.delete(id);
            response.status(200); // success
            resp = "Produto (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Produto (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}
