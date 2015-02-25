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
		// �����ļ���С���ƣ���λ���ֽڣ�
		mysmartupload.setMaxFileSize(10000000);           
		// �������ϴ������ܴ�С����λ���ֽڣ�
		mysmartupload.setTotalMaxFileSize(20000000);
//		// ����������ļ���չ��
//		mysmartupload.setAllowedFilesList("jpg,png,gif,bmp,jpeg");
//		// ���ò�������ļ���չ��
//		mysmartupload.setDeniedFilesList("exe,bat,jsp,htm,html,,");
		
		
		
		
		

		try {
			mysmartupload.upload();
			// ��ȡ�������ļ��ϴ��ֶ�
			com.jspsmart.upload.Request req = mysmartupload.getRequest();
			String uid = UUID.randomUUID().toString().replace("-", "");		
			//��ȡ����·��
			String  path=this.getServletConfig().getServletContext().getRealPath("/");
			//�ļ�·��
			String tempfile = path+"\\temp\\"+uid+".dat";

			// �����ļ�
			for (int i = 0; i < mysmartupload.getFiles().getCount(); i++) {
				com.jspsmart.upload.File file = mysmartupload.getFiles().getFile(i);
		
				byte[] all = new byte[file.getSize()];
				for(int k =0;k<file.getSize();k++){
					all[k] = file.getBinaryData(k);
				}
				
				String cerfileNames = path+"\\cer\\lichong_verify.cer";
				String outPath =  path+"\\file\\";
				
				//���ر�ʶ��0��������
				int flag = SwxaJCEUtil. ERROR_NORMAL;
				String userXml = path+"\\users\\user.xml";

				//�ж���ǩ֤���Ƿ����
				File cerFile = new File(cerfileNames);
				if(!cerFile.exists()){
					flag = SwxaJCEUtil. ERROR_NFVERIFYCER;
					log.error("��ǩ֤�鲻���ڣ��ļ�·����"+cerfileNames);
				}
				
				if(flag ==  SwxaJCEUtil. ERROR_NORMAL){
					
					flag = SwxaJCEUtil.veriSigFile(cerfileNames, all,outPath,userXml);
				}
				
				
				out.write(flag);
			}
		} catch (SmartUploadException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}

	}
	


}
