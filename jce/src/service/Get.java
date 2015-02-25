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

		//Ա���� 
		String employeeSerialNum = request.getParameter("employeeSerialNum");
		//Ӳ�����к�
		String hdSerialNum = request.getParameter("hdSerialNum");
		
		//��ȡ����·��
		String  path=this.getServletConfig().getServletContext().getRealPath("/");
		log.info("Ա����"+employeeSerialNum+",�����ȡӲ�����к�Ϊ"+hdSerialNum+"��˽Կ");

		//�ļ�·��
		String fileName = path+"\\file\\"+hdSerialNum+".dat";
		String cerFileName = path+"\\cer\\lichong_encrypt.cer";
		String userXml = path+"\\users\\user.xml";
		
		//���ر�ʶ��0��������
		int flag = SwxaJCEUtil. ERROR_NORMAL;
		
		Element root = XmlParser.getRootNode(userXml);
		
		List<Element> users = XmlParser.getChildList(root);
		HashSet<String> set = new HashSet<String>();
		for(int i = 0 ;i<users.size();i++){
			String name = XmlParser.getAttribute(users.get(i), "employeeSerialNum").getValue();
			set.add(name);
			
		}
		//�ж��Ƿ���Ȩ��
		if(!set.contains(employeeSerialNum))
		{
			flag = SwxaJCEUtil. ERROR_NOPERMISSION;
			log.error("Ա����"+employeeSerialNum+"��Ȩ��ȡ˽Կ");
		}
		//�ж��ļ��Ƿ����
		File file = new File(fileName);
		if(!file.exists()){
			log.error("˽Կ�����ڣ��ļ�·��"+fileName);
			flag = SwxaJCEUtil. ERROR_FILENOTFOUND;
		}
		//�жϽ���֤���Ƿ����
		File cerFile = new File(cerFileName);
		if(!cerFile.exists()){
			log.error("����֤�鲻���ڣ��ļ�·��"+cerFileName);
			flag = SwxaJCEUtil. ERROR_NFDECER;
		}

		try {

			//��ʶ
			byte[] result = ByteUtil.get1Bytes(flag);
			if(flag==SwxaJCEUtil. ERROR_NORMAL){
				//ԭʼ����
				byte[] input = Base64Utils.fileToByte(fileName);
				//�������2��λ˽Կ����
				byte[] content = SwxaJCEUtil.decryptBySwxa(2, input);
				//��֤�鹫Կ����
				byte[] encontent = SwxaJCEUtil.encryptByCer(cerFileName, content);

				//�������ݳ���
				byte[] contentLength = ByteUtil.integer2Bytes(encontent.length);

				
				
				result =ByteUtil.bytes2Bytes(result,contentLength);
				result = ByteUtil.bytes2Bytes(result,encontent);

				//�������1��λ˽Կǩ��
				byte[] sign = SwxaJCEUtil.signBySwxa(1, SwxaJCEUtil.SHA1, result);
				result = ByteUtil.bytes2Bytes(result,sign);
			}

			response.getOutputStream().write(result);
			response.getOutputStream().flush();
		    response.getOutputStream().close();


		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
