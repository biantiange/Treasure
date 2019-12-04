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


/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
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
		System.out.println("hello");
		/*String fileName = request.getParameter("filename");
		System.out.println("要上传的文件"+fileName);*/
		/*System.out.println("上传文件");
		response.setCharacterEncoding("UTF-8");
		InputStream is = request.getInputStream();
		
		
		File file = new File("E://a.jpg");   //绝对路径
		FileOutputStream fos = new FileOutputStream(file);   
		byte[] buffer = new byte[1024];
		int len =0;
		while((len = is.read(buffer))!=-1){
			fos.write(buffer,0,len);
		}   //写入成功
		response.getWriter().append("上传成功");*/
	}

	

}
