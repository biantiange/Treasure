package Monitor.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Monitor.service.JPushToChildAppInfoDemo;


/**
 * Servlet implementation class tuisong
 */
@WebServlet("/toChild/appInfo")
public class ToChildAppInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String appKey ="de0195e1275170d55e8bb893";
	private static final String masterSecret = "fa04153947bbff4e921633e2";

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ToChildAppInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String childId = request.getParameter("childId");
		if(childId!=null && !childId.equals("")){
			System.out.println("服务器向(childId="+childId+")ChildDemo推送获取APP信息开始了");
			JPushToChildAppInfoDemo.testSendPush(appKey, masterSecret,childId,"appInfo","childId",childId);
			System.out.println("服务器向(childId="+childId+")childDemo推送APP信息结束了");
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
