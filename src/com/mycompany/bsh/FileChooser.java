package com.mycompany.bsh;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;

public class FileChooser extends ListActivity
{
	File root;
	String path,subpath,fn,def;
	ListView lv;
	EditText et;
	int icon = 0;
	Address a=new Address();
	RW rw=new RW();
	boolean flag;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		icon = R.drawable.ic_launcher;
		lv = this.getListView();
		def = a.getTmp() + "/default";
		root = new File(a.getSrc());
		File f=new File(a.getAddress());
		if (f.isDirectory())
		{
			path = f.toString();
		}
		else
		{
			path = f.getParent();
		}
		if (path.length() < root.toString().length())
		{
			path = root.toString();
		}
		setListView(new File(path));
	}
	public void Toast(String s)
	{
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}
	private void setListView(File file)
	{
		if (file.isDirectory())
		{
			File[] files = file.listFiles();
			List<File> list = new ArrayList<File>();
			if (files != null)
				for (File f : files) 
					list.add(f);

			lv.setAdapter(new FileListAdapter(file, list));
			lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view,
												   int position, long id)
					{
						File f = (File) lv.getItemAtPosition(position);
						delete(f.toString());
						return true;
					}

				});
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id)
					{
						File f = (File) lv.getItemAtPosition(position);
						if (f.isDirectory())
						{
							path = f.toString();
							setListView(f);
						}
						else
						{
							a.setAddress(f.getAbsolutePath());
							rw.write(def, a.getAddress());
							Intent intent = new Intent();
							intent.setClass(FileChooser.this, MainActivity.class);
							subpath = f.toString().substring(f.toString().indexOf("/src/") + 5);
						 	Toast(subpath);
							startActivity(intent);
							finish();
						}
					}
				});
		}
	}

	private class FileListAdapter extends BaseAdapter
	{

		File path;
		List<File> files;

		FileListAdapter(File path, List<File> files)
		{
			this.path = path;
			this.files = files;
			List<File> removed = new ArrayList<File>();
			for (File f : files)
			{
				if (f.isDirectory() && !f.getName().startsWith("."))
					continue;
				if (f.isFile() && !f.getName().startsWith("."))
					continue;
				removed.add(f);
			}
			files.removeAll(removed);
			removed.clear();
			Collections.sort(files, new Comparator<File>() {

					@Override
					public int compare(File f0, File f1)
					{
						return f0.getName().compareTo(f1.getName());
					}
				});
			Collections.sort(files, new Comparator<File>() {

					@Override
					public int compare(File f0, File f1)
					{
						if (f0.isDirectory() && f1.isFile())
						{
							return -1;
						}
						if (f1.isDirectory() && f0.isFile())
						{
							return 1;
						}
						else
						{
							return 0;
						}

					}
				});

			if (!path.toString().equals(root.toString()))
				files.add(0, path.getParentFile());

		}

		@Override
		public int getCount()
		{
			return files.size();
		}

		@Override
		public Object getItem(int position)
		{
			return files.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent)
		{
			View v = LayoutInflater.from(FileChooser.this)
				.inflate(R.layout.item, null);
			File f = files.get(position);
			String name = (f.toString().equals(path.getParent())) ?"..": f.getName();
			((TextView) v.findViewById(R.id.item_text)).setText(name);
			if (f.isFile())
			{
				((ImageView) v.findViewById(R.id.item_icon))
					.setImageResource(icon);
			}
			return v;
		}
	}

	public void onBackPressed()
	{
		if (!path.toString().equals(root.toString()))
		{
			path = new File(path).getParent();
			a.setAddress(path);
			setListView(new File(path));
		}
	}
	public void reset()
	{
		Intent intent = new Intent();
		intent.setClass(FileChooser.this, FileChooser.class);
		startActivity(intent);
		finish();
	}
	public void back()
	{
		Intent intent = new Intent();
		intent.setClass(FileChooser.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	public void download()
	{
		rw.write(def, a.getSrc() + "/1.bsh");
		a.setAddress("http://kontrol484.esy.es/files/help_bsh.zip");
		Intent intent = new Intent();
		intent.setClass(FileChooser.this, LoaderActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void help(){
		Intent intent = new Intent();
		intent.setClass(FileChooser.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	public boolean delete()
	{
		File f=new File(fn);
		if (f.isDirectory())
		{
			new Delete().delete(fn);
			Toast("Папка " + f.getName() + " удалена!");
		}
		else
		{
			new Delete().delete(fn);
			Toast("Файл " + f.getName() + " удален!");
		}
		return true;
	}
	public boolean delete(final String fn)
	{
		this.fn = fn;
		flag = false;
		final Dialog dl=new Dialog(this);
		dl.setContentView(R.layout.delete);
		dl.setTitle("Удалить " + new File(fn).getName() + " ?");
		Button yesBtn=(Button)dl.findViewById(R.id.yes);
		Button noBtn=(Button)dl.findViewById(R.id.no);
		yesBtn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v)
				{
					a.setAddress(new File(fn).getParent());
					if (new File(fn).isDirectory())
					{
						new Delete().delete(fn);
						Toast("Папка " + new File(fn).getName() + " удалена!");
						flag = true;
					}
					else
					{
						new Delete().delete(fn);
						Toast("Файл " + new File(fn).getName() + " удален!");
						flag = true;
					}
					dl.dismiss();
					reset();
				}
			});

		noBtn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v)
				{
					flag = false;
					dl.dismiss();
					reset();
				}
			});
		dl.show();
		return flag;
	}
	public void newfile()
	{
		final Dialog dl=new Dialog(this);
		dl.setContentView(R.layout.newfile);
		dl.setTitle("Введите имя:");
		et = (EditText)dl.findViewById(R.id.et);
		Button yesBtn=(Button)dl.findViewById(R.id.file);
		Button noBtn=(Button)dl.findViewById(R.id.folder);
		yesBtn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v)
				{
					if (et.getText().length() > 0)
					{
						File f=new File(path + "/" + et.getText().toString());
						a.setAddress(f.toString());
						//a.setFlag(true);
						write(f.toString());
					}
					dl.dismiss();
					reset();
				}
			});

		noBtn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v)
				{
					if (et.getText().length() > 0)
					{
						File f=new File(path + "/" + et.getText().toString());
						f.mkdirs();
						path = f.getParent();
						a.setAddress(f.toString());
					}
					dl.dismiss();
					reset();
				}
			});
		dl.show();
	}

	public void write(String fn)
	{
		File f=new File(fn);
		if (!f.exists())
		{
			String txt=newfile(f.toString());
			rw.write(f.toString(), txt);
		}
		rw.write(def, f.toString());
	}
	public String newfile(String fn)
	{

		String pack="";
		/*
		 File f=new File(fn);
		 if (fn.endsWith(".java"))
		 {
		 String n= stripClassName(f.getName(), 5);
		 String	txt =
		 "public class " + n + "{\n" +
		 "public " + n + "(){\n" +
		 "main();\n" +
		 "}\n" +
		 "public void main(){\n" +
		 "System.out.println(\"" + n + "\");\n" +
		 "}\n" +
		 "}\n";
		 if (!f.getParent().endsWith("/src"))
		 {
		 String p="";
		 while (!f.getParent().endsWith("/src"))
		 {
		 p = f.getParent().substring(f.getParent().lastIndexOf("/") + 1) + "." + p;
		 f = new File(f.getParent());
		 }
		 p = p.substring(0, p.length() - 1);
		 pack = "package " + p + ";\n";
		 }
		 pack = pack + txt;
		 }
		 */

		return pack;
	}
	private String stripClassName(String className, int x)
	{
        return className.substring(0, className.length() - x);
    }

	private static final int NEW_MENU_ID = Menu.FIRST;
	private static final int BACK_MENU_ID = Menu.FIRST + 1;
    private static final int HELP_MENU_ID = Menu.FIRST + 2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
        super.onCreateOptionsMenu(menu);
		menu.add(0, NEW_MENU_ID, 0, "Создать").setShortcut('0', 'c');
		menu.add(0, BACK_MENU_ID, 0, "Вернуться").setShortcut('0', 'r');
        menu.add(0, HELP_MENU_ID, 0, "Помощь").setShortcut('0', 'h');
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
	{
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{

        switch (item.getItemId())
		{
			case NEW_MENU_ID:
				newfile();
				return true;
			case BACK_MENU_ID:
				back();
				return true;
			case HELP_MENU_ID:
				help();
				return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
