package sw.jce.func.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		transf();
	}
	
	public static void kpg (){
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
			kpg.initialize(1024);
			KeyPair keyPair = kpg.generateKeyPair();
			byte[] keyBytes =keyPair.getPrivate().getEncoded(); 
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			System.out.println(privateKey);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printProviders(){
		for(Provider p:Security.getProviders()){
			System.out.println(p);
			for(Map.Entry<Object, Object> entry : p.entrySet()){
				System.out.println(entry.getKey());
			}
		}
	}
	
	public static void md5(){
		
		try {
			byte[] input = "md5".getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			DigestInputStream dis = new DigestInputStream(new ByteArrayInputStream(input),md);
			dis.read(input, 0, input.length);
			byte[] output = dis.getMessageDigest().digest();
			dis.close();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public  static void transf (){
		System.out.println(1<<16);
	}

}
