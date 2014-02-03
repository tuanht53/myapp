package com.example.myapp.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;


public class NetHelper {
	
	private static NetHelper mInstance;
	private Context context;
	final String LOG_TAG = "NetHelperLog";
	final int googleru = 1249742686;
	final byte INTERNET_STATUS = 0;
	
	// Singleton
	public static NetHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new NetHelper(context);
		}
		return mInstance;
	}
	
	// Приватный конструктор
	private NetHelper(Context context) {
		
		this.context = context;
		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
		
	}
	
	// Проверяем сеть
	public boolean checkInternetConnection(){
		
		ConnectivityManager connMgr = (ConnectivityManager) 
		        context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] ni = connMgr.getAllNetworkInfo();
		for (int i = 0; i < ni.length; i++){
			Log.d("ConnectivityManager.TYPE =", String.valueOf(ni[i].getType()));
			if (ni[i].getType() == ConnectivityManager.TYPE_WIFI){
				if(connMgr.requestRouteToHost(ConnectivityManager.TYPE_WIFI, googleru))
					return true;
			}
		}
		
		for (int i = 0; i < ni.length; i++){
			Log.d("ConnectivityManager.TYPE =", String.valueOf(ni[i].getType()));
			if(ni[i].getType() == ConnectivityManager.TYPE_MOBILE){
				if(connMgr.requestRouteToHost(ConnectivityManager.TYPE_MOBILE, googleru))
					return true;
			}
		}
		   
		return false;
		
		/*NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    if (networkInfo != null && networkInfo.isConnected()) 
		       return true;
		    else 
		       return false;*/
		    
	}
	
	// Вызов asyncGet
	public String asyncGet(String uri){
		
		String g = null;
		
		try {
			g = new asyncGet().execute(uri).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g;
	}
	
	// Вызов asyncPost
	public String asyncPost(String uri, String data){
		
		String[] s = {uri, data};
		String g = null;

		try {
			g = new asyncPost().execute(s).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g;
	}
	
	// Логика GET - запроса
	public String executeHttpGet(String uri){
		
		HttpURLConnection conn = null;
		InputStream is = null;
		URL url = null;
		String s = null;
		
		try {

			url = new URL(uri);

			conn = (HttpURLConnection) url.openConnection();
			
			conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
			
			Log.d(LOG_TAG, "The GET response is: " + conn.getResponseCode());
			
			is = conn.getInputStream();
			s = readStream(is);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
	            try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } 
			conn.disconnect();
		}
			
		return s;
		
	}
	
	// Логика POST - запроса
	public String executeHttpPost(String uri, String data) {

		HttpURLConnection conn = null;
		URL url = null;
		String s = null;

		try {

			url = new URL(uri);

			conn = (HttpURLConnection) url.openConnection();
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            
            DisplayResponseHeaders(conn);
			s = readStream(conn.getInputStream());
            Log.d("body", s);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			s = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}

		return s;

	}
	
	//  POST GCM
	public String executeHttpPostGCM(String data) {

		HttpsURLConnection conn = null;
		URL url = null;
		String s = null;

		try {

			url = new URL("https://android.googleapis.com/gcm/send");

			conn = (HttpsURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=AIzaSyApM9pt88hoJpEb827AP9JTtZCqqBI0Fhk");

			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(data);
			wr.flush();

			DisplayResponseHeadersGCM(conn);
			s = readStream(conn.getInputStream());
			Log.d("GCMbody", s);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}

		return s;

	}
	
	// Вывод логов
	private void DisplayResponseHeaders(HttpURLConnection conn) {

		Map<String, List<String>> headerFields = conn.getHeaderFields();
		Set<String> headerFieldsSet = headerFields.keySet();
		Iterator<String> hearerFieldsIter = headerFieldsSet.iterator();

		while (hearerFieldsIter.hasNext()) {

			String headerFieldKey = hearerFieldsIter.next();
			List<String> headerFieldValue = headerFields.get(headerFieldKey);

			StringBuilder sb = new StringBuilder();
			for (String value : headerFieldValue) {
				sb.append(value);
				sb.append("");
			}

			Log.d(headerFieldKey, sb.toString());

		}

	}
	
	// Вывод логов
	private void DisplayResponseHeadersGCM( HttpsURLConnection conn) {

		Map<String, List<String>> headerFields = conn.getHeaderFields();
		Set<String> headerFieldsSet = headerFields.keySet();
		Iterator<String> hearerFieldsIter = headerFieldsSet.iterator();

		while (hearerFieldsIter.hasNext()) {

			String headerFieldKey = hearerFieldsIter.next();
			List<String> headerFieldValue = headerFields.get(headerFieldKey);

			StringBuilder sb = new StringBuilder();
			for (String value : headerFieldValue) {
				sb.append(value);
				sb.append("");
			}

			Log.d(headerFieldKey, sb.toString());

		}

	}
	
	// Получаем String из InputStream
	private String readStream(InputStream in) {
		BufferedReader reader = null;

		StringBuffer sb = new StringBuffer("");
		String line = "";
		String NL = System.getProperty("line.separator");
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			while ((line = reader.readLine()) != null) {
				sb.append(line + NL);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		String result = sb.toString();
		
		return result;
		
	}
	
	// Background отрабатывает вызывает логику executeHttpGet()
	private class asyncGet extends AsyncTask<String, Void, String> {

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(String... params) {
			
			return executeHttpGet(params[0]);
			
		}
	}
	
	// Background отрабатывает вызывает логику executeHttpPost()
	private class asyncPost extends AsyncTask<String, Void, String> {

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(String... params) {
			return executeHttpPost(params[0], params[1]);
		}
	}
	
	// Сохраняем по ссылке файл
	public void saveFile(String uri, String filename) throws Exception {

		if (!(new File(filename).exists())) {

			HttpURLConnection conn = null;
			try {
				Log.d("ImageLoader", "tryFromWeb");
				URI uri1 = new URI(uri);
				URL imageUrl = new URL(uri1.toASCIIString());
				conn = (HttpURLConnection) imageUrl
						.openConnection();
				conn.setRequestMethod("GET");
				//conn.setDoOutput(true);
	
				conn.connect();
				InputStream inputStream = conn.getInputStream();
				
				byte[] buffer = new byte[1024];
				int bufferLength = 0;
				
				FileOutputStream fos = context.openFileOutput(filename,
						Context.MODE_WORLD_READABLE);
	
				// читаем со входа и пишем в выход
				while ((bufferLength = inputStream.read(buffer)) > 0) {
					fos.write(buffer, 0, bufferLength);
	
				}
				fos.flush();
				fos.close();
				inputStream.close();
	
			} catch (Exception ex) {
				ex.printStackTrace();
			}finally {
				conn.disconnect();
			}
		}

	}

}
