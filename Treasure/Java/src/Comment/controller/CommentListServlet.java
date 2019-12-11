package Comment.controller;

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

import Comment.dao.CommentDao;
import User.dao.UserDao;
import entity.Comment;
import entity.User;

/**
 * Servlet implementation class CommentListServlet
 */
@WebServlet("/CommentListServlet")
public class CommentListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentListServlet() {
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
		//获得帖子Id
		InputStream inputStream = request.getInputStream();
		byte[] bs = new byte[255];
		int len = inputStream.read(bs);
		String param = new String(bs,0,len);
		JSONObject object = new JSONObject(param);
		int postId = object.getInt("postId");
		
		JSONArray jsonArray = new JSONArray();
		List<Map<String,Object>> list = new CommentDao().findAll(postId);
		for(Map<String, Object> map:list) {
			//comment
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", map.get("id"));
			jsonObject.put("time", map.get("time"));
			jsonObject.put("content", map.get("content"));
			//commentator
			User commentator = new UserDao().findById((int)map.get("commentatorId"));
			jsonObject.put("commentatorId", map.get("commentatorId"));
			jsonObject.put("headerPath_c", commentator.getHeaderPath());
			jsonObject.put("nickName_c", commentator.getNickName());
			//responderId
			if ((int)map.get("responderId")!=0) {
				User responderId = new UserDao().findById((int)map.get("responderId"));
				
	//			jsonObject.put("headerPath_r", responderId.getHeaderPath());
				jsonObject.put("nickName_r", responderId.getNickName());
				//resComid
				Comment resComment = new CommentDao().findById((int)map.get("resComId"));
	//			User resCommentator = new UserDao().findById(resComment.getCommentatorId());
				jsonObject.put("resComment_content", resComment.getContent());
	//			jsonObject.put("resCommentatorName", resCommentator.getNickName());
			}else {
				jsonObject.put("nickName_r", "null");
				jsonObject.put("resComment_content", "null");
			}
			jsonArray.put(jsonObject);
			
		}
		inputStream.close();
		out.write(jsonArray.toString().getBytes());
		out.flush();
		out.close();
	}

}
