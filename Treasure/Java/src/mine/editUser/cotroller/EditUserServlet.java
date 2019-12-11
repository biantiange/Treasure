package mine.editUser.cotroller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import mine.editUser.service.FindUserService;


/**
 * Servlet implementation class EditUserServlet
 */
@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("开始修改。。。");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String phoneNumber=request.getParameter("phoneNumber");
		String url=request.getParameter("imgPath");
		String nickname=request.getParameter("nickname");
		System.out.println(url);
		System.out.println(nickname);
		System.out.println(phoneNumber);
		boolean edit = new FindUserService().editUser(nickname, url, phoneNumber);
		if(edit){
				response.getWriter().append(url);
			}else {
				response.getWriter().append("修改失败");
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
