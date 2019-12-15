package mine.child.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.json.JSONArray;
import org.json.JSONObject;

import mine.child.service.ChildService;



/**
 * Servlet implementation class EditChildServlet
 */
@WebServlet("/EditChildServlet")
public class EditChildServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditChildServlet() {
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
		System.out.println("开始修改操作");
		String id = request.getParameter("id");//.getInt("id");
		String parentId = request.getParameter("parentId");//object.getInt("parentId");
		String birthday = request.getParameter("birthday");//object.getInt("birthday");
		String name=request.getParameter("name");//object.getString("nickname");
		String imgPath =request.getParameter("imgPath");// object.getString("imgPath");
		System.out.println(id);
		System.out.println(parentId);
		System.out.println(birthday);
		System.out.println(name);
		System.out.println(imgPath);
		
	if(id!=null&&parentId!=null&&birthday!=null&&name!=null&&imgPath!=null){
			int i=Integer.parseInt(id);
			int pi=Integer.parseInt(parentId);
			
			boolean edit = new ChildService().editChild(i,name,birthday,imgPath,pi);
		if(edit==true){
			response.getWriter().append("修改成功");
		}
		else {
			response.getWriter().append("修改失败");
		}
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
