package util;

public class ByteUtil {
	  public static void main(String[] args)
	  {
	   

	   
	   byte[] b1 = new byte[20];
	   byte[] b2 = new byte[3];
	   b1[0] = (byte) 0x01;
	   b1[1] = (byte) 0x02;
	   b1[2] = (byte) 0x55;
	   b1[3] = (byte) 0x04;
	   b1[4] = (byte) 0x55;
	   b1[5] = (byte) 0x06;
	   b1[6] = (byte) 0x07;
	   b1[7] = (byte) 0x55;
	   b1[8] = (byte) 0xff;
	   b1[9] = (byte) 0x0a;
	   b1[10] = (byte) 0x0b;
	   b1[11] = (byte) 0x0c;
	   b1[12] = (byte) 0x0d;
	   b1[13] = (byte) 0x0e;
	   b1[14] = (byte) 0x0f;
	   b1[15] = (byte) 0x55;
	   b1[16] = (byte) 0xff;
	   b1[17] = (byte) 0xee;
	   b1[18] = (byte) 0x13;
	   b1[19] = (byte) 0x14;
	   b2[0] = (byte) 0x55;
	   b2[1] = (byte) 0xff;
	   b2[2] = (byte) 0xee;  
	   System.out.println(indexOfBytes(b1,b2));
	  }
	  
	  /**
	   * 整数转四字节二进制
	   * @param intValue
	   * @return
	   */
	  public static byte[] integer2Bytes(int intValue)
	  {
	   byte[] result = new byte[4];
	   result[0] = (byte) ((intValue & 0xFF000000) >> 24);
	   result[1] = (byte) ((intValue & 0x00FF0000) >> 16);
	   result[2] = (byte) ((intValue & 0x0000FF00) >> 8);
	   result[3] = (byte) ((intValue & 0x000000FF));
	   return result;
	  }
	  
	  
	  /**
	   * 整数转双字节二进制
	   * @param intValue
	   * @return
	   */
	  public static byte[] get2Bytes(int intValue) {
	   byte[] tmp = integer2Bytes(intValue);//获取4个字节
	   byte[] result = new byte[2];
	   result[0] = tmp[2];
	   result[1] = tmp[3];
	   return result;
	  }
	  /**
	   * 整数转单字节二进制
	   * @param intValue
	   * @return
	   */
	  public static byte[] get1Bytes(int intValue) {
	   byte[] tmp = integer2Bytes(intValue);//获取4个字节
	   byte[] result = new byte[1];
	   result[0] = tmp[3];
	   return result;
	  }
	  
	  /**
	   * 二进制转整数
	   * @param byteVal
	   * @return
	   */
	  public static int bytes2Integer(byte[] byteVal)
	  {
	   int result = 0;
	   for (int i = 0; i < byteVal.length; i++)
	   {
	    int tmpVal = (byteVal[i] << (8 * (3 - i)));
	    switch (i)
	    {
	     case 0:
	      tmpVal = tmpVal & 0xFF000000;
	      break;
	     case 1:
	      tmpVal = tmpVal & 0x00FF0000;
	      break;
	     case 2:
	      tmpVal = tmpVal & 0x0000FF00;
	      break;
	     case 3:
	      tmpVal = tmpVal & 0x000000FF;
	      break;
	    }
	    result = result | tmpVal;
	   }
	   return result;
	  }
	  
	  public static int anybytes2Integer(byte[] byteVal)
	  { 
	   int len = byteVal.length;
	   if(len>4)
	   {
	    return 0;
	   }
	   byte[]tmp = {0,0,0,0};
	   for(int i = 0;i<len;i++)
	   {
	    tmp[3-i] = byteVal[len-1-i];
	   }
	   
	   int result = 0;
	   for (int i = 0; i < tmp.length; i++)
	   {
	    int tmpVal = (tmp[i] << (8 * (3 - i)));
	    switch (i)
	    {
	     case 0:
	      tmpVal = tmpVal & 0xFF000000;
	      break;
	     case 1:
	      tmpVal = tmpVal & 0x00FF0000;
	      break;
	     case 2:
	      tmpVal = tmpVal & 0x0000FF00;
	      break;
	     case 3:
	      tmpVal = tmpVal & 0x000000FF;
	      break;
	    }
	    result = result | tmpVal;
	   }
	   return result;
	  }
	  
	   public static void putShort(byte b[], short s, int index) {
	          b[index] = (byte) (s >> 8);
	          b[index + 1] = (byte) (s >> 0);
	      }
	  
	   
	      public static int getOneByteValue(byte b) {
	          return (int) (( b& 0xff));
	      }
	  
	      public static short getShort(byte[] b, int index) {
	          return (short) (((b[index] << 8) | b[index + 1] & 0xff));
	      } 
	     
	      // ///////////////////////////////////////////////////////
	      public static void putInt(byte[] bb, int x, int index) {
	          bb[index + 0] = (byte) (x >> 24);
	          bb[index + 1] = (byte) (x >> 16);
	          bb[index + 2] = (byte) (x >> 8);
	          bb[index + 3] = (byte) (x >> 0);
	      }
	     
	     
	      public static int getInt(byte[] bb, int index) {
	          return (int) ((((bb[index + 0] & 0xff) << 24)
	                  | ((bb[index + 1] & 0xff) << 16)
	                  | ((bb[index + 2] & 0xff) << 8) | ((bb[index + 3] & 0xff) << 0)));
	      }
	     
	      // /////////////////////////////////////////////////////////
	      public static void putLong(byte[] bb, long x, int index) {
	          bb[index + 0] = (byte) (x >> 56);
	          bb[index + 1] = (byte) (x >> 48);
	          bb[index + 2] = (byte) (x >> 40);
	          bb[index + 3] = (byte) (x >> 32);
	          bb[index + 4] = (byte) (x >> 24);
	          bb[index + 5] = (byte) (x >> 16);
	          bb[index + 6] = (byte) (x >> 8);
	          bb[index + 7] = (byte) (x >> 0);
	      }
	     
	      public static long getLong(byte[] bb, int index) {
	          return ((((long) bb[index + 0] & 0xff) << 56)
	                  | (((long) bb[index + 1] & 0xff) << 48)
	                  | (((long) bb[index + 2] & 0xff) << 40)
	                  | (((long) bb[index + 3] & 0xff) << 32)
	                  | (((long) bb[index + 4] & 0xff) << 24)
	                  | (((long) bb[index + 5] & 0xff) << 16)
	                  | (((long) bb[index + 6] & 0xff) << 8)
	                  | (((long) bb[index + 7] & 0xff) << 0));
	      }
	     
	     
	      public static byte[] getNewByteByTwo(byte[] byte1, byte[] byte2) {
	       byte[] returnValue = new byte[byte1.length + byte2.length];
	       System.arraycopy( byte1, 0, returnValue, 0, byte1.length );
	    System.arraycopy( byte2, 0, returnValue, byte1.length, byte2.length );
	       return returnValue;
	      }
	     
	     
	      public static String byte2hex(byte[] b) {
	       String hs="";
	       String stmp="";
	       for (int n=0;n<b.length;n++) {
	        stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
	        
	        hs = hs + stmp;
	       }
	       return hs.toUpperCase();
	      }   
	     
	      public static void printHexString(String hint, byte[] b) {
	       for (int i = 0; i < b.length; i++) {
	        String hex = Integer.toHexString(b[i] & 0xFF);
	           if (hex.length() == 1) {
	            hex = '0' + hex;
	           }
	           System.out.print(hex.toUpperCase() + " ");
	       }
	      }
	     
	      public static String bytes2HexString(byte[] b) {
	       String ret = "";
	       for (int i = 0; i < b.length; i++) {
	        String hex = Integer.toHexString(b[i] & 0xFF);
	        if (hex.length() == 1) {
	         hex = '0' + hex;
	        }
	        ret += hex.toUpperCase();
	       }
	       return ret;
	      }
	     
	      public static byte uniteBytes(byte src0, byte src1) {
	       byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
	       _b0 = (byte)(_b0 << 4);
	       byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
	       byte ret = (byte)(_b0 ^ _b1);
	       return ret;
	      }
	     
	      public static byte[] hexString2Bytes(String src){
	       byte[] ret = new byte[src.length()/2];
	       byte[] tmp = src.getBytes();
	       for(int i=0; i<src.length()/2; i++){
	        ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]);
	       }
	       return ret;
	      }   
	     
	      /**
	       * byte[] 合并
	       * @param b1
	       * @param b2
	       * @return
	       */
	      public static byte[] bytes2Bytes(byte[] b1,byte[] b2){
	       byte[] ret = new byte[b1.length+b2.length];
	       System.arraycopy(b1, 0, ret, 0, b1.length);
	       System.arraycopy(b2, 0, ret, b1.length, b2.length);
	       return ret;
	      }    
	     
	      /**
	       * byte[]截取
	       * @param b1 原始内容
	       * @param start 起点
	       * @param length 需要截取的长度
	       * @return
	       */
	      public static byte[] copyBytes(byte[] b1,int start,int length){
	       byte[] ret = new byte[length];
	       if (start + length > b1.length) return null;      
	       System.arraycopy(b1, start, ret, 0, length);
	       return ret;
	      }     
	     
	      public static int indexOfBytes(byte[] b1,byte[] b2){
	       int ret = -1;
	       if (null == b1 || null == b2) return ret;
	       if (b1.length < b2.length) return ret;
	       byte[] newByte = new byte[b2.length];
	       for (int i = 0;i < b1.length;i++) {
	        newByte = copyBytes(b1,i,b2.length);
	        if (null != newByte && newByte.length == b2.length) {
	         int j=0;
	         for (j=0;j < newByte.length;j++) {
	          if (newByte[j] != b2[j]) break;
	         }
	         if (j == b2.length) {
	          ret = i;
	          break;
	         }
	        }
	       }   
	       return ret;
	      } 
}
