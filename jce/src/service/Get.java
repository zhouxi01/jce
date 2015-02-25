package service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import util.Base64Utils;
import util.ByteUtil;
import util.SwxaJCEUtil;
import util.XmlParser;

/**
 * Servlet implementation class Get
 */
public class Get extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(Get.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Get() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//员工号 
		String employeeSerialNum = request.getParameter("employeeSerialNum");
		//硬盘序列号
		String hdSerialNum = request.getParameter("hdSerialNum");
		
		//获取服务路径
		String  path=this.getServletConfig().getServletContext().getRealPath("/");
		log.info("员工号"+employeeSerialNum+",请求获取硬盘序列号为"+hdSerialNum+"的私钥");

		//文件路径
		String fileName = path+"\\file\\"+hdSerialNum+".dat";
		String cerFileName = path+"\\cer\\lichong_encrypt.cer";
		String userXml = path+"\\users\\user.xml";
		
		//返回标识：0代表正常
		int flag = SwxaJCEUtil. ERROR_NORMAL;
		
		Element root = XmlParser.getRootNode(userXml);
		
		List<Element> users = XmlParser.getChildList(root);
		HashSet<String> set = new HashSet<String>();
		for(int i = 0 ;i<users.size();i++){
			String name = XmlParser.getAttribute(users.get(i), "employeeSerialNum").getValue();
			set.add(name);
			
		}
		//判断是否有权限
		if(!set.contains(employeeSerialNum))
		{
			flag = SwxaJCEUtil. ERROR_NOPERMISSION;
			log.error("员工号"+employeeSerialNum+"无权获取私钥");
		}
		//判断文件是否存在
		File file = new File(fileName);
		if(!file.exists()){
			log.error("私钥不存在，文件路径"+fileName);
			flag = SwxaJCEUtil. ERROR_FILENOTFOUND;
		}
		//判断解密证书是否存在
		File cerFile = new File(cerFileName);
		if(!cerFile.exists()){
			log.error("解密证书不存在，文件路径"+cerFileName);
			flag = SwxaJCEUtil. ERROR_NFDECER;
		}

		try {

			//标识
			byte[] result = ByteUtil.get1Bytes(flag);
			if(flag==SwxaJCEUtil. ERROR_NORMAL){
				//原始内容
				byte[] input = Base64Utils.fileToByte(fileName);
				//用密码机2号位私钥解密
				byte[] content = SwxaJCEUtil.decryptBySwxa(2, input);
				//用证书公钥加密
				byte[] encontent = SwxaJCEUtil.encryptByCer(cerFileName, content);

				//加密内容长度
				byte[] contentLength = ByteUtil.integer2Bytes(encontent.length);

				
				
				result =ByteUtil.bytes2Bytes(result,contentLength);
				result = ByteUtil.bytes2Bytes(result,encontent);

				//用密码机1号位私钥签名
				byte[] sign = SwxaJCEUtil.signBySwxa(1, SwxaJCEUtil.SHA1, result);
				result = ByteUtil.bytes2Bytes(result,sign);
			}

			response.getOutputStream().write(result);
			response.getOutputStream().flush();
		    response.getOutputStream().close();


		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
