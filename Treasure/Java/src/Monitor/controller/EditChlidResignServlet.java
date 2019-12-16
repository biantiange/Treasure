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
@WebServlet("/editisResign/child")
public class EditChlidResignServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditChlidResignServlet() {
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
		String childId = request.getParameter("childId");
		if(childId!=null && !childId.equals("")){
			System.out.println("修改孩子注册信息——"+childId);
			new MonitorServiceImpl().EditIsRegistChildById(Integer.parseInt(childId));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
