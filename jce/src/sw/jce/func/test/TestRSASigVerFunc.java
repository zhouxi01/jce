package sw.jce.func.test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

import com.sansec.jce.provider.SwxaProvider;

import sun.misc.BASE64Encoder;
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
 * 3. 签名<br>
 * 3.1 得到Signature对象<br>
 * Signature.getInstance(algorithm, provider);<br>
 * 定义Signature类的对象，用于指明加密的模式和提供着名称。<br>
 * 参数说明：<br>
 * algorithm：签名的摘要算法，一般格式：“SHA1WithRSA”、“SHA224WithRSA”、“SHA256WithRSA”、“SHA384WithRSA”、“SHA512WithRSA”、“SHA1/RSA”<br>
 * provider：JCE提供者的名字，一般应为：“SwxaJCE”<br>
 * 3.2 初始化Signature对象
 * initSign(privateKey);
 * 参数说明：<br>
 * privateKey： 签名的私钥<br>
 * 3.3 更新要签名的数据<br>
 * update(data);<br>
 * 参数说明：<br>
 * data： 要签名的数据<br>
 * 3.4 做运算<br>
 * sign()<br>
 * 返回值：加密后的结果。<br>
 * <p>
 * 4. 验签<br>
 * 4.1 得到Signature对象<br>
 * Signature.getInstance(algorithm, provider);<br>
 * 定义Signature类的对象，用于指明加密的模式和提供着名称。<br>
 * 参数说明：<br>
 * algorithm：签名的摘要算法，一般格式：“SHA1WithRSA”、“SHA224WithRSA”、“SHA256WithRSA”、“SHA384WithRSA”、“SHA512WithRSA”、“SHA1/RSA”<br>
 * provider：JCE提供者的名字，一般应为：“SwxaJCE”<br>
 * 4.2 初始化Signature对象<br>
 * initVerify(publicKey);<br>
 * 参数说明：<br>
 * publicKey：验签的公钥<br>
 * 4.3 更新数据<br>
 * update(data);<br>
 * 参数说明：<br>
 * data： 要验签的数据<br>
 * 4.4 做运算<br>
 * verify(signature);<br>
 * 参数说明：<br>
 * signature：签名值<br>
 * 返回值：验签的结果。<br>
 */
public class TestRSASigVerFunc {
	

	public static void main(String[] args) {
		Security.addProvider( new SwxaProvider());
		while (true) {
			int choice = -1;
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++ SwxaJCE API RSA Sign Func Test ++++++++++++++++++++++++");
			System.out.println("                                                                                 ");
			System.out.println(" 1 RSA Internal Sign And Verify Test          2 RSA External Sign And Verify Test");
			System.out.println("                                                                                 ");
			System.out.println(" 0 Return to Prev Menu                                                           ");
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			choice = InUtil.getInput("Select:", 3);
			if (choice == 0) {
				return;
			}
			if ((choice < 1) || (choice > 2)) {
				continue;
			}

			switch (choice) {
			case 1:
				testInternalSign();
				break;
			case 2:
				testExternalSign();
				break;
			default:
				break;
			}
		}
	}


	private static void testSign(KeyPair kp) {
		PrivateKey privateKey = kp.getPrivate();
		PublicKey publicKey = kp.getPublic();
		Signature signatue = null;
		byte[] out;
		byte[] dataInput = "是23132hello1231223".getBytes();
		List<String> alg = new ArrayList<String>();
		alg.add("SHA1WithRSA");
		alg.add("SHA224WithRSA");
		alg.add("SHA256WithRSA");
		alg.add("SHA384WithRSA");
		alg.add("SHA512WithRSA");
		alg.add("SHA1/RSA");
		System.out.println("Source Data : " + new String(dataInput));
		try {
			for(int i=0; i<alg.size(); i++) {
				System.out.println("Sign Algorithm [ "+alg.get(i)+" ]");
				signatue = Signature.getInstance(alg.get(i), "SwxaJCE");
				//签名
				signatue.initSign(privateKey);
				signatue.update(dataInput);
				out = signatue.sign();
				System.out.println("Sign Value : "+new BASE64Encoder().encode(out));
				//验签
				signatue.initVerify(publicKey);
				signatue.update(dataInput);
				boolean flag = signatue.verify(out);
				
				System.out.println("Verify Result: "+flag);
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 内部密钥签名验签<br>
	 */
	public static void testInternalSign() {
		KeyPair kp = TestRSAGenKeyFunc.testGenInternalKey();
		testSign(kp);
	}
	
	/**
	 * 外部密钥签名验签<br>
	 */
	public static void testExternalSign() {
		KeyPair kp = TestRSAGenKeyFunc.testGenExternalKey();
		testSign(kp);
	}
}
