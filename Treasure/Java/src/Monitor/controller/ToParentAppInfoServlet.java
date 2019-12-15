package Monitor.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.google.gson.Gson;

import Monitor.service.JPushToParentAppInfoDemo;
import Monitor.service.MonitorServiceImpl;
import entity.Parent;

/**
 * Servlet implementation class tuisong
 */
@WebServlet("/toParent/appInfo")
public class ToParentAppInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String appKey ="aeae2f341e867be8f986dc74";
	private static final String masterSecret = "fe677c28bb7f2f2b401da9b4";

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ToParentAppInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		//传递Json数据
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String childId=request.getParameter("childId");
		System.out.println("接收到ChildDemo发送的POST请求:"+childId);
		if(childId!=null && !childId.equals("")){
			List<Parent> parents=new MonitorServiceImpl().getParentIdByChildId(Integer.parseInt(childId));
			String parentId=parents.get(0).getId()+"";
			// 使用输入流获取客户端的Json数据
			InputStream is = request.getInputStream();
			// 转换流
			InputStreamReader isr = new InputStreamReader(is);
			// 字符输入流
			BufferedReader reader = new BufferedReader(isr);
			String jsonStr = reader.readLine();
			JSONArray json = new JSONArray(jsonStr);
			System.out.println("服务器向(parentId="+parentId+")MAAndroid推送APP信息开始了");
			//家长端虚拟机 358240051111110
			JPushToParentAppInfoDemo.testSendPush(appKey, masterSecret,parentId,jsonStr,"tag","appInfo");
			System.out.println("服务器向(childId="+childId+")MAAndroid推送APP信息结束了");
			response.getWriter().append("成功!");
		}
				
	}

}
