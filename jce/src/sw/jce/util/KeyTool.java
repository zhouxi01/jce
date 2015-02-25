package sw.jce.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

import sun.misc.BASE64Encoder;

import com.sansec.asn1.ASN1InputStream;
import com.sansec.asn1.ASN1Sequence;
import com.sansec.asn1.DERBitString;
import com.sansec.asn1.DERObject;
import com.sansec.asn1.pkcs.CertificationRequest;
import com.sansec.asn1.pkcs.CertificationRequestInfo;
import com.sansec.asn1.x509.AlgorithmIdentifier;
import com.sansec.asn1.x509.SubjectPublicKeyInfo;
import com.sansec.asn1.x509.X509Name;
import com.sansec.jce.provider.SwxaProvider;


/**
 * @author 陈明 E-mail:chenming@sansec.com.cn
 * @version 创建时间：2012-3-5 上午10:34:00
 * 
 */

public class KeyTool {
	public static final String PROVIDER = "SwxaJCE";
	static KeyStore ks = null;
	
	public  static void main(String[] args) throws Exception {
		Security.addProvider(new SwxaProvider());
		while (true) {
			int n = 0;
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("    1  生成JKS1内存对象                       ");
			System.out.println("    2  测试JKS1 相关函数           ");
			System.out.println("    0  退出                                         ");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
			n = getInt("Select : ");
			switch (n) {
			case 0:
				System.exit(0);
			case 1:
				genKeyStore();
				break;
			case 2:
				getAliases();
				break;
			case 3:
				//importRootCert();
				break;
			case 4:
				//importServerCert();
				break;
			case 5:
				//listCert();
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * 取得一个数字
	 * @param prompt
	 * @return
	 */
	private static int getInt(String prompt) {
		while(true) {
			try {
				Scanner scanner = new Scanner(System.in);
				System.out.print(prompt);
				return scanner.nextInt();
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}

	/**
	 * 产生KeyStore
	 * @throws Exception 
	 */
	public  static  void genKeyStore() throws Exception {
		System.out.print("产生JKS文件[  ] ... ");
		//FileOutputStream stream = new FileOutputStream(JKS_FILE_NAME);
		if ( ks == null) {
		ks = KeyStore.getInstance("SWKS1", PROVIDER);
		ks.load(null, null); //PASSWORD.toCharArray()
		}
		
		/*
		String alias = null;
		for(Enumeration<String> e = ks.aliases(); e.hasMoreElements(); ) {
			alias = e.nextElement();
			System.out.println(ks.getCertificate(alias));
		}  */
		KeyPairGenerator keyPairGenerator;
		
        Enumeration<String>   enumAliases=null;
        enumAliases = ks.aliases();
		while (enumAliases.hasMoreElements()) {
			String msg = (String) enumAliases.nextElement();
			System.out.println(msg);
			System.out.println(ks.getCertificate(msg).getPublicKey().getAlgorithm());
			System.out.println(ks.getCertificate(msg).getPublicKey().getFormat());
			System.out.println(PrintUtil.toHexString(ks.getCertificate(msg).getPublicKey().getEncoded()));
			System.out.println(ks.getCertificate(msg).toString());
			//keyPairGenerator = KeyPairGenerator.getInstance("RSA", "SwxaJCE");
			//int keyNum = Integer.valueOf(msg);
			//keyPairGenerator.initialize(keyNum << 16);
			//KeyPair keyPair = keyPairGenerator.generateKeyPair();
			//System.out.println(PrintUtil.toHexString(keyPair.getPublic().getEncoded()));
		}
		
	}

	public  static  void getAliases() throws Exception {
		if ( ks != null) {
		System.out.println("ks.size()="+ks.size());
		System.out.println("ks.containsAlias(RSAKey1)"+ks.containsAlias("RSAKey1"));
		System.out.println("ks.getCertificate(RSAKey9)"+ks.getCertificate("RSAKey9").getPublicKey().getAlgorithm());
		
		System.out.println("ks.getCertificateAlias="+ks.getCertificateAlias(ks.getCertificate("RSAKey5")));
		/*
		System.out.println("RSAKey9");
		System.out.println(ks.getCertificate("RSAKey9").getPublicKey().getAlgorithm());
		System.out.println(ks.getCertificate("RSAKey9").getPublicKey().getFormat());
		System.out.println(PrintUtil.toHexString(ks.getCertificate("RSAKey9").getPublicKey().getEncoded()));
		System.out.println(ks.getCertificate("RSAKey9").toString());
		
		System.out.println("SM2Key9");
		System.out.println(ks.getCertificate("SM2Key9").getPublicKey().getAlgorithm());
		System.out.println(ks.getCertificate("SM2Key9").getPublicKey().getFormat());
		System.out.println(PrintUtil.toHexString(ks.getCertificate("SM2Key9").getPublicKey().getEncoded()));
		System.out.println(ks.getCertificate("SM2Key9").toString());*/
		}else {
			System.out.println("ks is null, 请先运行-> 1.生成JKS文件 ");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
