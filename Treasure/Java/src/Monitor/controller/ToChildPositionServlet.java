package Monitor.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Monitor.service.JPushToChildPositionDemo;

/**
 * Servlet implementation class tuisong
 */
@WebServlet("/toChild/position")
public class ToChildPositionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String appKey ="de0195e1275170d55e8bb893";
	private static final String masterSecret = "fa04153947bbff4e921633e2";

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ToChildPositionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String childId = request.getParameter("childId");
		System.out.println("服务器向ChildDemo推送获取位置信息开始了");
		//学生端虚拟机   358240051111110      真机860649040120944
		JPushToChildPositionDemo.testSendPush(appKey, masterSecret,"358240051111110","position");
		System.out.println("服务器向ChildDemo推送获取位置信息结束了");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
