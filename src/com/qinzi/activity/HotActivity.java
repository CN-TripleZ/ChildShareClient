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
import android.widget.GridView;
import android.widget.ImageView;

import com.qinzi.constants.Url;
import com.qinzi.pojo.ItemInfo;
import com.qinzi.utils.AsyncImageLoader;
import com.qinzi.utils.AsyncImageLoader.ImageCallback;

public class HotActivity extends Activity {
	
	private List<ItemInfo> share_items;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot);
        
        String url = Url.DOMAIN_URL + "child_share/ImageServlet?cmd=hotjson&userId=qinzi";
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
						ItemInfo item = new ItemInfo();
						item.setImage(image);
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
				GridView itemsView = (GridView)findViewById(R.id.hot_images);
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
			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.hot_item, null);

			ItemHolder itemHolder = new ItemHolder();
			itemHolder.image = (ImageView) convertView.findViewById(R.id.hot_item_image);
			
			ItemInfo item = share_items.get(position);
			
			if (item != null) {
				convertView.setTag(item.getId());
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
	}
}