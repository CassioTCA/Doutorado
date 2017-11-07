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

public class EffListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<EffObject> effObjects;
	private LayoutInflater inflater;
	private EffHelper effHelper;

	public EffListAdapter(Context context, ArrayList<EffObject> effObjects, EffHelper effHelper) {
		this.context = context;
		this.effObjects = effObjects;
		this.effHelper = effHelper;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return effObjects.size();
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
		public Button btnPol2;
		public Button btnPol4;
		public Button btnPol6;
		public Button btnPol8;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;

		// reuse views
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_eff_item, null);
			holder = new ViewHolder();

			holder.btnPol2 = (Button) rowView.findViewById(R.id.button1);
			holder.btnPol2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LinearLayout linearLayout = (LinearLayout)v.getParent();
		            TextView txtPotencia = (TextView)linearLayout.findViewById(R.id.textView1);
		            effHelper.adapterClick(((Button)v).getText().toString(), txtPotencia.getText().toString());
				}
			});
			holder.btnPol4 = (Button) rowView.findViewById(R.id.button2);
			holder.btnPol4.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LinearLayout linearLayout = (LinearLayout)v.getParent();
		            TextView txtPotencia = (TextView)linearLayout.findViewById(R.id.textView1);
		            effHelper.adapterClick(((Button)v).getText().toString(), txtPotencia.getText().toString());
				}
			});

			holder.btnPol6 = (Button) rowView.findViewById(R.id.button3);
			holder.btnPol6.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LinearLayout linearLayout = (LinearLayout)v.getParent();
		            TextView txtPotencia = (TextView)linearLayout.findViewById(R.id.textView1);
		            effHelper.adapterClick(((Button)v).getText().toString(), txtPotencia.getText().toString());
				}
			});
			
			holder.btnPol8 = (Button) rowView.findViewById(R.id.button4);
			holder.btnPol8.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LinearLayout linearLayout = (LinearLayout)v.getParent();
		            TextView txtPotencia = (TextView)linearLayout.findViewById(R.id.textView1);
		            effHelper.adapterClick(((Button)v).getText().toString(), txtPotencia.getText().toString());
				}
			});

			holder.txtPotencia = (TextView) rowView.findViewById(R.id.textView1);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		EffObject object = effObjects.get(position); 

		holder.txtPotencia.setText(object.getPotencia());
		
		
		holder.btnPol2.setText(object.getValues()[0]);
		if(Helper.isNullField(object.getValues()[0])) {
			holder.btnPol2.setClickable(false);
		}
		
		holder.btnPol4.setText(object.getValues()[1]);
		if(Helper.isNullField(object.getValues()[1])) {
			holder.btnPol4.setClickable(false);
		}
		
		holder.btnPol6.setText(object.getValues()[2]);
		if(Helper.isNullField(object.getValues()[2])) {
			holder.btnPol6.setClickable(false);
		}
		
		if(object.getValues().length == 4) {
			holder.btnPol8.setText(object.getValues()[3]);
			if(Helper.isNullField(object.getValues()[3])) {
				holder.btnPol8.setClickable(false);
			}
		} else {
			holder.btnPol8.setVisibility(View.GONE);
		}

		return rowView;
	}
} 