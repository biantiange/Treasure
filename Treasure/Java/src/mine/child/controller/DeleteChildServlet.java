package mine.child.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import mine.child.service.ChildService;



/**
 * Servlet implementation class DeleteChildServlet
 */
@WebServlet("/DeleteChildServlet")
public class DeleteChildServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteChildServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String name=request.getParameter("id");
		int id=0;
		boolean delete=false;
		if(name!=null){
			id=Integer.parseInt(name);
			delete = new ChildService().dropChild(id);
		}
		
		//(buyer_id, seller_id, cake_id, count, 1, address, tel, cake_name,cake_price);
		if (delete) {
			System.out.println("删除："+id+"....：succeed");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		
		
		doGet(request, response);
	}

}
