package mine.myMessage.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;



import mine.child.service.ChildService;
import mine.editUser.service.FindUserService;
import mine.myMessage.service.FindMyMessageService;

/**
 * Servlet implementation class FindMyMessageServlet
 */
@WebServlet("/FindMyMessageServlet")
public class FindMyMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindMyMessageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String userId=request.getParameter("userId");
		
		System.out.println(userId);
		if(userId!=null){
			int id=Integer.parseInt(userId);
			//userId就是被回复人的Id，跟据id查找哪条评论是回复user的，放到一个集合里面
			List<Map<String, Object>> list  = new FindMyMessageService().listMessage(id);
		    JSONArray jsonArray=new JSONArray();
			for(Map<String,Object> map:list) {
		        	JSONObject jsonObject = new JSONObject();
		        	jsonObject.put("postId", map.get("postId").toString());//在哪个的帖子下评论的
		        	//根据帖子id查找帖子内容
		        	jsonObject.put("commentatorId", map.get("commentatorId").toString());//评论人的id
		        	String commentatorId=map.get("commentatorId").toString();
		        	//根据id，查找此人的相关信息
		        	if(commentatorId!=null){
		        		 Map<String, Object> map2=new HashMap<>();
		        		 List<Map<String, Object>> list2 = new FindUserService().User(commentatorId);
		     	        if(list2.size()!=0){
		     	        map2=list2.get(0);
		     	        jsonObject.put("userImg", map2.get("headerPath").toString());//评论人的图片地址
		     	        jsonObject.put("nickName", map2.get("nickName").toString());//评论人的昵称
		     	        }
		        	}
		        
		        	jsonObject.put("tomycontent", map.get("content").toString());//评论内容
		        	String res=map.get("resComId").toString();//在哪个评论下进行的（被评论的id）
		        	int resComId=Integer.parseInt(res);
		        	System.out.println(resComId);
		        	
		        	//根据userId和resComId确认评论人评论的是user发表的什么,如果resComId为非空即非0，则表示是在我的的评论下
		        	//进行的评论，如果是0表示是评论了自己的社区内容
		        	if(resComId==0){//表示评论的是我的帖子	
		        		jsonObject.put("mycontent", "");
		        	}else{//表示对我的评论     的回复
		        		System.out.println("1");
		        		List<Map<String, Object>> li  = new FindMyMessageService().listUserMessage(resComId);//指定评论id、
		        		System.out.println("2");
		        		Map<String, Object> map3=new HashMap<>();
		        		if(li==null){
		        			System.out.println("获取失败");
		        			
		        		}
		        		else{
		        			System.out.println("3");
		        			map3=li.get(0);
		        			jsonObject.put("mycontent", map3.get("content").toString());
		        		}
		        	}
		        	System.out.println("4");
		        	jsonObject.put("time", map.get("time").toString());//该评论的时间
		        	jsonArray.put(jsonObject);
			}
			response.getWriter().append(jsonArray.toString());
		System.out.println(jsonArray.toString());

		}
		
	}
	
}
