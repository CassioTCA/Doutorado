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

public class TxListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<TxObject> txObjects;
	private LayoutInflater inflater;
	private TxHelper txHelper;

	public TxListAdapter(Context context, ArrayList<TxObject> txObjects, TxHelper txHelper) {
		this.context = context;
		this.txObjects = txObjects;
		this.txHelper = txHelper;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return txObjects.size();
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
		public TextView txtCountry;
		public Button btnTx;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;

		// reuse views
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_tx_item, null);
			holder = new ViewHolder();

			holder.btnTx = (Button) rowView.findViewById(R.id.button1);
			holder.btnTx.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LinearLayout linearLayout = (LinearLayout)v.getParent();
		            TextView txtPotencia = (TextView)linearLayout.findViewById(R.id.textView1);
		            txHelper.adapterClick(((Button)v).getText().toString(), txtPotencia.getText().toString());
				}
			});

			holder.txtCountry = (TextView) rowView.findViewById(R.id.textView1);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		TxObject object = txObjects.get(position); 

		holder.txtCountry.setText(object.getCountry());
		
		
		holder.btnTx.setText(object.getTx());
		if(object.getTx().equals("x")) {
			holder.btnTx.setClickable(false);
		}

		return rowView;
	}
} 