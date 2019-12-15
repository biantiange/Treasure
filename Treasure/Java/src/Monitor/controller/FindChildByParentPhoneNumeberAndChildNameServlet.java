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
@WebServlet("/find/child")
public class FindChildByParentPhoneNumeberAndChildNameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindChildByParentPhoneNumeberAndChildNameServlet() {
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
		String parentPhoneNumber= request.getParameter("phoneNumber");
		String childName = request.getParameter("childName");
		if(parentPhoneNumber!=null && !parentPhoneNumber.equals(" ") && childName!=null && !childName.equals("")){
			System.out.println("查询孩子信息——"+"phoneNumber:"+parentPhoneNumber+" childName:"+childName);
			List<Child> list = new MonitorServiceImpl().getChildByParentPhoneNumberAndChildName(parentPhoneNumber, childName);
			if(list.size()>0){
				String childId=list.get(0).getId()+"";
				System.out.println(childId);
				response.getWriter().append(childId);
			}else{
				response.getWriter().append("no");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
