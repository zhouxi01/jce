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
 * @类名: FileUtil
 * @作用: TODO
 * @日期: 2012-3-1 下午04:58:31
 */

public class FileUtil
{
	/**
	* 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
	* @param fileName    文件的名
	*/
	public static void readFileByBytes ( String fileName )
	{
		File file = new File ( fileName );
		InputStream in = null;
		byte[] str = null;
		try
		{
			System.out.println ( "以字节为单位读取文件内容，一次读一个字节：" );
// 一次读一个字节
			
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
			System.out.println ( "以字节为单位读取文件内容，一次读多个字节：" );
// 一次读多个字节
			byte[] tempbytes = new byte[100];
			int byteread = 0;
			in = new FileInputStream ( fileName );
			FileUtil.showAvailableBytes ( in );
// 读入多个字节到字节数组中，byteread为一次读入的字节数
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
	* 以字符为单位读取文件，常用于读文本，数字等类型的文件
	*
	* @param fileName
	*            文件名
	*/
	public static void readFileByChars ( String fileName )
	{
		File file = new File ( fileName );
		Reader reader = null;
		try
		{
			System.out.println ( "以字符为单位读取文件内容，一次读一个字节：" );
// 一次读一个字符
			reader = new InputStreamReader ( new FileInputStream ( file ) );
			int tempchar;
			while ( ( tempchar = reader.read() ) != -1 )
			{
// 对于windows下，rn这两个字符在一起时，表示一个换行。
// 但如果这两个字符分开显示时，会换两次行。
// 因此，屏蔽掉r，或者屏蔽n。否则，将会多出很多空行。
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
			System.out.println ( "以字符为单位读取文件内容，一次读多个字节：" );
// 一次读多个字符
			char[] tempchars = new char[30];
			int charread = 0;
			reader = new InputStreamReader ( new FileInputStream ( fileName ) );
// 读入多个字符到字符数组中，charread为一次读取字符数
			while ( ( charread = reader.read ( tempchars ) ) != -1 )
			{
// 同样屏蔽掉r不显示
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
	* 以行为单位读取文件，常用于读面向行的格式化文件
	*
	* @param fileName
	*            文件名
	*/
	public static String readFileByLines ( String fileName )
	{
		StringBuffer str = new StringBuffer();
		File file = new File ( fileName );
		BufferedReader reader = null;
		try
		{
			System.out.println ( "以行为单位读取文件内容，一次读一整行：" );
			reader = new BufferedReader ( new FileReader ( file ) );
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ( ( tempString = reader.readLine() ) != null )
			{
			// 显示行号
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
	* 随机读取文件内容
	*
	* @param fileName
	*            文件名
	*/
	public static void readFileByRandomAccess ( String fileName )
	{
		RandomAccessFile randomFile = null;
		try
		{
			System.out.println ( "随机读取一段文件内容：" );
// 打开一个随机访问文件流，按只读方式
			randomFile = new RandomAccessFile ( fileName, "r" );
// 文件长度，字节数
			long fileLength = randomFile.length();
// 读文件的起始位置
			int beginIndex = ( fileLength > 4 ) ? 4 : 0;
// 将读文件的开始位置移到beginIndex位置。
			randomFile.seek ( beginIndex );
			byte[] bytes = new byte[10];
			int byteread = 0;
// 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
// 将一次读取的字节数赋给byteread
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
	* 显示输入流中还剩的字节数
	*
	* @param in
	*/
	private static void showAvailableBytes ( InputStream in )
	{
		try
		{
			System.out.println ( "当前字节输入流中的字节数为:" + in.available() );
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
	* A方法追加文件：使用RandomAccessFile
	*
	* @param fileName
	*            文件名
	* @param content
	*            追加的内容
	*/
	public static void appendMethodA ( String fileName, String content )
	{
		try
		{
// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile ( fileName, "rw" );
// 文件长度，字节数
			long fileLength = randomFile.length();
// 将写文件指针移到文件尾。
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
	 * 重写文件内容
	 * @param fileName
	 * @param content
	 */
	public static void reWriteFile ( String fileName, String content )
	{
		try
		{
// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
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
	* B方法追加文件：使用FileWriter
	*
	* @param fileName
	* @param content
	*/
	public static void appendMethodB ( String fileName, String content )
	{
		try
		{
// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
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
// 按方法A追加文件
// AppendToFile.appendMethodA(fileName, content);
// AppendToFile.appendMethodA(fileName, "append end. n");
// 显示文件内容
// ReadFromFile.readFileByLines(fileName);
// 按方法B追加文件
// AppendToFile.appendMethodB(fileName, content);
// AppendToFile.appendMethodB(fileName, "append end. n");
// 显示文件内容
// ReadFromFile.readFileByLines(fileName);
	}
	
	
	  /**
	   *  文件复制函数，参数是两个全路径文件名，如果发生错误，将抛出FileCopyException异常
	   * @param source_name String
	   * @param dest_name String
	   * @param type int
	   * 目标文件如果存在 参数1： 覆盖 ，2：生成一个新的文件， 3：不重新生成
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
	          //目标文件 参数1 覆盖 2生成一个新的文件 3不重新生成
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
//	      //先copy ,后换名
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