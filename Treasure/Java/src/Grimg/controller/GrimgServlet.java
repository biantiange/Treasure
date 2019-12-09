package Grimg.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Grimg.service.GrimgService;
import entity.Grimg;

/**
 * Servlet implementation class GrimgServlet
 */
@WebServlet("/GrimgServlet")
public class GrimgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GrimgServlet() {
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
		System.out.println("GrimgServlet");
		String id = request.getParameter("id");
		String growthRecordId = request.getParameter("growthRecordId");
		String upTime = request.getParameter("upTime");
		//String imgPath = request.getParameter("imgPath");
		String tag = request.getParameter("tag");
		if(id!=null&&growthRecordId!=null && upTime!=null){
			Grimg grimg = new Grimg();
			grimg.setId(Integer.parseInt(id));
			grimg.setGrowthRecordId(Integer.parseInt(growthRecordId));
			//grimg.setImgPath(imgPath);
			grimg.setUpTime(upTime);
			grimg.setTag(tag);
			int count = new GrimgService().addGrimgOther(grimg);
			if(count != 0){
				System.out.println("添加记录图片成功");
				response.getWriter().append("OK");
			}
		}
		
	}

}
