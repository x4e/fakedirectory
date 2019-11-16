package cookiedragon.fakedirectory;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * @author cookiedragon234 16/Nov/2019
 */
public class FakeDirectory
{
	private final File inputFile;
	
	public FakeDirectory(String inputFile)
	{
		this(new File(inputFile));
	}
	
	public FakeDirectory(File inputFile)
	{
		this.inputFile = inputFile;
		
		if(!inputFile.exists() || inputFile.isDirectory())
		{
			System.err.println("File '" + inputFile.getAbsolutePath() + "' couldn't be found!");
		}
		
		Instant start = Instant.now();
		
		JarFile inputJar;
		JarOutputStream jarOutputStream;
		try
		{
			inputJar = new JarFile(inputFile);
		}
		catch(Exception e)
		{
			throw new RuntimeException("Error while reading JarFile", e);
		}
		
		try
		{
			jarOutputStream = new JarOutputStream(
				new FileOutputStream(
					new File(
						inputFile.getAbsolutePath().replace(".jar", "-obf.jar").replace(".zip", "-obf.zip")
					)
				)
			);
		}
		catch(Exception e)
		{
			throw new RuntimeException("Error while opening outputs stream", e);
		}
		
		Enumeration<JarEntry> entries = inputJar.entries();
		
		while(entries.hasMoreElements())
		{
			JarEntry inEntry = entries.nextElement();
			String name = inEntry.getName();
			
			try
			{
				if(name.endsWith(".class"))
					name += "/";
				
				JarEntry outEntry = new JarEntry(name);
				jarOutputStream.putNextEntry(outEntry);
				jarOutputStream.write(getBytesFromInputStream(inputJar.getInputStream(inEntry)));
				jarOutputStream.closeEntry();
			}
			catch(Exception e)
			{
				new RuntimeException("Error while writing entry '" + name + "'", e).printStackTrace();
			}
		}
		
		try
		{
			inputJar.close();
		}
		catch(Exception e)
		{
			throw new RuntimeException("Error while closing input", e);
		}
		
		try
		{
			jarOutputStream.close();
		}
		catch(Exception e)
		{
			throw new RuntimeException("Error while saving output", e);
		}
		
		System.out.println("Finished in " + Duration.between(start, Instant.now()).toMillis() + "ms");
	}
	
	private static byte[] getBytesFromInputStream(InputStream is) throws IOException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[0xFFFF];
		for (int len = is.read(buffer); len != -1; len = is.read(buffer))
		{
			os.write(buffer, 0, len);
		}
		return os.toByteArray();
	}
}
