package com.mycompany.bsh; 
import java.io.File;
public class Delete
{
	public Delete()
	{}
	public void delete(String fn)
	{
		File f=new File(fn);
		if (f.isDirectory())
		{
			del(fn);
		}
		else
		{
			f.delete();
		}
	}
	public void del(String dir)
	{
		File f=new File(dir);
		File[] ff=f.listFiles();
		if (ff.length > 0)
		{
			for (File fn:ff)
			{
				if (fn.isDirectory())
				{
					del(fn.toString());
				}
				else
				{
					fn.delete();
				}
			}
		}
		f.delete();
	}
}

