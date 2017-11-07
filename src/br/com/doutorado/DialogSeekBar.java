package br.com.doutorado;

import java.math.BigDecimal;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DialogSeekBar extends Dialog implements OnSeekBarChangeListener {
	
	private SeekBar seekBar;
	private TextView txtValue;
	private Button btnOk;
	
	private BigDecimal initialValue;
	private int minValue;
	private int maxValue;
	private boolean hasLimit = false;

	private int casasDecimais = 4;
	
	public DialogSeekBar(Context context, int  resTitle, String value) {
		super(context);
		setContentView(R.layout.dialog_seekbar);
		setTitle(resTitle);
		setCanceledOnTouchOutside(false);
		
		initialValue = new BigDecimal(value);
		
		txtValue = (TextView) findViewById(R.id.txtValue);
		txtValue.setText(format(initialValue.toPlainString()));
		
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setProgress(50);

		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogSeekBar.this.dismiss();
			}
		});
	}
	
	public DialogSeekBar(Context context, int  resTitle, String value, int minValue, int maxValue) {
		this(context, resTitle, value);
		
		hasLimit = true;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public String getValue() {
		if(txtValue.getText().toString().equals("")) {
			return "0";
		} else {
			return txtValue.getText().toString();	
		}
	}
	
	private String format(String value) {
		String aux = value;
		
		if(aux.contains(".") == false) {
			aux = aux.concat(".");
		}
		
		for(int i = 0; i < casasDecimais; i++) {
			aux = aux.concat("0");
		}
		
		if(casasDecimais > 0 && aux.contains(".")) {
			aux = aux.substring(0, casasDecimais + aux.indexOf('.') + 1);
		}
		
		return aux;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(progress == 50) {
        	txtValue.setText("" + format(initialValue.toPlainString()));
        } else {
        	int aux = progress - 50;
        	
        	BigDecimal result = new BigDecimal(aux * 0.01).multiply(initialValue).add(initialValue);
        	
        	if(hasLimit) {
        		if(result.doubleValue() > maxValue) {
        			result = new BigDecimal(maxValue);
        		} else if(result.doubleValue() < minValue) {
        			result = new BigDecimal(minValue);
        		}
        	}
        	
        	txtValue.setText("" + format(result.toPlainString()));
        }
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {}
}
