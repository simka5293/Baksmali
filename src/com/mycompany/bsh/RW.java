package com.mycompany.bsh;
import java.io.*;

public class RW
{
	InputStream input;
	PrintStream output;
	PrintStream error;

	public String read(String fn)
	{
		StringBuffer sb=new StringBuffer();
		try
		{
			InputStream is=new FileInputStream(new File(fn));
			Reader r=new BufferedReader(new InputStreamReader(is));
			char[] buffer=new char[1024 * 10];
			int i=0;
			while ((i = r.read(buffer)) > 0)
			{
				sb.append(buffer, 0, i);
			}
			is.close();
			r.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
	public void write(String fn, String s)
	{
		try
		{
			PrintStream ps=new PrintStream(new FileOutputStream(new File(fn)));
			ps.write(s.getBytes());
			ps.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public void setOut(String in, String out, String err)
	{
		try
		{
			input = new FileInputStream(new File(in));
			System.setIn(input);
			output = new PrintStream(new FileOutputStream(new File(out)));
			System.setOut(output);
			error = new PrintStream(new FileOutputStream(new File(err)));
			System.setErr(error);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

	}

	public void close()
	{
		try
		{
			input.close();
			output.close();
			error.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
