package GrowthRecord.controller;

import java.io.IOException;
import java.util.ArrayList;
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
 * Servlet implementation class LookTimeServlet
 */
@WebServlet("/LookTimeServlet")
public class LookTimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LookTimeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("获取时间列表");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		List<String> list =new GrowthRecordService().listTimes();
		List<String> newList = new  ArrayList<String>(); 
         for (String cd:list) {
            if(!newList.contains(cd)){
                newList.add(cd);
            }
        }
		response.getWriter().append(new Gson().toJson(newList));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
