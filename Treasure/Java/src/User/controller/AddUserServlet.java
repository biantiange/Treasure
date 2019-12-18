package User.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import User.service.UserService;
import entity.User;

/**
 * Servlet implementation class AddUserServlet
 */
@WebServlet("/AddUserServlet")
public class AddUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    //http://localhost:8080/Java/AddUserServlet?phoneNumber=15032742188&&password=321
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("UTF-8");
		String phoneNumber = request.getParameter("phoneNumber");
		String password = request.getParameter("password");
		System.out.println("要添加的用户："+phoneNumber+"-"+password);
		if(!phoneNumber.equals("") && !password.equals("")){
			User user = new User();
			user.setPassword(password);
			user.setPhoneNumber(phoneNumber);
			//给注册的用户添加默认的信息
			user.setHeaderPath("parent/my_01.png"); //头像
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMHHmmss" );// HH:mm:ss
	        Date date = new Date(System.currentTimeMillis());
	        user.setNickName(simpleDateFormat.format(date));//昵称
			String str = new UserService().addUser(user);  //返回的是id值
			System.out.println(str);
			if(str.equals("该手机号已被注册")){
				response.getWriter().append("");
				return;
			}else{
				int id = Integer.parseInt(str);
				if(id==-1){
					response.getWriter().append("FAIL");
					return;
				}else{
					response.getWriter().append("OK");
					return;
				}
			}
		}else{
			response.getWriter().append("FAIL");
			return;
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
