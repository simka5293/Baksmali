package com.mycompany.bsh; 
import dalvik.system.DexClassLoader;
import java.io.File;

public class Loader extends DexClassLoader
{
	public Loader(String dexpath)
	{
		super(dexpath, new File(dexpath).getParent(), dexpath, new MyClassLoader());
	}
	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException
	{
		return super.loadClass(className);
	}
}
