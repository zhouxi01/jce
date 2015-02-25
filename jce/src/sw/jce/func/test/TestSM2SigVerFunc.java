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
 * algorithm：设置密钥对类型，一般应为“SM2”。<br>
 * provider：JCE提供者的名字，一般应为：“SwxaJCE”<br>
 * <p>
 * 2. 初始化密码生成器<br>
 * 2.1 生成外部密钥初始化<br>
 * initialize(keysize)<br>
 * 初始化密钥对生成器<br> 
 * 参数说明：<br>
 * keysize：指定生成密钥的长度，一般为256<br>
 * kpg.initialize(keysize);<br>
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
 * algorithm：签名的摘要算法，一般格式：“SHA1WithSM2”、“SHA224WithSM2”、“SHA256WithSM2”、“SHA384WithSM2”、“SHA512WithSM2”、“SHA1/SM2”<br>
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
 * algorithm：签名的摘要算法，一般格式：“SHA1WithSM2”、“SHA224WithSM2”、“SHA256WithSM2”、“SHA384WithSM2”、“SHA512WithSM2”、“SHA1/SM2”<br>
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
 * 
 * 
 * 
 * 注：
 * 	SM2签名规则：
 *	  对原始数据做指定的摘要，
 *	  1. 如果摘要值大于32字节，则取摘要的前32个字节做签名运算
 *	  2. 如果摘要值等于32字节，则直接对摘要值做签名运算
 *	  3. 如果摘要值小于32字节，则补0至32个字节，然后再对结果做签名运算
 */
public class TestSM2SigVerFunc {
	

	public static void main(String[] args) {
		Security.addProvider(new SwxaProvider());
		while (true) {
			int choice = -1;
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("++++++++++++++++++++++++ SwxaJCE API SM2 Sign Func Test +++++++++++++++++++++++++");
			System.out.println("                                                                                 ");
			System.out.println(" 1 SM2 Internal Sign And Verify Test          2 SM2 External Sign And Verify Test");
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
		Signature signature = null;
		byte[] out;
		byte[] dataInput = "ALICE123@YAHOO.COM".getBytes();
		List<String> alg = new ArrayList<String>();
		alg.add("SHA1WithSM2");
		alg.add("SHA224WithSM2");
		alg.add("SHA256WithSM2");
		alg.add("SHA384WithSM2");
		alg.add("SHA512WithSM2");
		alg.add("SHA1/SM2");
		alg.add("MD2WithSM2");
		alg.add("MD4WithSM2");
		alg.add("MD5WithSM2");
		alg.add("SM3WithSM2");
		System.out.println("Source Data : " + new String(dataInput));
		try {
			for(int i=0; i<alg.size(); i++) {
				System.out.println("Sign Algorithm [ "+alg.get(i)+" ]");
				signature = Signature.getInstance(alg.get(i), "SwxaJCE");
				//签名
				signature.initSign(privateKey);
				signature.update(dataInput);
				out = signature.sign();
				System.out.println("Sign Value : "+new BASE64Encoder().encode(out));

				//验签
				signature.initVerify(publicKey);
				signature.update(dataInput);
				boolean flag = signature.verify(out);
				
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
		KeyPair kp = TestSM2GenKeyFunc.testGenInternalKey();
		testSign(kp);
	}
	
	/**
	 * 外部密钥签名验签<br>
	 */
	public static void testExternalSign() {
		KeyPair kp = TestSM2GenKeyFunc.testGenExternalKey();
		testSign(kp);
	}
}
