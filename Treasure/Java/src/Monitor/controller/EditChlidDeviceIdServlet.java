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
@WebServlet("/editDeviceId/child")
public class EditChlidDeviceIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditChlidDeviceIdServlet() {
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
		String deviceId = request.getParameter("deviceId");
		if(deviceId!=null && !deviceId.equals("") && childId!=null && !childId.equals("")){
			System.out.println("修改孩子deviceId信息——"+deviceId);
			new MonitorServiceImpl().EditDeviceIdChildById(deviceId,Integer.parseInt(childId));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
