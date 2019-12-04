package GrowthRecord.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import GrowthRecord.service.GrowthRecordService;
import entity.GrowthRecord;

/**
 * Servlet implementation class AddGrowthRecordServlet
 */
@WebServlet("/AddGrowthRecordServlet")
public class AddGrowthRecordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddGrowthRecordServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String parentId = request.getParameter("parentId");
		String upTime = request.getParameter("upTime");
		String content = request.getParameter("content");
		if(parentId!=null && upTime!=null && content!=null){
			GrowthRecord growthRecord = new GrowthRecord(upTime, Integer.parseInt(parentId), content);
			int id = new GrowthRecordService().addGrowthRecord(growthRecord);  //返回的是id
			if(id != 0){
				System.out.println("插入成长记录成功");
				response.getWriter().append(id+"");    //将id返回
			}else{
				response.getWriter().append("");
			}
		}
		System.out.println("上传文件");
		response.setCharacterEncoding("UTF-8");
		InputStream is = request.getInputStream();
		File file = new File("E://a.jpg");   //绝对路径
		FileOutputStream fos = new FileOutputStream(file);   
		byte[] buffer = new byte[1024];
		int len =0;
		while((len = is.read(buffer))!=-1){
			fos.write(buffer,0,len);
		}   //写入成功
		response.getWriter().append("上传成功");
	}

}
