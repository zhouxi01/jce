package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.dom4j.Element;

import sw.jce.func.test.Demo;
import util.Base64Utils;
import util.SwxaJCEUtil;
import util.FileUtil;
import util.XmlParser;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.sansec.jce.provider.SwxaProvider;

/**
 * Servlet implementation class Post
 */

public class Post extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(Get.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Post() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	

	

	public void smartUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException{
		
		PrintWriter out = response.getWriter();
		SmartUpload mysmartupload = new SmartUpload();
		mysmartupload.initialize(this.getServletConfig(), request, response);
		// 设置文件大小限制（单位：字节）
		mysmartupload.setMaxFileSize(10000000);           
		// 设置总上传数据总大小（单位：字节）
		mysmartupload.setTotalMaxFileSize(20000000);
//		// 设置允许的文件扩展名
//		mysmartupload.setAllowedFilesList("jpg,png,gif,bmp,jpeg");
//		// 设置不允许的文件扩展名
//		mysmartupload.setDeniedFilesList("exe,bat,jsp,htm,html,,");
		
		
		
		
		

		try {
			mysmartupload.upload();
			// 读取其它非文件上传字段
			com.jspsmart.upload.Request req = mysmartupload.getRequest();
			String uid = UUID.randomUUID().toString().replace("-", "");		
			//获取服务路径
			String  path=this.getServletConfig().getServletContext().getRealPath("/");
			//文件路径
			String tempfile = path+"\\temp\\"+uid+".dat";

			// 保存文件
			for (int i = 0; i < mysmartupload.getFiles().getCount(); i++) {
				com.jspsmart.upload.File file = mysmartupload.getFiles().getFile(i);
		
				byte[] all = new byte[file.getSize()];
				for(int k =0;k<file.getSize();k++){
					all[k] = file.getBinaryData(k);
				}
				
				String cerfileNames = path+"\\cer\\lichong_verify.cer";
				String outPath =  path+"\\file\\";
				
				//返回标识：0代表正常
				int flag = SwxaJCEUtil. ERROR_NORMAL;
				String userXml = path+"\\users\\user.xml";

				//判断验签证书是否存在
				File cerFile = new File(cerfileNames);
				if(!cerFile.exists()){
					flag = SwxaJCEUtil. ERROR_NFVERIFYCER;
					log.error("验签证书不存在，文件路径："+cerfileNames);
				}
				
				if(flag ==  SwxaJCEUtil. ERROR_NORMAL){
					
					flag = SwxaJCEUtil.veriSigFile(cerfileNames, all,outPath,userXml);
				}
				
				
				out.write(flag);
			}
		} catch (SmartUploadException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	

		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		try {
			smartUpload(request,response);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}
	


}
