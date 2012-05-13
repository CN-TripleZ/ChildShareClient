package com.qinzi.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhotoUploadActivity extends Activity {

	private EditText description;
	private Button uploadButton;
	private String imagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_upload);

		Intent intent = super.getIntent();
		Bundle extras = intent.getExtras();
		imagePath = extras.getString("imagePath");

		description = (EditText) super.findViewById(R.id.description);
		uploadButton = (Button) super.findViewById(R.id.uploadButton);

		uploadButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				uploadFile1();
			}
		});
	}

	private void uploadFile() {

		String uploadUrl = "http://192.168.1.35:8080/child_share/ImageServlet?cmd=add&userId=jeffreyzhang";
		
		String userId = "jeffreyzhang";
		String desc = description.getText().toString();
		try {
			if (desc != null && desc != "") {
				uploadUrl += "&description="+ URLEncoder.encode(description.getText().toString(), "UTF-8");
			}
			if (userId != null && userId != "") {
				uploadUrl += "&userId="+ URLEncoder.encode(userId, "UTF-8");
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(httpURLConnection
					.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);

			dos
					.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
							+ imagePath
									.substring(imagePath.lastIndexOf("/") + 1)
							+ "\"" + end);
			dos.writeBytes(end);

			FileInputStream fis = new FileInputStream(imagePath);
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);

			}
			fis.close();

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();

			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			dos.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
			setTitle(e.getMessage());
		}

	}

	private void uploadFile1() {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost("http://192.168.1.35:8080/child_share/ImageServlet?cmd=add");

			FileBody bin = new FileBody(new File(imagePath));

			MultipartEntity reqEntity = new MultipartEntity();

			reqEntity.addPart("file", bin);
			reqEntity.addPart("description", new StringBody(URLEncoder.encode(description.getText().toString(), "UTF-8")));
			reqEntity.addPart("userId", new StringBody(URLEncoder.encode("jeffreyzhang", "UTF-8")));

			httppost.setEntity(reqEntity);

			System.out
					.println("executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			if (resEntity != null) {
				System.out.println("Response content length: "
						+ resEntity.getContentLength());
			}

		} catch (Exception e) {
			System.out.println("上传失败....");
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
			}
		}
	}
}
