package sw.jce.func.test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;

import javax.crypto.Cipher;

import com.sansec.jce.provider.SwxaProvider;

import sw.jce.util.PKCS1Padding;
import sw.jce.util.InUtil;

/**
 * 1. 等到密钥生成器<br>
 * KeyPairGenerator.getInstance(algorithm,provider)<br>
 * 参数说明：<br>
 * algorithm：设置密钥对类型，一般应为“RSA”。<br>
 * provider：JCE提供者的名字，一般应为：“SwxaJCE”<br>
 * <p>
 * 2. 初始化密码生成器<br>
 * 2.1 生成外部密钥初始化<br>
 * initialize(keysize)<br>
 * 初始化密钥对生成器<br>
 * 参数说明：<br>
 * keysize：指定生成密钥的长度，一般为1024或者2048<br>
 * kpg.initialize(keylength);<br>
 * 2.2 生成内部密钥初始化<br>
 * initialize(keynum<<16)<br>
 * 初始化密钥对生成器<br>
 * 参数说明：<br>
 * keynum：指定生成内部密钥的密钥序号，1-100<br>
 * kpg.initialize(keynum<<16);<br>
 * <p>
 * 3. 加密<br>
 * 3.1 得到Cipher对象<br>
 * Cipher.getInstance(transformation,provider)<br>
 * 定义Cipher类的对象，用于指明加密的模式和提供着名称。<br>
 * 参数说明：<br>
 * transformation：转换的名称，一般格式：“RSA/ECB/PKCS5Padding”或“RSA/ECB/PKCS5NoPadding”<br>
 * provider：JCE提供者的名字，一般应为：“SwxaJCE”<br>
 * 3.2 初始化Cipher对象<br>
 * init(mode,key)<br>
 * 初始化Cipher类的对象<br>
 * 参数说明：<br>
 * mode：对象的操作模式，一般为Cipher.PUBLIC_KEY、Cipher.PRIVATE_KEY、Cipher.ENCRYPT_MODE、Cipher.DECRYPT_MODE<br>
 * key：用于相应的公私钥<br>
 * 3.3 做运算<br>
 * doFinal(content)<br>
 * 通过调用对content中的内容进行加密。<br>
 * 返回值：加密后的结果。<br>
 * <p>
 * 4. 解密<br>
 * 4.1 得到Cipher对象<br>
 * Cipher.getInstance(transformation,provider)<br>
 * 定义Cipher类的对象，用于指明加密的模式和提供着名称。<br>
 * 参数说明：<br>
 * transformation：转换的名称，一般格式：“RSA/ECB/PKCS5Padding”或“RSA/ECB/PKCS5NoPadding”<br>
 * provider：JCE提供者的名字，一般应为：“SwxaJCE”<br>
 * 4.2 初始化Cipher对象<br>
 * init(mode,key)<br>
 * 初始化Cipher类的对象<br>
 * 参数说明：<br>
 * mode：对象的操作模式，一般为Cipher.PUBLIC_KEY、Cipher.PRIVATE_KEY、Cipher.ENCRYPT_MODE、Cipher.DECRYPT_MODE<br>
 * key：用于相应的公私钥<br>
 * 4.3 做运算<br>
 * doFinal(content)<br>
 * 通过调用对content中的内容进行解密。<br>
 * 返回值：解密后的结果。<br>
 *
 */
public class TestRSAEncDecFunc {
	
	public static void main(String[] args) {
		Security.addProvider(new SwxaProvider());
		while (true) {
			int choice = -1;
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("++++++++++++++++++++++ SwxaJCE API RSA Encryption Func Test +++++++++++++++++++++");
			System.out.println("                                                                                 ");
			System.out.println(" 1 RSA External Padding Cryption Test      2 RSA External NoPadding Cryption Test");
			System.out.println(" 3 RSA Internal Padding Cryption Test                                            ");
			System.out.println("                                                                                 ");
			System.out.println(" 0 Return to Prev Menu                                                           ");
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			choice = InUtil.getInput("Select:", 3);
			if (choice == 0) {
				return;
			}
			if ((choice < 1) || (choice > 3)) {
				continue;
			}

			switch (choice) {
			case 1:
				testExternalPadding();
				break;
			case 2:
				testExternalNoPadding();
				break;
			case 3:
				testInternalPadding();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 外部密钥带padding加解密<br>
	 */
	public static void testExternalPadding() {
		String transformation = "RSA/ECB/PKCS1Padding";
		byte[] plain = "hello world!".getBytes();
		KeyPair kp = null;
		int keylength = -1;
		while ((keylength != 1024) && (keylength != 2048)) {
			keylength = InUtil.getInput("Please Input the Key Length (1024,2048) :", 3);
		}
		System.out.print("Create External RSA Key " + ':' + " KeyLength " + keylength + ' ' + " ... ");
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "SwxaJCE");
			kpg.initialize(keylength);
			kp = kpg.genKeyPair();
			if (kp == null) {
				System.out.println("fail！");
			} else {
				// 生成密钥成功。
				System.out.println("ok！");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		testExternalFn(kp, transformation, plain);
	}
	
	/**
	 * 外部密钥加解密<br>
	 */
	public static void testExternalNoPadding() {
		String transformation = "RSA/ECB/NoPadding";
		KeyPair kp = null;
		int keylength = -1;
		while ((keylength != 1024) && (keylength != 2048)) {
			keylength = InUtil.getInput("Please Input the Key Length (1024,2048) :", 3);
		}
		System.out.print("Create External RSA Key " + ':' + " KeyLength " + keylength + ' ' + " ... ");
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "SwxaJCE");
			kpg.initialize(keylength);
			kp = kpg.genKeyPair();
			if (kp == null) {
				System.out.println("fail！");
			} else {
				// 生成密钥成功。
				System.out.println("ok！");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		byte[] plain = PKCS1Padding.EncPadding("hello".getBytes(), keylength>>3);
		
		testExternalFn(kp, transformation, plain);
	}
	
	/**
	 * 内部密钥带padding加解密
	 */
	public static void testInternalPadding() {
		String transformation = "RSA/ECB/PKCS1Padding";
		byte[] plain = "hello world!".getBytes();
		int keynum = -1;
		KeyPair kp = null;
		while ((keynum < 1) || (keynum > 100))
			keynum = InUtil.getInput("Please Input the KeyNumber (1-100) :", 3);
		System.out.print("Create Internal RSA Key " + ':' + " KeyIndex " + keynum + ' ' + " ... ");
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "SwxaJCE");
			kpg.initialize(keynum << 16);
			kp = kpg.genKeyPair();
			if (kp == null) {
				System.out.println("fail！");
			} else {
				// 生成密钥成功。
				System.out.println("ok！");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		testInternalFn(kp, transformation, plain);
	}
	
	private static void testExternalFn(KeyPair kp, String transformation, byte[] plain) {
		Cipher cipher = null;
		try {
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
		} catch (Exception e) {
			System.out.println(transformation+ " Mode Encrypt and Decrypt ERROR!");
			e.printStackTrace();
		}
	}

	private static void testInternalFn(KeyPair kp, String transformation, byte[] plain) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(transformation, "SwxaJCE");
			cipher.init(Cipher.ENCRYPT_MODE, kp.getPrivate());
			byte[] tTemp = cipher.doFinal(plain);
			if (tTemp == null) {
				System.out.println(transformation+ " Mode Encrypt ERROR! Return value is NULL!");
			} else {
				// 定义解密Cipher类对象
				cipher = Cipher.getInstance(transformation, "SwxaJCE");
				// 初始化Cipher类对象
				cipher.init(Cipher.DECRYPT_MODE, kp.getPublic());
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
		} catch (Exception e) {
			System.out.println(transformation+ " Mode Encrypt and Decrypt ERROR!");
			e.printStackTrace();
		}
	}
}
