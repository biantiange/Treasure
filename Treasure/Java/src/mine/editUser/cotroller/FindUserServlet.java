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
 * Servlet implementation class FindUserServlet
 */
@WebServlet("/FindUserServlet")
public class FindUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String phoneNumber=request.getParameter("phoneNumber");
		if(phoneNumber!=null){
			 JSONArray jsonArray = new JSONArray();
	        List<Map<String, Object>> list = new FindUserService().listUser(phoneNumber);
	        if(list!=null){
	        for(Map<String,Object> map:list) {
	        	JSONObject jsonObject = new JSONObject();
	        	jsonObject.put("headerPath", map.get("headerPath").toString());
	        	jsonObject.put("nickName", map.get("nickName"));
	        	jsonArray.put(jsonObject);
	        	System.out.println(jsonObject.toString());
	        }
	        	response.getWriter().append(jsonArray.toString());
	        }else {
				System.out.println("kongzhi");
			}
	    }else {
	    	System.out.println("失败");
				
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
