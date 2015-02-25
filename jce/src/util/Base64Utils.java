package util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bouncycastle.util.encoders.Base64;



/**
 * <p>
 * BASE64������빤�߰�
 * </p>
 * <p>
 * ����javabase64-1.3.1.jar
 * </p>
 * 
 * @author IceWee
 * @date 2012-5-19
 * @version 1.0
 */
public class Base64Utils {
	
    /**
     * �ļ���ȡ��������С
     */
    private static final int CACHE_SIZE = 1024;
    
    /**
     * <p>
     * BASE64�ַ�������Ϊ����������
     * </p>
     * 
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) throws Exception {
        return Base64.decode(base64.getBytes());
    }
    
    /**
     * <p>
     * ���������ݱ���ΪBASE64�ַ���
     * </p>
     * 
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) throws Exception {
        return new String(Base64.encode(bytes));
    }
    
    /**
     * <p>
     * ���ļ�����ΪBASE64�ַ���
     * </p>
     * <p>
     * ���ļ����ã����ܻᵼ���ڴ����
     * </p>
     * 
     * @param filePath �ļ�����·��
     * @return
     * @throws Exception
     */
    public static String encodeFile(String filePath) throws Exception {
        byte[] bytes = fileToByte(filePath);
        return encode(bytes);
    }
    

 
	public static void main ( String[] args )
	{
		String fileName = "C:/Users/Administrator/Desktop/test.dat";
		
		
		FileInputStream in;
		try {

			 byte[] all = Base64Utils.fileToByte(fileName);
	        //Ա����ų���
	        byte[] flag1 = ByteUtil.copyBytes(all, 0, 4);
			int temp1 = ByteUtil.bytes2Integer(flag1);
			byte[] flag2 =  ByteUtil.copyBytes(all, 4, 4);
		    //Ӳ����ų���
			int temp2 = ByteUtil.bytes2Integer(flag2);
			//ǩ������
			 byte[] flag3 = ByteUtil.copyBytes(all, 8, 4);
			 int temp3 = ByteUtil.bytes2Integer(flag3);
	        //Ա�����
			 byte[] employeeSerialNum =  ByteUtil.copyBytes(all, 12, temp1);
			 //Ӳ�����
			 byte[] hdSerialNum =   ByteUtil.copyBytes(all, 12+temp1, temp2);
			//ǩ��
			 byte[] sign =   ByteUtil.copyBytes(all,  12+temp1+temp2, temp3);
			 byte[] input =  ByteUtil.copyBytes(all,  0, 12+temp1+temp2+temp3);
			 
	


			System.out.println("ddd");
		} catch (FileNotFoundException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
       

    }  

	
    
    /**
     * <p>
     * BASE64�ַ���ת���ļ�
     * </p>
     * 
     * @param filePath �ļ�����·��
     * @param base64 �����ַ���
     * @throws Exception
     */
    public static void decodeToFile(String filePath, String base64) throws Exception {
        byte[] bytes = decode(base64);
        byteArrayToFile(bytes, filePath);
    }
    
    /**
     * <p>
     * �ļ�ת��Ϊ����������
     * </p>
     * 
     * @param filePath �ļ�·��
     * @return
     * @throws Exception
     */
    public static byte[] fileToByte(String filePath) throws Exception {
        byte[] data = new byte[0];
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
            out.close();
            in.close();
            data = out.toByteArray();
         }
        return data;
    }
    
    /**
     * <p>
     * ����������д�ļ�
     * </p>
     * 
     * @param bytes ����������
     * @param filePath �ļ�����Ŀ¼
     */
    public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
        InputStream in = new ByteArrayInputStream(bytes);   
        File destFile = new File(filePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        byte[] cache = new byte[CACHE_SIZE];
        int nRead = 0;
        while ((nRead = in.read(cache)) != -1) {   
            out.write(cache, 0, nRead);
            out.flush();
        }
        out.close();
        in.close();
    }
 
}