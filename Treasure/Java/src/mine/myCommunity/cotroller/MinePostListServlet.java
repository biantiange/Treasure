package mine.myCommunity.cotroller;

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

import Comment.service.CommentServicelmpl;
import Post.service.PostServicelmpl;
import PostImg.service.PostImgServicelmpl;
import Praise.dao.PraiseDao;
import User.dao.UserDao;
import entity.User;

/**
 * Servlet implementation class MinePostListServlet
 */
@WebServlet("/MinePostListServlet")
public class MinePostListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int praiserId;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MinePostListServlet() {
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
		// TODO Auto-generated method stub
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String pa=request.getParameter("praiserId");
		
		if(pa!=null || pa.length()!=0){
			praiserId =Integer.parseInt(pa);
		}
		JSONArray jsonArray = new JSONArray();
        List<Map<String, Object>> list = new PostServicelmpl().MylistPost(praiserId);
        for(Map<String,Object> map:list) {
        	//post
        	JSONObject jsonObject = new JSONObject();
        	jsonObject.put("id", map.get("id"));
        	System.out.println(map.get("id")+"post");
        	jsonObject.put("content", map.get("content"));
        	jsonObject.put("time", map.get("time"));
        	jsonObject.put("praiseCount", map.get("praiseCount"));
        	//Poster
        	User pUser = new UserDao().findById((int)map.get("posterId"));
        	jsonObject.put("Poster_id", pUser.getId());
//        	jsonObject.put("phoneNumber", pUser.getPhoneNumber());
        	jsonObject.put("headerPath", pUser.getHeaderPath());
        	jsonObject.put("nickName", pUser.getNickName());
        	//img
        	List<Map<String,Object>> imgs = new PostImgServicelmpl().listPostImg((int)map.get("id"));
        	JSONArray Jimgs = new JSONArray();
        	JSONObject img = new JSONObject();
        	int j = 0;
        	for(Map<String,Object> mimg:imgs) {
        		img.put("Pimg_id"+j, mimg.get("id"));
        		img.put("path"+j, mimg.get("path"));
        		img.put("Pimg_time"+j, mimg.get("time").toString());
        		img.put("postId"+j, mimg.get("postId"));
        		j++;
        		Jimgs.put(img);
        	}
        	jsonObject.put("imgs", Jimgs);
        	System.out.println(Jimgs);
        	
        	//3_comment
        	List<Map<String,Object>> comments = new CommentServicelmpl().listComment_3((int)map.get("id"));
        	JSONArray jcomments = new JSONArray();
        	JSONObject comment = new JSONObject();
        	int i=0;
        	for(Map<String,Object> mcomment:comments) {
        		//根据commentatorId 查出评论人
        		User user = new UserDao().findById((int) mcomment.get("commentatorId"));
        		comment.put("commentatorName"+i, user.getNickName());
        		comment.put("commentContent"+i, mcomment.get("content"));
        		jcomments.put(comment);
        		i++;
        	}
        	jsonObject.put("comments", jcomments);
        	
        	//isPraise
        	int isPraise = new PraiseDao().isPraise(praiserId, (int)map.get("id"));
        	jsonObject.put("isPraise",isPraise);

        	jsonArray.put(jsonObject);
        	
        }
        
        response.getWriter().append(jsonArray.toString());
	}

}