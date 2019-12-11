package Praise.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import Praise.dao.PraiseDao;

/**
 * Servlet implementation class PraiseAddServlet
 */
@WebServlet("/PraiseAddServlet")
public class PraiseAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PraiseAddServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		InputStream inputStream = request.getInputStream();
		byte[] bs = new byte[255];
		int len = inputStream.read(bs);
		String param = new String(bs,0,len);
		JSONObject object = new JSONObject(param);
		int praiserId = object.getInt("praiserId");
		int postId = object.getInt("postId");
		int add = new PraiseDao().savePraise(praiserId, postId);
		if (add>0) {
			System.out.println("succeed");
		}
	}

}
