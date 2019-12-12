package mine.editUser.cotroller;

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
 * Servlet implementation class MineUpLoadServlet
 */
@WebServlet("/MineUpLoadServlet")
public class MineUpLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MineUpLoadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 System.out.println("上传文件");
		    response.setCharacterEncoding("UTF-8");
		    String phone=request.getParameter("name");
		    InputStream is = request.getInputStream();
		    System.out.print(phone);
		    
		    String imgPath=request.getServletContext().getRealPath("/")+"childImg";
		    System.out.println(imgPath);
		    
		    File file = new File(imgPath);
		    if(!file.exists()){
		    	file.mkdirs();
		    }
		    String path=phone+".jpg";
		    String ipath=imgPath+"/"+path;
		    File img=new File(ipath);
		    if(img.exists()){
		    	boolean flag=false;
		    	flag=img.delete();
		    	if(flag){
		    		System.out.println("删除无效文件");
		    	}
		    }
		    File img1=new File(ipath);
		    FileOutputStream fos = new FileOutputStream(img1);
		    byte[] buffer = new byte[1024];
		    int len = 0;
		    while((len = is.read(buffer)) != -1) {
		    	fos.write(buffer,0,len);
		    }
		   
		    is.close();
		    fos.close();
		    response.getWriter().append(path);

		
	}

}

