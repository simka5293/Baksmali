package com.mycompany.bsh;
import java.io.*;

public class FileSearch
{
	public FileSearch()
	{}
	String s="";
	String path="";
	String endsWith="";

	public String[] main(String[] args)
	{
		this.path = args[0];
		this.endsWith = args[1];
		dir(path);
		return s.split("\n");
	}

	public void dir(String dir)
	{
		File[] files=new File(dir).listFiles();
		for (File f:files)
		{
			if (f.isFile())
			{
				if (f.toString().endsWith(endsWith))
				{
					this.s += dir + "/" + f.getName() + "\n";
				}
			}
			else
			{
				dir(dir + "/" + f.getName());
			}
		}
	}
}

