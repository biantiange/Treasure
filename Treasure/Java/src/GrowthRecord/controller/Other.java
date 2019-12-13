package GrowthRecord.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.NEW;

import GrowthRecord.dao.GrowthRecordDao;
import GrowthRecord.service.GrowthRecordService;
import entity.LookByTagEntity;

/**
 * Servlet implementation class Other
 */
@WebServlet("/Other")
public class Other extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Other() {
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
	//http://localhost:8080/Java/Other?str=%E7%94%9F%E6%97%A5&parentId=1
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Other");
		String str = request.getParameter("str");
		//str=new String(str.getBytes("ISO-8859-1"),"UTF-8");
		str = URLDecoder.decode(str,"UTF-8"); 
		String parentId1 = request.getParameter("parentId");
		System.out.println("按标签要查找"+str+parentId1);
		List<Map<String,Object>> looks = new ArrayList<>();
		
		List<Map<String,Object>> lists = new GrowthRecordService().findByTag(str, Integer.parseInt(parentId1));   //找出来的原数据	
		String[] paths = new String[lists.size()];
		
		HashMap<String,String[]> data = new HashMap<>();  //只存放路径
		//Map<String,Object> map = new HashMap<>();
		paths[0] = lists.get(0).get("imgPath").toString();
		System.out.println(Arrays.toString(paths));
		//data.put(lists.get(0).get("growthRecordId").toString(), paths);
		data.put(lists.get(0).get("upTime").toString(), paths);
		/*map.put("content",lists.get(0).get("content").toString());
		map.put("upTime",lists.get(0).get("upTime").toString());
		map.put("growthRecordId",lists.get(0).get("growthRecordId").toString());*/
		for(int i=0;i<lists.size();i++){
			//String growthRecordId = lists.get(i).get("growthRecordId").toString(); //得到成长记录id
			String time = lists.get(i).get("upTime").toString();	
			if(data.containsKey(time)){				
				String[] paths1=data.get(time);
				for(int j=0;j<paths1.length;j++){
					if(paths1[j]==null){
						paths1[j]=lists.get(i).get("imgPath").toString();
						data.put(time, paths1);
						System.out.println(Arrays.toString(data.get(time)));
						break;
					}
				}
			}else{  //不包含，就自己存
				String[] paths2=new String[lists.size()];
				for(int j=0;j<paths2.length;j++){
					if(paths2[j]==null){
						paths2[j]=lists.get(i).get("imgPath").toString();
						data.put(time, paths2);
						System.out.println(Arrays.toString(data.get(time)));
						break;
					}
				}
			}
			
		}  //for结束
		
		//把HashMap<String,String[]> data转成Json字符串格式
		String imgPath = new Gson().toJson(data);
		System.out.println(imgPath);
		response.getWriter().append(imgPath);
		//System.out.println(lists);
		//根据GrowthRecordID来查询growthRecord
		/*Map<String,Object> map = new HashMap<>();
		map.put("content",lists.get(i).get("content").toString());*/
	}

}
