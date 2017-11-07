package br.com.doutorado.helper;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import br.com.doutorado.R;

public class FatorDescontoListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<FatorDescontoObject> fatorDescontoObjects;
	private LayoutInflater inflater;
	private FatorDescontoHelper fatorDescontoHelper;

	public FatorDescontoListAdapter(Context context, ArrayList<FatorDescontoObject> fatorDescontoObjects, FatorDescontoHelper fatorDescontoHelper) {
		this.context = context;
		this.fatorDescontoObjects = fatorDescontoObjects;
		this.fatorDescontoHelper = fatorDescontoHelper;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return fatorDescontoObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public Button btnTx;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;

		// reuse views
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_fator_desconto_item, null);
			holder = new ViewHolder();

			holder.btnTx = (Button) rowView.findViewById(R.id.button1);
			holder.btnTx.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
		            fatorDescontoHelper.adapterClick(((Button)v).getText().toString(), ((Button)v).getText().toString());
				}
			});

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		FatorDescontoObject object = fatorDescontoObjects.get(position); 
		
		
		holder.btnTx.setText(object.getFatorDesconto());
		if(object.getFatorDesconto().equals("x")) {
			holder.btnTx.setClickable(false);
		}

		return rowView;
	}
} 