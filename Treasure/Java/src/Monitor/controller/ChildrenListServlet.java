package Monitor.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Monitor.service.MonitorServiceImpl;
import entity.Child;

/**
 * Servlet implementation class ListServlet
 */
@WebServlet("/monitor/child")
public class ChildrenListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChildrenListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//根据家长的Id获取全部孩子信息
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String parentId = request.getParameter("parentId");
		System.out.println("获取孩子信息——"+"parentId:+"+parentId);
		List<Child> list = new MonitorServiceImpl().listChildByParentId(Integer.parseInt(parentId));

		if(list.size()>0){
			String msgStr=new Gson().toJson(list);
			response.getWriter().append(msgStr);	
		}else{
			response.getWriter().append("no");	
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
