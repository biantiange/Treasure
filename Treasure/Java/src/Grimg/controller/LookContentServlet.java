package Grimg.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Grimg.service.GrimgService;
import GrowthRecord.service.GrowthRecordService;

/**
 * Servlet implementation class LookContentServlet
 */
@WebServlet("/LookContentServlet")
public class LookContentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LookContentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String time=request.getParameter("time");
		System.out.println(time);
		List<Map<String,Object>> list = new ArrayList<>();
		List<Map<String,Object>> pictureList =new GrimgService().listPictureByTime(time);
		List<Map<String,Object>> contentList =new GrimgService().listContentByTime(time);
		for(int i=0;i<pictureList.size();i++){
			for(int j=0;j<contentList.size();j++){
				if(pictureList.get(i).get("recordId")==contentList.get(j).get("id")){
					Map<String,Object> map=new HashMap<>();
					String path=pictureList.get(i).get("imgPath").toString();
					String cont=contentList.get(j).get("content").toString();
					map.put("path", path);
					map.put("cont", cont);
					list.add(map);
				}
			}
		}
		response.getWriter().append(new Gson().toJson(list));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
