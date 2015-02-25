package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.Date;

/**
 *
 * @����: FileUtil
 * @����: TODO
 * @����: 2012-3-1 ����04:58:31
 */

public class FileUtil
{
	/**
	* ���ֽ�Ϊ��λ��ȡ�ļ��������ڶ��������ļ�����ͼƬ��������Ӱ����ļ���
	* @param fileName    �ļ�����
	*/
	public static void readFileByBytes ( String fileName )
	{
		File file = new File ( fileName );
		InputStream in = null;
		byte[] str = null;
		try
		{
			System.out.println ( "���ֽ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���ֽڣ�" );
// һ�ζ�һ���ֽ�
			
			in = new FileInputStream ( file );
			int tempbyte;
			
			while ( ( tempbyte = in.read() ) != -1 )
			{
				System.out.write ( tempbyte );
		
			}
			in.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
			return;
		}
		try
		{
			System.out.println ( "���ֽ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�����ֽڣ�" );
// һ�ζ�����ֽ�
			byte[] tempbytes = new byte[100];
			int byteread = 0;
			in = new FileInputStream ( fileName );
			FileUtil.showAvailableBytes ( in );
// �������ֽڵ��ֽ������У�bytereadΪһ�ζ�����ֽ���
			while ( ( byteread = in.read ( tempbytes ) ) != -1 )
			{
				System.out.write ( tempbytes, 0, byteread );
			}
		}
		catch ( Exception e1 )
		{
			e1.printStackTrace();
		}
		finally
		{
			if ( in != null )
			{
				try
				{
					in.close();
				}
				catch ( IOException e1 )
				{
				}
			}
		}
	}

	/**
	* ���ַ�Ϊ��λ��ȡ�ļ��������ڶ��ı������ֵ����͵��ļ�
	*
	* @param fileName
	*            �ļ���
	*/
	public static void readFileByChars ( String fileName )
	{
		File file = new File ( fileName );
		Reader reader = null;
		try
		{
			System.out.println ( "���ַ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���ֽڣ�" );
// һ�ζ�һ���ַ�
			reader = new InputStreamReader ( new FileInputStream ( file ) );
			int tempchar;
			while ( ( tempchar = reader.read() ) != -1 )
			{
// ����windows�£�rn�������ַ���һ��ʱ����ʾһ�����С�
// ������������ַ��ֿ���ʾʱ���ỻ�����С�
// ��ˣ����ε�r����������n�����򣬽������ܶ���С�
				if ( ( ( char ) tempchar ) != 'r' )
				{
					System.out.print ( ( char ) tempchar );
				}
			}
			reader.close();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		try
		{
			System.out.println ( "���ַ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�����ֽڣ�" );
// һ�ζ�����ַ�
			char[] tempchars = new char[30];
			int charread = 0;
			reader = new InputStreamReader ( new FileInputStream ( fileName ) );
// �������ַ����ַ������У�charreadΪһ�ζ�ȡ�ַ���
			while ( ( charread = reader.read ( tempchars ) ) != -1 )
			{
// ͬ�����ε�r����ʾ
				if ( ( charread == tempchars.length )
				        && ( tempchars[tempchars.length - 1] != 'r' ) )
				{
					System.out.print ( tempchars );
				}
				else
				{
					for ( int i = 0; i < charread; i++ )
					{
						if ( tempchars[i] == 'r' )
						{
							continue;
						}
						else
						{
							System.out.print ( tempchars[i] );
						}
					}
				}
			}
		}
		catch ( Exception e1 )
		{
			e1.printStackTrace();
		}
		finally
		{
			if ( reader != null )
			{
				try
				{
					reader.close();
				}
				catch ( IOException e1 )
				{
				}
			}
		}
	}

	/**
	* ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
	*
	* @param fileName
	*            �ļ���
	*/
	public static String readFileByLines ( String fileName )
	{
		StringBuffer str = new StringBuffer();
		File file = new File ( fileName );
		BufferedReader reader = null;
		try
		{
			System.out.println ( "����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�" );
			reader = new BufferedReader ( new FileReader ( file ) );
			String tempString = null;
			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ( ( tempString = reader.readLine() ) != null )
			{
			// ��ʾ�к�
				System.out.println ( "line " + line + ": " + tempString );
				str.append(tempString);
				line++;
			}
			reader.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			if ( reader != null )
			{
				try
				{
					reader.close();
				}
				catch ( IOException e1 )
				{
				}
			}
		}
		return str.toString();
	}

	/**
	* �����ȡ�ļ�����
	*
	* @param fileName
	*            �ļ���
	*/
	public static void readFileByRandomAccess ( String fileName )
	{
		RandomAccessFile randomFile = null;
		try
		{
			System.out.println ( "�����ȡһ���ļ����ݣ�" );
// ��һ����������ļ�������ֻ����ʽ
			randomFile = new RandomAccessFile ( fileName, "r" );
// �ļ����ȣ��ֽ���
			long fileLength = randomFile.length();
// ���ļ�����ʼλ��
			int beginIndex = ( fileLength > 4 ) ? 4 : 0;
// �����ļ��Ŀ�ʼλ���Ƶ�beginIndexλ�á�
			randomFile.seek ( beginIndex );
			byte[] bytes = new byte[10];
			int byteread = 0;
// һ�ζ�10���ֽڣ�����ļ����ݲ���10���ֽڣ����ʣ�µ��ֽڡ�
// ��һ�ζ�ȡ���ֽ�������byteread
			while ( ( byteread = randomFile.read ( bytes ) ) != -1 )
			{
				System.out.write ( bytes, 0, byteread );
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			if ( randomFile != null )
			{
				try
				{
					randomFile.close();
				}
				catch ( IOException e1 )
				{
				}
			}
		}
	}

	/**
	* ��ʾ�������л�ʣ���ֽ���
	*
	* @param in
	*/
	private static void showAvailableBytes ( InputStream in )
	{
		try
		{
			System.out.println ( "��ǰ�ֽ��������е��ֽ���Ϊ:" + in.available() );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public static void main ( String[] args )
	{
		String fileName = "C:/temp/newTemp.txt";
		appendMethodA(fileName, "dfsfsd");
		
		readFileByBytes(fileName);

	}

	/**
	* A����׷���ļ���ʹ��RandomAccessFile
	*
	* @param fileName
	*            �ļ���
	* @param content
	*            ׷�ӵ�����
	*/
	public static void appendMethodA ( String fileName, String content )
	{
		try
		{
// ��һ����������ļ���������д��ʽ
			RandomAccessFile randomFile = new RandomAccessFile ( fileName, "rw" );
// �ļ����ȣ��ֽ���
			long fileLength = randomFile.length();
// ��д�ļ�ָ���Ƶ��ļ�β��
			randomFile.seek ( fileLength );
			randomFile.writeBytes ( content );
			randomFile.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * ��д�ļ�����
	 * @param fileName
	 * @param content
	 */
	public static void reWriteFile ( String fileName, String content )
	{
		try
		{
// ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
			FileWriter writer = new FileWriter ( fileName, false );
			writer.write ( content );
			writer.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	/**
	* B����׷���ļ���ʹ��FileWriter
	*
	* @param fileName
	* @param content
	*/
	public static void appendMethodB ( String fileName, String content )
	{
		try
		{
// ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
			FileWriter writer = new FileWriter ( fileName, true );
			writer.write ( content );
			writer.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public static void main1 ( String[] args )
	{
		String fileName = "C:/temp/newTemp.txt";
		String content = "new append!";
// ������A׷���ļ�
// AppendToFile.appendMethodA(fileName, content);
// AppendToFile.appendMethodA(fileName, "append end. n");
// ��ʾ�ļ�����
// ReadFromFile.readFileByLines(fileName);
// ������B׷���ļ�
// AppendToFile.appendMethodB(fileName, content);
// AppendToFile.appendMethodB(fileName, "append end. n");
// ��ʾ�ļ�����
// ReadFromFile.readFileByLines(fileName);
	}
	
	
	  /**
	   *  �ļ����ƺ���������������ȫ·���ļ���������������󣬽��׳�FileCopyException�쳣
	   * @param source_name String
	   * @param dest_name String
	   * @param type int
	   * Ŀ���ļ�������� ����1�� ���� ��2������һ���µ��ļ��� 3������������
	   * @throws IOException
	   * @return int
	   */
	  @SuppressWarnings("finally")
	public static String copy(String source_name, String dest_name, int type) throws
	      IOException {
	    File source_file = new File(source_name);

	    File dest_file = new File(dest_name);
	    String destination_fileName = dest_name.substring(0,
	        dest_name.indexOf(dest_file.getName())) + source_file.getName();
	    File destination_file = new File(destination_fileName);
	    FileInputStream source = null;
	    FileOutputStream destination = null;
	    byte[] buffer;
	    int bytes_read;
	    int result = 0;
	    try {
	      if (!source_file.exists() || !source_file.isFile()) {
	        throw new FileCopyException("FileCopy: no such source file: " +
	                                    source_name);
	      }
	      if (!source_file.canRead()) {
	        throw new FileCopyException("FileCopy: source file " +
	                                    "is unreadable: " + source_name);
	      }

	      if (destination_file.exists()) {
	        if (destination_file.isFile()) {
	          //Ŀ���ļ� ����1 ���� 2����һ���µ��ļ� 3����������
	          //destination_file.delete();
	          if (type == 1) {
	            destination_file.delete();
	            result = 1;
	          }
	          else if (type == 2) {
	            String NewDesc_Name = dest_name.substring(0, dest_name.length() - 4);
	            String EndName = dest_name.substring( (dest_name.length() - 3));
//	            NewDesc_Name +="_"+ new Date()+ "." + EndName;	            
	            NewDesc_Name +="_"+ new java.text.SimpleDateFormat("yyyy-MM-dd").
	                format(new Date()) + "." + EndName;
	            dest_name = NewDesc_Name;
	            destination_file = new File(NewDesc_Name);
	            result = 2;
	          }
	          else {
	            result = 3;
	          }
	        }
	        else {
	          throw new FileCopyException("FileCopy: destination "
	                                      + "is not a file: " + dest_name);
	        }
	      }
	      else {
	        File parentdir = parent(destination_file);
	        if (!parentdir.exists()) {
	          throw new FileCopyException("FileCopy: destination "
	                                      + "directory doesn't exist: " +
	                                      dest_name);
	        }
	        if (!parentdir.canWrite()) {
	          throw new FileCopyException("FileCopy: destination "
	                                      + "directory is unwriteable: " +
	                                      dest_name);
	        }
	      }

	      // If we've gotten this far, then everything is okay; we can
	      // copy the file.
	      source = new FileInputStream(source_file);
	      destination = new FileOutputStream(destination_file);
	      buffer = new byte[1024];
	      while (true) {
	        bytes_read = source.read(buffer);
	        if (bytes_read == -1) {
	          break;
	        }
	        destination.write(buffer, 0, bytes_read);
	      }
	    } //if end
	    // No matter what happens, always close any streams we've  opened.
	    finally {
	      if (source != null) {
	        try {
	          source.close();
	        }
	        catch (final IOException e) {
	          ;
	        }
	      }
	      if (destination != null) {
	        try {
	          destination.close();
	        }
	        catch (final IOException e) {
	          ;
	        }
	      }
//	      //��copy ,����
//	      if (!dest_file.getPath().equals(destination_file.getPath()) &&
//	          dest_file.exists()) {
//	        dest_file.delete();
//	      }
//	      destination_file.renameTo(dest_file);
	      return dest_name;
	    }
	  }

	  // File.getParent() can return null when the file is specified without
	  // a directory or is in the root directory.
	  // This method handles those cases.
	  private static File parent(File f) {
	    String dirname = f.getParent();
	    if (dirname == null) {
	      if (f.isAbsolute()) {
	        return new File(File.separator);
	      }
	      else {
	        return new File(System.getProperty("user.dir"));
	      }
	    }
	    return new File(dirname);
	  }
	}

	class FileCopyException
	    extends IOException {
	  public FileCopyException(String msg) {
	    super(msg);
	  }
	
}