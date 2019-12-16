package Post.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.sun.jmx.snmp.Timestamp;

import Post.dao.PostDao;
import PostImg.dao.PostImgDao;
import entity.Post;
import entity.PostImg;


/**
 * Servlet implementation class GetImageFileServlet
 */
@WebServlet("/GetImageFileServlet")
public class GetImageFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetImageFileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("下载文件");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String content = "";
		int paretId = 0;
		String imgPath = "";
		List<String> imgPaths = new ArrayList<String>();
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			// Parse the request
			List<FileItem> /* FileItem */items = upload.parseRequest(request);
			System.out.println(items.size()+"这么多！");
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					System.out.println(fi.getFieldName()+":"+fi.getString());
					if (fi.getFieldName().equals("content")) {
						content = URLDecoder.decode(fi.getString(), "utf-8");
						System.out.println("输入的文字是:"+content);
					}else if (fi.getFieldName().equals("id")) {
						paretId = Integer.parseInt(fi.getString());
					}
				}else {
					String realPath = this.getServletContext().getRealPath("/");
					String keepPath = realPath + "postImgs/";
					File file = new File(keepPath);
					if (!file.exists()) {
						file.mkdir();
					}
					fi.write(new File(keepPath+fi.getName()));
					imgPath = fi.getName();
					imgPaths.add(imgPath);
				}
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Post post = new Post();
		post.setContent(content);
//		post.setTime(System.currentTimeMillis());
		post.setPosterId(paretId);
		int add = new PostDao().savePost(post);
		
		Post id = new PostDao().maxId();
		int maxId = id.getId();
		//img
		for(int i = 0;i<imgPaths.size();i++){
			PostImg img = new PostImg();
			img.setPath(imgPaths.get(i));
			img.setPostId(maxId);
//			img.setTime(System.currentTimeMillis());
			int imgAdd = new PostImgDao().savePostImg(img);
		}
			
			
		// 返回响应
		response.getWriter().append(new Gson().toJson(id));
	}

}
