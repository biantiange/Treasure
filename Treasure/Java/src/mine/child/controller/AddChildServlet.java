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
 * Servlet implementation class AddChildServlet
 */
@WebServlet("/AddChildServlet")
public class AddChildServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddChildServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("zengjia");
		request.setCharacterEncoding("UTF-8");
	
	/*	InputStream inputStream = request.getInputStream();*/
		
		/*JSONObject object = new JSONObject();*/
		String parentId = request.getParameter("parentId");//object.getInt("parentId");
		String birthday = request.getParameter("birthday");//object.getInt("birthday");
		String name=request.getParameter("name");//object.getString("nickname");
		String imgPath =request.getParameter("imgPath");// object.getString("imgPath");
		
		System.out.println(parentId);
		System.out.println(birthday);
		System.out.println(name);
		System.out.println(imgPath);
		
		if(parentId!=null&&birthday!=null&&name!=null&&imgPath!=null){
			
			int pi=Integer.parseInt(parentId);
			boolean add = new ChildService().addChild(pi,birthday,imgPath,name);
			if (add) {
				response.getWriter().append("succeed");
				System.out.println("succeed");
			}
		}
		
		
		//(buyer_id, seller_id, cake_id, count, 1, address, tel, cake_name,cake_price);
		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
