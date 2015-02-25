package sw.jce.func.test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.sansec.jce.provider.SwxaProvider;

import sun.misc.BASE64Encoder;

public class Demo {
	public static void main(String[] args) throws Exception {
		testRSASign();
	}
	public static String testRandom() throws Exception {
		SecureRandom random = SecureRandom.getInstance("RND", "SwxaJCE");
		int length = random.nextInt(819);
		//生成数据数最大长度为 8kB(8192B)
		byte[] tmp = random.generateSeed(length);
		System.out.println(new BASE64Encoder().encode(tmp));
		return new BASE64Encoder().encode(tmp);
	}
	
	public static KeyPair testGenRSAKey() throws Exception {
		KeyPair kp = null;
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "SwxaJCE");
		//int keysize = 1024、 2048、 3072、 4096、 n<<16(n为密钥序号）
		/*
		// 初始化产生1024长度密钥
		int keysize = 1024; 
		kpg.initialize(keysize);
		*/
		/*
		// 初始化产生密码机1号密钥
		int keysize = 1<<16; 
		kpg.initialize(keysize);
		 */
		// 初始化产生密码机2号密钥
		kpg.initialize(2<<16);
		
		kp = kpg.genKeyPair();
		if (kp == null) {
			System.out.println("产生RSA密钥对失败！");
		} else {
			// 生成密钥成功。
			System.out.println(kp.getPublic());
			System.out.println(kp.getPrivate());
		}
		
		return kp;
	}
	
	public static void testRSACipher() throws Exception {
		// 生成RSA密钥对
		KeyPair kp = testGenRSAKey();
		String transformation = "";
		byte[] plain = "hello".getBytes();
		Cipher cipher = null;
		/*
		 // 以transformation = "RSA/ECB/NoPadding" 模式初始化得到的Cipher对象 运算前后都不对输入数据做处理（要求输入数据并须是模长，否则出错）
		 cipher = Cipher.getInstance("RSA/ECB/NoPadding", "SwxaJCE");
		 */
		// 以transformation = "RSA/ECB/PKCS1Padding"模式初始化得到的Cipher对象 加密运算前对明文做PKCS1 padding 加密运算后对加密出的数据unpadding
		transformation = "RSA/ECB/PKCS1Padding";
		//long t1 = System.currentTimeMillis();
		cipher = Cipher.getInstance(transformation, "SwxaJCE");
		cipher.init(Cipher.ENCRYPT_MODE, kp.getPublic());
		byte[] tTemp = cipher.doFinal(plain);
		if (tTemp == null) {
			System.out.println(transformation+ " Mode Encrypt ERROR! Return value is NULL!");
		} else {
			// 定义解密Cipher类对象
			cipher = Cipher.getInstance(transformation, "SwxaJCE");
			// 初始化Cipher类对象
			cipher.init(Cipher.DECRYPT_MODE, kp.getPrivate());
			// 调用解密函数
			byte[] tResult = cipher.doFinal(tTemp);

			if (tResult == null) {
				System.out.println(transformation+ " Mode Decrypt ERROR! Return value is NULL!");
			}
			// 比较结果
			if (new String(plain).equals(new String(tResult)))
				System.out.println(transformation+ " Mode Encrypt and Decrypt Success!");
			else
				System.out.println(transformation+ " Mode Encrypt and Decrypt ERROR!");
		}
	}
	
	public static void testRSASign() throws Exception {
		// 生成RSA密钥对
		KeyPair kp = testGenRSAKey();
		PrivateKey privateKey = kp.getPrivate();
		PublicKey publicKey = kp.getPublic();
		Signature signatue = null;
		byte[] out;
		byte[] dataInput = "TestForVerify".getBytes();
		List<String> alg = new ArrayList<String>();
		/**
		 * RSA签名算法支持 SHA1、SHA224、SHA256、SHA384、SHA512、MD2、MD4、MD5
		 */
		alg.add("SHA1WithRSA");		//SHA1
//		alg.add("SHA1/RSA");		//SHA1
//		alg.add("SHA224WithRSA");	//SHA224
//		alg.add("SHA256WithRSA");	//SHA256
//		alg.add("SHA384WithRSA");	//SHA384
//		alg.add("SHA512WithRSA");	//SHA512
//		alg.add("MD2WithRSA");		//MD2
//		alg.add("MD4WithRSA");		//MD4
//		alg.add("MD5WithRSA");		//MD5
		System.out.println("Source Data : " + new String(dataInput));
		for(int i=0; i<alg.size(); i++) {
			System.out.println("Sign Algorithm [ "+alg.get(i)+" ]");
			signatue = Signature.getInstance(alg.get(i), "SwxaJCE");
			//签名
			signatue.initSign(privateKey);
			signatue.update(dataInput);
			out = signatue.sign();
			int len = out.length;
			System.out.println("Sign Value : "+new BASE64Encoder().encode(out));
			//验签
			signatue.initVerify(publicKey);
			signatue.update(dataInput);
			boolean flag = signatue.verify(out);
			
			System.out.println("Verify Result: "+flag);
			System.out.println();
		}
	}
	
	public static KeyPair testGenSM2Key() throws Exception {
		KeyPair kp = null;
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("SM2", "SwxaJCE");
		//int keysize = 256、 n<<16(n为密钥序号）
		/*
		// 初始化产生256长度密钥
		int keysize = 256; 
		kpg.initialize(keysize);
		*/
		/*
		// 初始化产生密码机1号密钥
		int keysize = 1<<16; 
		kpg.initialize(keysize);
		 */
		
		// 初始化产生密码机2号密钥
		kpg.initialize(2<<16);
		
		kp = kpg.genKeyPair();
		if (kp == null) {
			System.out.println("产生SM2密钥对失败！");
		} else {
			// 生成密钥成功。
			System.out.println(kp.getPublic());
			System.out.println(kp.getPrivate());
		}
		
		return kp;
	}
	
	public static void testSM2Cipher() throws Exception {
		// 生成SM2密钥对
		KeyPair kp = testGenSM2Key();
		String transformation = "";
		byte[] plain = "hello".getBytes();
		Cipher cipher = null;
		// 以transformation = "SM2"模式初始化得到的Cipher对象 做SM2加解密运算密文数据为国密SM2应用规范中的密文机构
		transformation = "SM2";
		cipher = Cipher.getInstance(transformation, "SwxaJCE");
		cipher.init(Cipher.ENCRYPT_MODE, kp.getPublic());
		byte[] tTemp = cipher.doFinal(plain);
		if (tTemp == null) {
			System.out.println(transformation+ " Mode Encrypt ERROR! Return value is NULL!");
		} else {
			// 定义解密Cipher类对象
			cipher = Cipher.getInstance(transformation, "SwxaJCE");
			// 初始化Cipher类对象
			cipher.init(Cipher.DECRYPT_MODE, kp.getPrivate());
			// 调用解密函数
			byte[] tResult = cipher.doFinal(tTemp);

			if (tResult == null) {
				System.out.println(transformation+ " Mode Decrypt ERROR! Return value is NULL!");
			}
			// 比较结果
			if (new String(plain).equals(new String(tResult)))
				System.out.println(transformation+ " Mode Encrypt and Decrypt Success!");
			else
				System.out.println(transformation+ " Mode Encrypt and Decrypt ERROR!");
		}
	}
	
	public static void testSM2Sign() throws Exception {
		// 生成SM2密钥对
		KeyPair kp = testGenSM2Key();
		PrivateKey privateKey = kp.getPrivate();
		PublicKey publicKey = kp.getPublic();
		Signature signature = null;
		byte[] out;
		byte[] dataInput = "北京三未信安".getBytes();
		List<String> alg = new ArrayList<String>();
		/**
		 * SM2签名算法支持 SHA1、SHA224、SHA256、SM3
		 */
		alg.add("SHA1WithSM2");		//SHA1
		alg.add("SHA1/SM2");		//SHA1
		alg.add("SHA224WithSM2");	//SHA224
		alg.add("SHA256WithSM2");	//SHA256
		alg.add("SM3WithSM2");		//SM3

		System.out.println("Source Data : " + new String(dataInput));
		for(int i=0; i<alg.size(); i++) {
			System.out.println("Sign Algorithm [ "+alg.get(i)+" ]");
			signature = Signature.getInstance(alg.get(i), "SwxaJCE");
			//签名
			signature.initSign(privateKey);
			signature.update(dataInput);
			out = signature.sign();
			//PrintUtil.printWithHex(out);
			System.out.println("Sign Value : "+new BASE64Encoder().encode(out));

			//验签
			signature.initVerify(publicKey);
			signature.update(dataInput);
			boolean flag = signature.verify(out);
			
			System.out.println("Verify Result: "+flag);
			System.out.println();
		}
	}
	
	
	public static void testSM1Cipher() throws Exception {
		// 生成SM1密钥
		KeyGenerator kg = KeyGenerator.getInstance("SM1", "SwxaJCE");
		//int keysize = 128、 n<<16(n为密钥序号）
		/*
		// 初始化产生128长度密钥
		int keysize = 128; 
		kpg.initialize(keysize);
		*/
		/*
		// 初始化产生密码机1号密钥
		int keysize = 1<<16; 
		kpg.initialize(keysize);
		 */
	
		// 初始化产生密码机1号密钥
		kg.init(1<<16);
		SecretKey key = kg.generateKey();
		String transformation = "";
		byte[] plain = "hello".getBytes();
		Cipher cipher = null;
		// 以transformation = "SM1/ECB/PKCS5Padding"模式初始化得到的Cipher对象 加密运算前对明文做PKCS5 padding(明文最后一个分组缺几补几，刚好合适补分组长度16) 加密运算后对加密出的数据unpadding
		// 支持ECB、CBC
		transformation = "SM1/CBC/PKCS5Padding";
		cipher = Cipher.getInstance(transformation, "SwxaJCE");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] cipherText = cipher.doFinal(plain);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] tResult = cipher.doFinal(cipherText);
		if (tResult == null) {
			System.out.println(transformation+ " Mode Decrypt ERROR! Return value is NULL!");
		}
		// 比较结果
		if (new String(plain).equals(new String(tResult))) {
			System.out.println(transformation+ " Mode Encrypt and Decrypt Success!");
		} else { 
			System.out.println(transformation+ " Mode Encrypt and Decrypt ERROR!");
		}
	}
}
