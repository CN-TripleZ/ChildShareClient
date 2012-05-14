package com.qinzi.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qinzi.constants.Url;
import com.qinzi.pojo.ItemInfo;
import com.qinzi.utils.AsyncImageLoader;
import com.qinzi.utils.AsyncImageLoader.ImageCallback;

public class ListActivity extends Activity {

	private List<ItemInfo> share_items;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		//TODO: 用户登录后使用登录后的账户 
		String url = Url.DOMAIN_URL + "child_share/ImageServlet?cmd=queryjson&userId=qinzi";
		HttpPost post = new HttpPost(url);
		post.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		HttpResponse response = null;
		try {
			response = new DefaultHttpClient().execute(post);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (null != response && 200 == response.getStatusLine().getStatusCode()){
			try {
				InputStream is = response.getEntity().getContent();
				Reader reader = new BufferedReader(new InputStreamReader(is), 4000);
				StringBuilder buffer = new StringBuilder((int) response.getEntity().getContentLength());
				try {
					char[] tmp = new char[1024];
					int l;
					while ((l = reader.read(tmp)) != -1) {
						buffer.append(tmp, 0, l);
					}
				} finally {
					reader.close();
				}
				String string = buffer.toString();
				JSONObject data = new JSONObject(string);
				if ("0".equals(data.getString("ret"))) {
					if (share_items == null) {
						share_items = new ArrayList<ItemInfo>();
					}
					JSONArray items = (JSONArray) data.get("data");
					for (int i = 0; i < items.length(); i++) {
						JSONObject o = items.getJSONObject(i);
						String image = o.getString("path");
						String text = o.getString("description");
						String time = o.getString("upload_time");
						ItemInfo item = new ItemInfo();
						item.setImage(image);
						item.setText(text);
						item.setTime(time);
						share_items.add(item);
					}
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if (share_items != null) {
				ItemAdapter itemAdapter = new ItemAdapter();
				ListView itemsView = (ListView)findViewById(R.id.Msglist);
				itemsView.setAdapter(itemAdapter);
			}
		}
	}

	public class ItemAdapter extends BaseAdapter {
		private AsyncImageLoader asyncImageLoader;

		public int getCount() {
			return share_items.size();
		}

		public Object getItem(int position) {
			return share_items.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (asyncImageLoader == null) {
				asyncImageLoader = new AsyncImageLoader();
			}
			if (position % 2 == 0) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(
						R.layout.item_img_left, null);
			} else {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(
						R.layout.item_img_right, null);
			}
			
			ItemHolder itemHolder = new ItemHolder();
			if (position % 2 == 0) {
				itemHolder.image = (ImageView) convertView.findViewById(R.id.item_image);
				itemHolder.time = (TextView) convertView.findViewById(R.id.item_time);
				itemHolder.text = (TextView) convertView.findViewById(R.id.item_text);
			} else {
				itemHolder.image = (ImageView) convertView.findViewById(R.id.item_image_right);
				itemHolder.time = (TextView) convertView.findViewById(R.id.item_time_right);
				itemHolder.text = (TextView) convertView.findViewById(R.id.item_text_right);
			}
			
			ItemInfo item = share_items.get(position);
			
			if (item != null) {
				convertView.setTag(item.getId());
				itemHolder.time.setText(item.getTime());
				itemHolder.text.setText(item.getText());
			}
			
			Drawable cachedImage = asyncImageLoader.loadDrawable(item.getImage(), itemHolder.image, new ImageCallback(){
                public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
                    imageView.setImageDrawable(imageDrawable);
                }
            });
			
			if (cachedImage == null) {
				itemHolder.image.setImageResource(R.drawable.child_icon);
			} else {
				itemHolder.image.setImageDrawable(cachedImage);
			}
			
			return convertView;
		}

	}

	public class ItemHolder {
		public ImageView image;
		public TextView time;
		public TextView text;
	}

}