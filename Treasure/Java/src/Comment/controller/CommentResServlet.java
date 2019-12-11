package Comment.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import Comment.dao.CommentDao;
import entity.Comment;

/**
 * Servlet implementation class CommentAddServlet
 */
@WebServlet("/CommentResServlet")
public class CommentResServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentResServlet() {
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
		OutputStream out = response.getOutputStream();
		//获得数据
		InputStream inputStream = request.getInputStream();
		byte[] bs = new byte[255];
		int len = inputStream.read(bs);
		String param = new String(bs,0,len);
		JSONObject object = new JSONObject(param);
		int postId = object.getInt("postId");
		int commentatorId = object.getInt("commentatorId");
		int resComId = object.getInt("resComId");
		int responderId = object.getInt("responderId");
		String content = object.getString("content");
		String time = object.getString("time");
		
		Comment comment = new Comment();
		comment.setResComId(resComId);
		comment.setCommentatorId(commentatorId);
		comment.setContent(content);
		comment.setPostId(postId);
		comment.setResponderId(responderId);
		comment.setTime(Timestamp.valueOf(time));
		
		System.out.println(Timestamp.valueOf(time));//打印传过来的时间
		
		int add = new CommentDao().AddComment(comment);
		
		if (add>0) {
			System.out.println("succeed!");
		}
		out.close();
		inputStream.close();
	}

}
