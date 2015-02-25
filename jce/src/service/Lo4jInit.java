package service;   
  
import java.io.FileInputStream;   
import java.io.IOException;   
import java.util.Properties;   
  
import javax.servlet.ServletException;   
import javax.servlet.http.HttpServlet;   
import javax.servlet.http.HttpServletRequest;   
import javax.servlet.http.HttpServletResponse;   
  
import org.apache.log4j.Logger;   
import org.apache.log4j.PropertyConfigurator;   
  
  
public class Lo4jInit   extends HttpServlet {   
  
    /**  
     *   
     */  
    private static final long serialVersionUID = 1L;   
    private static Logger logger=Logger.getLogger(Lo4jInit.class);   
       
    public void init(){   
        String path=this.getServletContext().getRealPath("/");   
        String file=this.getInitParameter("log4j_init_path");   
        String logFile=this.getInitParameter("log4j_file_path");   
        if(file!=null)   
        {   
            Properties prop = new Properties();    
            try{   
                prop.load(new FileInputStream(path+file)); //����log4j.properties   
                prop.setProperty("log4j.appender.R.File", path+logFile+ prop.getProperty("log4j.appender.R.File")); //������־�ļ������·��   
                PropertyConfigurator.configure(prop); //����������   
            }catch(Exception e)   
            {   
                logger.info("��ʼ��log4j��־����·���쳣������web.xml���������Ƿ��������쳣������"+this.getClass().getName()+"���public void init()�������쳣��Ը���ǣ�"+e.getMessage(), e.fillInStackTrace());   
            }   
        }   
           
  
    }   
    protected void service(HttpServletRequest request, HttpServletResponse response)   
    throws ServletException, IOException {   
           
    }   
  
}  