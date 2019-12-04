package Post.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.sun.jmx.snmp.Timestamp;

import Post.service.PostServicelmpl;
import entity.Post;

/**
 * Servlet implementation class PostAddServlet
 */
@WebServlet("/PostAddServlet")
public class PostAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostAddServlet() {
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		InputStream inputStream = request.getInputStream();
		byte[] bs = new byte[255];
		int len = inputStream.read(bs);
		String param = new String(bs,0,len);
		JSONObject object = new JSONObject(param);
		Post post = new Post();
		post.setContent(object.getString("content"));
		post.setPosterId(object.getInt("postId"));
		post.setPraiseCount(object.getInt("praiseCount"));
		post.setTime( (Timestamp) object.get("time"));
		boolean add = new PostServicelmpl().addPost(post);
		if (add) {
			System.out.println("succeed");
		}
	}

}
