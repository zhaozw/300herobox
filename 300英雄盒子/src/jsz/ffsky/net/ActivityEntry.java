package jsz.ffsky.net;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ActivityEntry extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listview = new ListView(this);
        listview.setAdapter(new MyListViewAdapter());
        listview.setOnItemClickListener(new OnItemClickListener() {  
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {  
            	onListItemClick(index);
            }  
        });  
        setContentView(listview);
    }
	
    void onListItemClick(int index) {
		Intent intent = null;
		intent = new Intent(ActivityEntry.this, acts[index].cls);
		this.startActivity(intent);
    }

    AcitivtyInfo[] acts = new AcitivtyInfo[]{
    		new AcitivtyInfo("Widget集成方式", SDK_WebApp.class),
    		new AcitivtyInfo("Webview集成方式", SDK_WebView.class)
    };
	class AcitivtyInfo{
		String title;
		Class<? extends android.app.Activity> cls;
		
		AcitivtyInfo(String title,Class cls){
			this.title = title;
 			this.cls = cls;
		}
	}
	
	class MyListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return acts.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return acts[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(parent.getContext());
			tv.setText(acts[position].title);
			convertView = tv;
			tv.setTextSize(35);
			//tv.setLayoutParams(new LayoutParams(-1, 20));
			return convertView;
		}
		
	}
}
