package mine.child.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import mine.child.service.ChildService;

import java.util.List;
import java.util.Map;




/**
 * Servlet implementation class ChildServlet
 */
@WebServlet("/ChildServlet")
public class ChildServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChildServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
	
		String pi=request.getParameter("parentId");
	
		OutputStream out = response.getOutputStream();
        JSONArray jsonArray = new JSONArray();
        if(pi!=null){
        List<Map<String, Object>> list = new ChildService().listChilds(pi);
        for(Map<String,Object> map:list) {
        	JSONObject jsonObject = new JSONObject();
        	jsonObject.put("id", map.get("id").toString());
        	jsonObject.put("nickName", map.get("name"));
        	jsonObject.put("birthday", map.get("age").toString());
        	jsonObject.put("parentId", map.get("parentId").toString());
        	jsonObject.put("imgPath", map.get("headerPath"));
        	
        	jsonArray.put(jsonObject);
        	System.out.println(jsonObject.toString());
        }
        out.write(jsonArray.toString().getBytes());
        }else {
			System.out.print("失败");
			
		}
        out.close();
        
      
      
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
