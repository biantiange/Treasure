package Post.controller;

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

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import Post.service.PostServicelmpl;
import entity.Post;

/**
 * Servlet implementation class PostListServlet
 */
@WebServlet("/PostListServlet")
public class PostListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostListServlet() {
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
		InputStream iStream = request.getInputStream();
		iStream.close();
		OutputStream out = response.getOutputStream();
		JSONArray jsonArray = new JSONArray();
        List<Map<String, Object>> list = new PostServicelmpl().listPost();
        for(Map<String,Object> map:list) {
        	//post
        	JSONObject jsonObject = new JSONObject();
        	jsonObject.put("id", map.get("id"));
        	jsonObject.put("content", map.get("content"));
        	jsonObject.put("time", map.get("time"));
        	jsonObject.put("praiseCount", map.get("praiseCount"));
        	
        	//Poster
        	map.get("posterId");
        	
        	//img
        	
        	//3_comment
        	
        	//isPraise
        	
        	
        	jsonArray.put(jsonObject);
        	
        }
        out.write(jsonArray.toString().getBytes());
        out.flush();
        out.close();
	}

}
