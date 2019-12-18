package mine.editUser.cotroller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	    String parentId=request.getParameter("name");
	    InputStream is = request.getInputStream();
	    System.out.print(parentId);
	    String imgPath=request.getServletContext().getRealPath("/")+"childImg";
	    System.out.println(imgPath);
	    
	    File file = new File(imgPath);
	    if(!file.exists()){
	    	file.mkdirs();
	    }
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMddHHmmss" );
	    Date date = new Date( System.currentTimeMillis() );
        String data0 = simpleDateFormat.format( date );
        System.out.println(data0);
	    String imgName=data0+".jpg";
	    String ipath=imgPath+"/"+imgName;
	    File img1=new File(ipath);
	    FileOutputStream fos = new FileOutputStream(img1);
	    byte[] buffer = new byte[1024];
	    int len = 0;
	    while((len = is.read(buffer)) != -1) {
	    	fos.write(buffer,0,len);
	    }
	    is.close();
	    fos.close();
	    response.getWriter().append(imgName);

	


		
	}

}

