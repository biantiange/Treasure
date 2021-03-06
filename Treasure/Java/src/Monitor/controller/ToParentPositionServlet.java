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

import com.google.gson.Gson;

import Monitor.service.JPushToParentPositionDemo;
import Monitor.service.MonitorServiceImpl;
import entity.Parent;

/**
 * Servlet implementation class tuisong
 */
@WebServlet("/toParent/position")
public class ToParentPositionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String appKey ="aeae2f341e867be8f986dc74";
	private static final String masterSecret = "fe677c28bb7f2f2b401da9b4";

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ToParentPositionServlet() {
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
		if(childId!=null && !childId.equals("")){
			System.out.println("接收到(childId="+childId+")ChildDemo发送的POST请求");
			List<Parent> parent=new MonitorServiceImpl().getParentIdByChildId(Integer.parseInt(childId));
			String parentId=parent.get(0).getDeviceId()+"";
			// 使用输入流获取客户端的Json数据
			InputStream is = request.getInputStream();
			// 转换流
			InputStreamReader isr = new InputStreamReader(is);
			// 字符输入流
			BufferedReader reader = new BufferedReader(isr);
			String jsonStr = reader.readLine();
			System.out.println("服务器向(parentId="+parentId+")MAAndroid推送开始了");
			//家长端虚拟机 358240051111110
			JPushToParentPositionDemo.testSendPush(appKey, masterSecret,parentId,jsonStr,"tag","position");
			System.out.println("服务器向(parentId="+parentId+")MAAndroid推送结束了");
			response.getWriter().append("成功!");
		}
				
	}

}
