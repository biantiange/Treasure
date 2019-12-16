package GrowthRecord.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import GrowthRecord.service.GrowthRecordService;

/**
 * Servlet implementation class LookByTagServlet
 */
@WebServlet("/LookByTagServlet")
public class LookByTagServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LookByTagServlet() {
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
	//http://localhost:8080/Java/LookByTagServlet?str=%E7%94%9F%E6%97%A5&parentId=1
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("LookByTagServlet");
		String str = request.getParameter("str");
		str=new String(str.getBytes("ISO-8859-1"),"UTF-8");
		//str = URLDecoder.decode(str,"UTF-8"); 
		String parentId1 = request.getParameter("parentId");
		System.out.println("按标签要查找"+str+parentId1);
		List<Map<String,Object>> lists = null;
		
		if(parentId1 !=null){
			int parentId = Integer.parseInt(parentId1);
			lists = new GrowthRecordService().findByTag(str, parentId);
			System.out.println(lists.toString());
		}
		System.out.println(lists.toString());
		response.getWriter().append(new Gson().toJson(lists));
		/*if(lists == null){
			System.out.println("无数据");
			response.getWriter().append("");
			return;
		}else{
			System.out.println("有数据");
			response.getWriter().append(new Gson().toJson(lists));
			return;
		}*/
		//System.out.println("查找咯"+lists.size());
		
			
	}

}
