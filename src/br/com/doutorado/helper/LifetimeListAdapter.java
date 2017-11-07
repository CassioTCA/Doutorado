package br.com.doutorado.helper;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.doutorado.R;

public class LifetimeListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<LifetimeObject> lifetimeObjects;
	private LayoutInflater inflater;
	private LifetimeHelper lifetimeHelper;

	public LifetimeListAdapter(Context context, ArrayList<LifetimeObject> lifetimeObjects, LifetimeHelper lifetimeHelper) {
		this.context = context;
		this.lifetimeObjects = lifetimeObjects;
		this.lifetimeHelper = lifetimeHelper;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return lifetimeObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder{
		public TextView txtPotencia;
		public Button btnLifetime;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;

		// reuse views
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_lifetime_item, null);
			holder = new ViewHolder();

			holder.btnLifetime = (Button) rowView.findViewById(R.id.button1);
			holder.btnLifetime.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LinearLayout linearLayout = (LinearLayout)v.getParent();
		            TextView txtPotencia = (TextView)linearLayout.findViewById(R.id.textView1);
		            lifetimeHelper.adapterClick(((Button)v).getText().toString(), txtPotencia.getText().toString());
				}
			});

			holder.txtPotencia = (TextView) rowView.findViewById(R.id.textView1);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		LifetimeObject object = lifetimeObjects.get(position); 

		holder.txtPotencia.setText(object.getPotencia());
		
		
		holder.btnLifetime.setText(object.getValue());
		if(object.getValue().equals("x")) {
			holder.btnLifetime.setClickable(false);
		}

		return rowView;
	}
} 