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

public class EnergyCostListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<EnergyCostObject> energyCostObjects;
	private LayoutInflater inflater;
	private EnergyCostHelper energyCostHelper;

	public EnergyCostListAdapter(Context context, ArrayList<EnergyCostObject> energyCostObjects, EnergyCostHelper energyCostHelper) {
		this.context = context;
		this.energyCostObjects = energyCostObjects;
		this.energyCostHelper = energyCostHelper;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return energyCostObjects.size();
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
		public Button btnEletricityIndustry;
		public TextView txtEletricityHouseholds;
		public TextView txtCurrency;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;

		// reuse views
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_energycost_item, null);
			holder = new ViewHolder();

			holder.btnEletricityIndustry = (Button) rowView.findViewById(R.id.btn_industry);
			holder.btnEletricityIndustry.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
		            energyCostHelper.adapterClick(((Button)v).getText().toString());
				}
			});

			holder.txtCountry = (TextView) rowView.findViewById(R.id.txt_country);
			holder.txtEletricityHouseholds = (TextView) rowView.findViewById(R.id.txt_households);
			holder.txtCurrency = (TextView) rowView.findViewById(R.id.txt_currency);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		EnergyCostObject object = energyCostObjects.get(position); 

		holder.txtCountry.setText(object.getCountry());
		holder.txtEletricityHouseholds.setText(object.getEletricityHouseholds());
		holder.txtCurrency.setText(object.getCurrency());
		
		
		holder.btnEletricityIndustry.setText(object.getEletricityIndustry());
		if(object.getEletricityIndustry().equals("x")) {
			holder.btnEletricityIndustry.setClickable(false);
		}

		return rowView;
	}
} 