package com.mycompany.bsh;
import java.io.*; 
import java.util.*; 
import java.util.zip.*; 
public class Unzip 
{ 
	public static void main(String args[]) 
	{ 
		String szZipFilePath=args[0]; 
		String szExtractPath=args[1]; 
		int i;
		ZipFile zf; 
		Vector zipEntries = new Vector(); 
		try 
		{ 
			zf = new ZipFile(szZipFilePath); 
			Enumeration en = zf.entries(); 
			while (en.hasMoreElements()) 
			{ 
				zipEntries.addElement( 
					(ZipEntry)en.nextElement()); 
			} 
			for (i = 0; i < zipEntries.size(); i++) 
			{ 
				ZipEntry ze = 
					(ZipEntry)zipEntries.elementAt(i); 
				extractFromZip(szZipFilePath, 
							   szExtractPath, 
							   ze.getName(), zf, ze); 
			} 
			zf.close();
			System.out.println("Done!");
		} 
		catch (Exception e) 
		{
			System.out.println(e.toString());
		} 
	} 
// ============================================ 
// extractFromZip 
// ============================================ 
	static void extractFromZip( 
		String szZipFilePath, String szExtractPath, 
		String szName, 
		ZipFile zf, ZipEntry ze) 
	{ 
		if (ze.isDirectory()) 
			return; 
		String szDstName = slash2sep(szName); 
		String szEntryDir; 
		if (szDstName.lastIndexOf(File.separator) != -1) 
		{ 
			szEntryDir = 
				szDstName.substring( 
				0, 
				szDstName.lastIndexOf(File.separator)); 
		} 
		else 
			szEntryDir = ""; 
		System.out.print(szDstName); 
		long nSize = ze.getSize(); 
		long nCompressedSize = 
			ze.getCompressedSize(); 
		System.out.println(" " + nSize + " (" + 
						   nCompressedSize + ")"); 
		try 
		{ 
			File newDir = new File(szExtractPath + 
								   File.separator + szEntryDir); 
			newDir.mkdirs(); 
			FileOutputStream fos = 
				new FileOutputStream(szExtractPath + 
									 File.separator + szDstName); 
			InputStream is = zf.getInputStream(ze); 
			byte[] buf = new byte[1024]; 
			int nLength; 
			while (true) 
			{ 
				try 
				{ 
					nLength = is.read(buf); 
				} 
				catch (EOFException ex) 
				{ 
					break; 
				} 
				if (nLength < 0) 
					break; 
				fos.write(buf, 0, nLength); 
			} 
			is.close(); 
			fos.close(); 
		} 
		catch (Exception e) 
		{ 
			System.out.println(e.toString());
		} 
	} 
// ============================================ 
// slash2sep 
// ============================================ 
	static String slash2sep(String src) 
	{ 
		int i; 
		char[] chDst = new char[src.length()]; 
		String dst; 
		for (i = 0; i < src.length(); i++) 
		{ 
			if (src.charAt(i) == '/') 
				chDst[i] = File.separatorChar; 
			else 
				chDst[i] = src.charAt(i); 
		} 
		dst = new String(chDst); 
		return dst; 
	}
} 

