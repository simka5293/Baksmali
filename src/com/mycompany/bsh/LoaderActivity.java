package com.mycompany.bsh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import com.mycompany.bsh.*;
import com.mycompany.utils.*;

public class LoaderActivity extends Activity
{
	Address a=new Address();
    public String s="";
	public String fn="";
	public String address="";
	public String path = "";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		address = a.getAddress();
		path = a.getTmp();

		if (address.length() == 0)
		{
			exit();
		}
		else if (!Commons.canAccess(this))
		{
			a.setAddress(path + "/out");
			new RW().write(a.getAddress(), "Подключение к интернету отсутствует!\nТысяча извинений!");
			exit();
		}
		else
		{
			s = getLocalFileName(address);
			downloadFile(address);
		}
	}

	private void downloadFile(String url)
	{
		final ProgressDialog progressDialog = new ProgressDialog(this);
		new AsyncTask<String, Integer, File>() {
			private Exception m_error = null;

			@Override
			protected void onPreExecute()
			{
				progressDialog.setMessage("Download\n" + s);
				progressDialog.setCancelable(false);
				progressDialog.setMax(100);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.show();
			}
			@Override
			protected File doInBackground(String... params)
			{
				URL url;
				HttpURLConnection urlConnection;
				InputStream inputStream;
				int totalSize;
				int downloadedSize;
				byte[] buffer;
				int bufferLength;
				File file=new File(path + "/" + getLocalFileName(params[0]));
				FileOutputStream fos = null;
				try
				{
					url = new URL(params[0]);
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoOutput(true);
					urlConnection.connect();
					file.createNewFile();
					fos = new FileOutputStream(file);
					inputStream = urlConnection.getInputStream();
					totalSize = urlConnection.getContentLength();
					downloadedSize = 0;
					buffer = new byte[1024];
					bufferLength = 0;
					// читаем со входа и пишем в выход, 
					// с каждой итерацией публикуем прогресс
					while ((bufferLength = inputStream.read(buffer)) > 0)
					{
						fos.write(buffer, 0, bufferLength);
						downloadedSize += bufferLength;
						publishProgress(downloadedSize, totalSize);
					}
					fos.close();
					inputStream.close();
					return file;
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
					m_error = e;
				}
				catch (IOException e)
				{
					e.printStackTrace();
					m_error = e;
				}
				return null;
			}
			// обновляем progressDialog
			protected void onProgressUpdate(Integer... values)
			{
				progressDialog.setProgress((int) ((values[0] / (float) values[1]) * 100));
			};

			@Override
			protected void onPostExecute(File file)
			{
				// отображаем сообщение, если возникла ошибка
				if (m_error != null)
				{
					m_error.printStackTrace();
					return;
				}
				// закрываем прогресс и удаляем временный файл
				progressDialog.hide();
				a.setAddress(a.getSrc() + "/1.bsh");
				unzip(address);
				exit();
			}
		}.execute(url);
    }
	public String getLocalFileName(String address)
	{
		int lastSlashIndex = address.lastIndexOf('/');
		if (lastSlashIndex >= 0 &&
			lastSlashIndex < address.length() - 1)
		{
			address = address.substring(lastSlashIndex + 1);
		}
		return address;
	}

	public void unzip(String address)
	{
		File f = new File(a.getTmp() + "/" + getLocalFileName(address));
		if (f.exists())
		{
			Unzip unzip=new Unzip();
			String[] u={f.toString(),a.getTmp()};
			unzip.main(u);
			f.delete();
		}
	}
	public String stripClassName(String className, int x)
	{
        return className.substring(0, className.length() - x);
    }
	public void exit()
	{
		Intent intent = new Intent();
		intent.setClass(LoaderActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}


