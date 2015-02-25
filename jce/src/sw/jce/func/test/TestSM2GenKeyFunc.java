package sw.jce.func.test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import sw.jce.util.Debug;
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
 */
public class TestSM2GenKeyFunc {
	public static void main(String[] args) {
        while(true) {
            int choice = -1;
	        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	        System.out.println("+++++++++++++++++++ SwxaJCE API Generate SM2 Keypair Func Test +++++++++++++++++++");
	        System.out.println("                                                                                 ");
	        System.out.println(" 1 Generate SM2 Internal Keypair Test        2 Generate SM2 External Keypair Test");
	        System.out.println("                                                                                 ");
	        System.out.println(" 0 Return to Prev Menu                                                           ");
	        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	        choice = InUtil.getInput("Select:", 3);
	        if(choice == 0) {
	        	return;
	        }
	        if ((choice < 0) || (choice > 2)) {
	            continue;
	        } 
	        long t1 = 0;
	        switch (choice) {
			case 1:
				t1 = System.currentTimeMillis();
				testGenInternalKey();
				Debug.println(Debug.INFO,"Time : "+( System.currentTimeMillis()-t1));
				break;
			case 2:
				t1 = System.currentTimeMillis();
				testGenExternalKey();
				Debug.println(Debug.INFO,"Time : "+( System.currentTimeMillis()-t1));
				break;
			default:
				break;
			}
        }
	}
	
	/**
	 * 生成内部密钥<br>
	 */
	public static KeyPair testGenInternalKey() {
		int keynum = -1;
		KeyPair kp = null;
		while ((keynum < 1) || (keynum > 100))
			keynum = InUtil.getInput("Please Input the KeyNumber (1-100) :", 3);
		System.out.print("Create Internal SM2 Key " + ':' + " KeyIndex " + keynum + ' ' + " ... ");
		try {
			long t1 = System.currentTimeMillis();
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("SM2", "SwxaJCE");
			kpg.initialize(keynum << 16);
			Debug.println(Debug.INFO, "Time : "+( System.currentTimeMillis()-t1));
			t1 = System.currentTimeMillis();
			kp = kpg.genKeyPair();
			Debug.println(Debug.INFO, "Time : "+( System.currentTimeMillis()-t1));
			if (kp == null) {
				System.out.println("fail！");
			} else {
				// 生成密钥成功。
				System.out.println("ok！");
				System.out.println(kp.getPublic());
				System.out.println(kp.getPrivate());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return kp;
	}
	
	/**
	 * 生成外部密钥<br>
	 */
	public static KeyPair testGenExternalKey() {
		int keylength = 256;
		KeyPair kp = null;
		while (keylength != 256) {
			keylength = InUtil.getInput("Please Input the Key Length (256) :", 3);
		}
		System.out.print("Create External SM2 Key " + ':' + " KeyLength " + keylength + ' ' + " ... ");
		try {
			long t1 = System.currentTimeMillis();
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("SM2", "SwxaJCE");
			kpg.initialize(keylength);
			Debug.println(Debug.INFO, "Time : "+( System.currentTimeMillis()-t1));
			t1 = System.currentTimeMillis();
			kp = kpg.genKeyPair();
			Debug.println(Debug.INFO, "Time : "+( System.currentTimeMillis()-t1));
			
			if (kp == null) {
				System.out.println("fail！");
			} else {
				// 生成密钥成功。
				System.out.println("ok！");
				System.out.println(kp.getPublic());
				System.out.println(kp.getPrivate());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return kp;
	}
	
}
