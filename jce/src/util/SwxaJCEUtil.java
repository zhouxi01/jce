package util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;



import java.util.HashSet;
import java.util.List;

import javax.crypto.Cipher;







import org.apache.log4j.Logger;
import org.dom4j.Element;

import sun.misc.BASE64Encoder;

public class SwxaJCEUtil {
	
	
    /**
     * Java��Կ��(Java ��Կ�⣬JKS)KEY_STORE
     */
    public static final String Provider = "SwxaJCE";
    public static final String RSA = "RSA";

    public static final String X509 = "X.509";
    
    public static final String Transformation ="RSA/ECB/PKCS1Padding";
    
    public static final String SHA1 = "SHA1WithRSA";
    public static final String SHA224 = "SHA224WithRSA";
    public static final String SHA256 = "SHA256WithRSA";
    public static final String SHA384 = "SHA384WithRSA";
    public static final String SHA512 = "SHA512WithRSA";
    public static final String MD2 = "MD2WithRSA";
    public static final String MD4 = "MD4WithRSA";
    public static final String MD5 = "MD5WithRSA";
    public static final int size = 4;
    public static Logger log = Logger.getLogger(SwxaJCEUtil.class);
    
    /**
     * ����ļ����ܿ�
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /**
     * ����ļ����ܿ�
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    
    /**
     * ����
     */
    public static final int ERROR_NORMAL = 0;
    /**
     * û��Ȩ��
     */
    public static final int  ERROR_NOPERMISSION = 3;
   
    /**
     * û����ǩ֤��
     */
    public static final int  ERROR_NFVERIFYCER= 4;
    /**
     * ��ǩʧ��
     */
    public static final int  ERROR_VERIFYFAILED = 5;
    /**
     * �ļ�������
     */
    public static final int  ERROR_FILENOTFOUND = 6;
    /**
     * û�н���֤��
     */
    public static final int  ERROR_NFDECER = 7;
    
    /**
     * �ļ���ȡ��������С
     */
    private static final int CACHE_SIZE = 2048;
    

	
	public SwxaJCEUtil(){
		//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	/**
	 * ��ȡ�����
	 * @return
	 * @throws Exception
	 */
	public static String genRandom() throws Exception {
		SecureRandom random = SecureRandom.getInstance("RND", Provider);
		int length = random.nextInt(819);
		//������������󳤶�Ϊ 8kB(8192B)
		byte[] tmp = random.generateSeed(length);
		System.out.println(new BASE64Encoder().encode(tmp));
		return new BASE64Encoder().encode(tmp);
	}
	
	/**
	 * ��ȡ��Կ��
	 * @param index ��ţ�1�Ŵ��� 1��ǩ����Կ��2�Ŵ���1�ż�����Կ��3�Ŵ���2��ǩ����Կ���Դ�����
	 * @return
	 * @throws Exception
	 */
	public static KeyPair genRSAKey(int index) throws Exception {
		KeyPair kp = null;
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA, Provider);
		//int keysize = 1024�� 2048�� 3072�� 4096�� n<<16(nΪ��Կ��ţ�
		kpg.initialize(index<<16);
		
		kp = kpg.genKeyPair();
		if (kp == null) {
			System.out.println("����RSA��Կ��ʧ�ܣ�");
		} else {
			// ������Կ�ɹ���
			System.out.println(kp.getPublic());
			System.out.println(kp.getPrivate());
		}
		
		return kp;
	}
	
	/**
	 * ����
	 * @throws Exception
	 */
	public static byte[] encryptBySwxa(int index,byte[] input)throws Exception{
		KeyPair kp =genRSAKey(index);
		Cipher cipher = Cipher.getInstance(Transformation, Provider);
		cipher.init(Cipher.ENCRYPT_MODE, kp.getPublic());
		
		byte[] out = encrypt(cipher, input);
		if(out ==null){
			System.out.println(Transformation+ " Mode Encrypt ERROR! Return value is NULL!");
		}
		return out;
	}
	
	/**
	 * ��֤�����
	 * @param cerfile ֤���ļ�
	 * @param input ����������
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByCer(String cerfile,byte[] input)throws Exception{
		 X509Certificate cer = getX509Certificate(cerfile);

		Cipher cipher = Cipher.getInstance(Transformation, Provider);
		cipher.init(Cipher.ENCRYPT_MODE, cer.getPublicKey());
		byte[] out = encrypt(cipher, input);
		if(out ==null){
			System.out.println(Transformation+ " Mode Encrypt ERROR! Return value is NULL!");
		}
		return out;
	}
	
	
	
	/**
	 * ����
	 * @param index ��Կ��ţ�1�Ŵ��� 1��ǩ����Կ��2�Ŵ���1�ż�����Կ��3�Ŵ���2��ǩ����Կ���Դ�����
	 * @param input �������ļ�
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBySwxa(int index, byte[] input)throws Exception{
		KeyPair kp = genRSAKey(index);
		Cipher cipher =  Cipher.getInstance(Transformation, Provider);
		cipher.init(Cipher.DECRYPT_MODE, kp.getPrivate());
		
		byte[]  out  = decrypt(cipher, input);		
		if(out==null){
			System.out.println(Transformation+ " Mode Decrypt ERROR! Return value is NULL!");
		}
		return out;
		
	}
	
	/**
	 * ���ܷ���������������⣩
	 * @param cipher
	 * @param input
	 * @return
	 */
	public static byte[] decrypt(Cipher cipher,byte[] input){
		byte[]  out = null;
		
		
		//��������ܳ��ȷֶ�
		int length= input.length;
		//ģ
		int mod = length/MAX_DECRYPT_BLOCK;
		//����
		int rem = length%MAX_DECRYPT_BLOCK;
		int end = rem>0?mod+1:mod;
		
		try{
			if(length<=MAX_DECRYPT_BLOCK){
				out = cipher.doFinal(input);
			}else{
				for(int i = 0;i<end;i++){
					if(i==0){
						byte[]  temp = cipher.doFinal(ByteUtil.copyBytes(input, i*MAX_DECRYPT_BLOCK, MAX_DECRYPT_BLOCK));
						 out = temp;
					}
					else if(i==end-1){
						byte[] temp = cipher.doFinal(ByteUtil.copyBytes(input, i*MAX_DECRYPT_BLOCK, (mod-end+1)*MAX_DECRYPT_BLOCK+rem));
						 out = ByteUtil.bytes2Bytes(out, temp);
					}else{
						byte[]temp = cipher.doFinal(ByteUtil.copyBytes(input, i*MAX_DECRYPT_BLOCK, MAX_DECRYPT_BLOCK));
						 out = ByteUtil.bytes2Bytes(out, temp);
					}
					
				}
			}
		}catch(Exception e){
			e.printStackTrace();	
		}
		return out;
	}
	
	
	/**
	 * ���ܷ���������������⣩
	 * @param cipher
	 * @param input
	 * @return
	 */
	public static byte[] encrypt(Cipher cipher,byte[] input){
		byte[]  out = null;
		

		int length= input.length;
		//��������ܳ��ȷֶ�
		//ģ
		int mod = length/MAX_ENCRYPT_BLOCK;
		//����
		int rem = length%MAX_ENCRYPT_BLOCK;
		int end = rem>0?mod+1:mod;
		
		try{
			if(length<=MAX_ENCRYPT_BLOCK){
				out = cipher.doFinal(input);
			}else{
				for(int i = 0;i<end;i++){
					if(i==0){
						byte[]  temp = cipher.doFinal(ByteUtil.copyBytes(input, i*MAX_ENCRYPT_BLOCK, MAX_ENCRYPT_BLOCK));
						 out = temp;
					}
					else if(i==end-1){
						byte[] temp = cipher.doFinal(ByteUtil.copyBytes(input, i*MAX_ENCRYPT_BLOCK, (mod-end+1)*MAX_ENCRYPT_BLOCK+rem));
						 out = ByteUtil.bytes2Bytes(out, temp);
					}else{
						byte[]temp = cipher.doFinal(ByteUtil.copyBytes(input, i*MAX_ENCRYPT_BLOCK, MAX_ENCRYPT_BLOCK));
						 out = ByteUtil.bytes2Bytes(out, temp);
					}
					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	/**
	 * ǩ��
	 * @throws Exception
	 */
	public static byte[] signBySwxa(int index,String algorithm,byte[] input)throws Exception{
		KeyPair kp = genRSAKey(index);
		Signature sign = Signature.getInstance(algorithm, Provider);
		sign.initSign(kp.getPrivate());
		sign.update(input);
		byte[] out = sign.sign();
		return out;	
	}
	
	/**
	 * ��ǩ
	 * @throws Exception
	 */
	public static boolean verifyBySwxa(int index,String algorithm,byte[] input,byte[] signature)throws Exception{
		KeyPair kp = genRSAKey(index);
		Signature verify = Signature.getInstance(algorithm, Provider);
		verify.initVerify(kp.getPublic());
		verify.update(input);
		boolean flag = verify.verify(signature);
		return flag;
		
		
	}
	


	
	/**
	 * ��֤����֤ǩ��
	 * @param cerfile ֤���ļ�
	 * @param userXml 
	 * @param updateData ��֤����
	 * @param sigedText ǩ��
	 * @return
	 * @throws Exception 
	 */
	public static int veriSigFile(String cerfile,byte[] all,String outPath, String userXml) throws Exception{
		int flag =  ERROR_NORMAL;
		log.info("�����ļ����ݣ���ʼ��ǩ!");
		 //Ա����ų���		
		int employeeSerialNumLength = ByteUtil.anybytes2Integer(ByteUtil.copyBytes(all, 0, size));
		//Ӳ����ų���
		int hdSerialNumLength = ByteUtil.bytes2Integer(ByteUtil.copyBytes(all, size, size));
		//���ݳ���
		 int contentLength =ByteUtil.bytes2Integer(ByteUtil.copyBytes(all, 2*size, size));
		 //Ա�����
		 byte[] employeeSerialNum = ByteUtil.copyBytes(all, 3*size, employeeSerialNumLength);
		 String  employeeSerialNumStr = new String(employeeSerialNum);
		 //У��Ա��Ȩ��
		 if(userXml!=null && !userXml.equals("")){
			 Element root = XmlParser.getRootNode(userXml);
				
				List<Element> users = XmlParser.getChildList(root);
				HashSet<String> set = new HashSet<String>();
				for(int i = 0 ;i<users.size();i++){
					String name = XmlParser.getAttribute(users.get(i), "employeeSerialNum").getValue();
					set.add(name);
					
				}
			 
			 if(!set.contains(employeeSerialNumStr)){
				 flag =  ERROR_NOPERMISSION;
				 log.error("Ա�����Ϊ��"+employeeSerialNumStr+"��Ȩ������Կ");
			 }
		 }
		 
		 if(flag == ERROR_NORMAL){
			 //Ӳ�����
			 byte[] hdSerialNum =  ByteUtil.copyBytes(all, 3*size+employeeSerialNumLength, hdSerialNumLength);
			 String hdSerialNumStr = new String(hdSerialNum);
			 log.info("Ա�����Ϊ��"+employeeSerialNumStr+"Ȩ����֤ͨ������������Կ��Ӳ�����Ϊ��"+hdSerialNumStr);
			
			//ǩ��
			 byte[] sign = ByteUtil.copyBytes(all,  3*size+employeeSerialNumLength+hdSerialNumLength+contentLength,all.length-(3*size+employeeSerialNumLength+hdSerialNumLength+contentLength));
			 
			 byte[] input = ByteUtil.copyBytes(all, 0, 3*size+employeeSerialNumLength+hdSerialNumLength+contentLength);
			 
			 byte[] content =ByteUtil.copyBytes(all, 3*size+employeeSerialNumLength+hdSerialNumLength, contentLength);
			
			 boolean result = veriSig(cerfile, input, sign);
			 //��ǩͨ������������
			 if(result){
				 if(outPath!=null && !outPath.equals("")){
					 String outfile = outPath +hdSerialNumStr+".dat";
					 //���������ļ�
					 Base64Utils.byteArrayToFile(content, outfile);
					 log.info("��ǩͨ����������Կ����"+outfile);
				 }
			 }else{
				 flag =  ERROR_VERIFYFAILED;
				 log.error("��ǩ��ͨ����ֱ�ӷ���");
			 }
		 }
	
		
		 return flag;
	}
	
	
	
	
	/**
	 * ��֤����֤ǩ��
	 * @param cerfile ֤���ļ�
	 * @param updateData ��֤����
	 * @param sigedText ǩ��
	 * @return
	 */
	public static boolean veriSig(String cerfile,byte[] updateData, byte[] sigedText){

		boolean flag = true;

	    try{  
	        CertificateFactory certificatefactory=CertificateFactory.getInstance(X509);
			FileInputStream fin=new FileInputStream(cerfile);
			X509Certificate certificate=(X509Certificate)certificatefactory.generateCertificate(fin);
		    PublicKey pub = certificate.getPublicKey();
		    Signature rsa=Signature.getInstance(certificate.getSigAlgName(),Provider);
	        rsa.initVerify(pub);
	        rsa.update(updateData);
	        flag=rsa.verify(sigedText);
	        System.out.println("verified "+flag);
	        if(flag){  		
	               System.out.println("Verify is done!");
	          }else{
	               System.out.println("verify is not successful");
	        }	    
		  }catch(Exception e){    
	            e.printStackTrace();	           	
		 }
	    return flag;
	}
	
	
	
	/**
	 * ��ȡ֤��
	 * @param filename
	 * @return
	 */
	public static X509Certificate getX509Certificate(String filename){
		X509Certificate cert = null;
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
			FileInputStream in = new FileInputStream(filename);
			cert = (X509Certificate)certificateFactory.generateCertificate(in);
			
		} catch (CertificateException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return cert;
		
	}
	
	public static Certificate getCertificate(String filename){
		Certificate cert = null;
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			FileInputStream in = new FileInputStream(filename);
			cert =certificateFactory.generateCertificate(in);
			
		} catch (CertificateException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return cert;
		
	}
	
	public static String parseCertDN(String dn, String type){
		type = type + "=";
		String[] split = dn.split(","); 
		for (String x : split) {
		    if (x.contains(type)) {
		    	x = x.trim();
		    	return x.substring(type.length());
		    }
		}
		return null;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		
//		String filePath = "C:/Users/Administrator/Desktop/ͨ�ð�2.0.15/encrypt.txt";
//		byte[] input = Base64Utils.fileToByte(filePath);
//		
//		
////		String input = "signtext";
//		String signfile = "D:/keys/signfile.dat";
//		String cryptfile = "D:/keys/cryptfile.dat";
////		byte[] out =sign(1, SHA1, input.getBytes());
////		Base64Utils.byteArrayToFile(out, outputfile);
//		
//		
//		
//		byte[] en = encryptBySwxa(2, input);
//		System.out.println(new BASE64Encoder().encode(en));
//		Base64Utils.byteArrayToFile(en, cryptfile);
//		byte[] temp = Base64Utils.fileToByte(cryptfile);
//		byte[] de = decryptBySwxa(2, temp);
//		System.out.println(new String(de));
//		
//
//		byte[] sign =  signBySwxa(1, SHA1, input);
//		Base64Utils.byteArrayToFile(en, signfile);
//		byte[] temp2 = Base64Utils.fileToByte(signfile);
//		boolean flag = verifyBySwxa(1, SHA1, input, sign);
//		System.out.println(flag);
//		
//		String cerencrptfile =  "D:/keys/cerencrptfile.dat";
//		String cerfile = "C:/Users/Administrator/Desktop/ͨ�ð�2.0.15/lichong_encrypt.cer";
//		String certest = "cerencrptfile";
//		byte[] cerByte = encryptByCer(cerfile, certest.getBytes());
//		Base64Utils.byteArrayToFile(cerByte, cerencrptfile);
		Logger logger = Logger.getLogger(SwxaJCEUtil.class);
		  // ��¼debug�������Ϣ  
        logger.debug("This is debug message.");  
        // ��¼info�������Ϣ  
        logger.info("This is info message.");  
        // ��¼error�������Ϣ  
        logger.error("This is error message."); 
		
		

		}
		

}
