package Grimg.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Grimg.service.GrimgService;


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
		//上传到服务器
		System.out.println("UploadServlet");
		response.setCharacterEncoding("UTF-8");
		InputStream is = request.getInputStream();
		String path = request.getServletContext().getRealPath("/")+"grimg/";
		
		File file = new File(path);   //绝对路径
		if(!file.exists()){
			file.mkdirs();
		}
		String str = System.currentTimeMillis()+".jpg";
		String imgPath = path+str;
		System.out.println("要上传的文件路径"+imgPath);
		File img = new File(imgPath);
		FileOutputStream fos = new FileOutputStream(img);   
		byte[] buffer = new byte[1024];
		int len =0;
		while((len = is.read(buffer))!=-1){
			fos.write(buffer,0,len);
		}   //写入成功
		//上传一个，插入一个grimg
		//request.setAttribute("imgPath", "/grimg/"+str);
		//先插入imgPath，让其返回id，然后根据id在插入其他值
		int id = -1;
		id=new GrimgService().addGrimgPath(str);
		System.out.println("插入记录图片成功");
		response.getWriter().append(id+"");
		
	}

	

}
