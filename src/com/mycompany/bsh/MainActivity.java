package com.mycompany.bsh;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import bsh.*;
import java.io.*;
import java.util.*;
import com.clarionmedia.dalvikbaksmali.impl.*;


public class MainActivity extends Activity
{
	EditText ed;
	Address a=new Address();
	boolean flag=true;
	RW rw=new RW();
	String path="/mnt/sdcard/Baksmali";
	String fn=a.getAddress();
	public String text="",reset="";
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setTitle("Редактор");
		setContentView(R.layout.main);
		ed = new EditText(this);
		ed = (EditText)findViewById(R.id.ed);
		if (fn.length() == 0)
		{
			fn = path + "/1.bsh";
		}
		File f=new File(fn);
		if (!f.exists())
		{
			create(f.getParent());
			rw.write(fn, "");
		}
		ed.setBackgroundColor(Color.rgb(255,255,255));
		ed.setTextColor(Color.rgb(0,0,0));
		if(fn.endsWith(".dex")||fn.endsWith(".apk")){
			dex();
		}else{
		ed.setText(rw.read(fn));
		}
    }
	public boolean getFlag()
	{
		flag = !flag;
		return flag;
	}
	public void download()
	{
		Address a=new Address();
		String address = "http://www.java2s.com/Code/JarDownload/java-rt/java-rt-jar-stubs-1.5.0.jar.zip";
		a.setSrc(path);
		a.setTmp(path);
		a.setAddress(address);
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, LoaderActivity.class);
		startActivity(intent);
		finish();
	}
	public void dex(){
		rw.setOut(fn, path + "/out", path + "/err");
		try{
			BaksmaliImpl bsm=new BaksmaliImpl();
			File f=new File(fn);
			String name=f.getName();
			String outdir=f.getParent()+"/"+name.substring(0,name.indexOf("."));
			new File(outdir).mkdirs();
			bsm.decompile(fn,outdir);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			rw.close();
			ed.setText(rw.read(path+"err"));
			if(ed.getText().length()==0){
				ed.setText(rw.read(path+"/out"));
			}
		}
	}
	public void execute()
	{
		Intent intent = new Intent();
		intent.setClass(MainActivity.this,MainActivity.class);
		startActivity(intent);
		finish();
	}
	public void run()
	{
		long x=getTime();
		if(fn.endsWith(".dex")){
			flag=false;
			dex();
		}
		try
		{
			if (flag == true)
			{
				setTitle("Результат");
				text = ed.getText().toString();
				reset = text;
				rw.write(fn, text);
				rw.setOut(fn, path + "/out", path + "/err");
				if(fn.endsWith(".bsh")){
				Interpreter i=new Interpreter();
				i.eval(text);
				}
				rw.close();
				String s = rw.read(path + "/err");
				if (s.length() == 0)
				{
					s = rw.read(path + "/out");
				}
				ed.setText(s);
				flag = false;
			}
			else
			{
				reset();
				flag = true;
			}
			long y=getTime();
			Toast("Выполнено за " + (y - x) + " мс!");
		}
		catch (Exception e)
		{
			ed.setText(e.toString());
		}
	}

	public void clear()
	{
		ed.setText("");
	}
	public void Toast(String s)
	{
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
	}
	public long getTime()
	{
		Date d=new Date();
		long x=d.getTime();
		return x;
	}
	public void create(String s)
	{
		String[] r=s.split("/");
		s = "";
		for (String x:r)
		{
			s += x;
			File f=new File(s);
			if (!f.exists())
			{
				f.mkdir();
				Toast("Папка " + s + " создана!");
			}
			s += "/";
		}
	}
	public void reset()
	{
		if(fn.endsWith(".dex")){
			flag=false;
			dex();
			return;
		}
		setTitle("Редактор");
		ed.setText(rw.read(fn));
		flag = true;
	}
	public void open()
	{
		Address a=new Address();
		a.setSrc(path);
		a.setTmp(path);
		a.setAddress(fn);
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, FileChooser.class);
		startActivity(intent);
		finish();
	}
	public void onBackPressed()
	{
		reset();
	}
	public void exit()
	{
		finish();
		System.exit(0);
	}
    public boolean onCreateOptionsMenu(Menu menu)
	{
        getMenuInflater().inflate(R.layout.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
        switch (item.getItemId())
		{
			case R.id.action_open:
                open();
                return true;
			case R.id.action_run:
                run();
                return true;
			case R.id.action_exit:
                exit();
                return true;
            default:
                return true;
        }
    }
}
