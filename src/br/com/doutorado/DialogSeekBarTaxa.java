package br.com.doutorado;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DialogSeekBarTaxa extends Dialog implements OnSeekBarChangeListener {
	
	private SeekBar seekBar;
	private TextView txtValue;
	private Button btnOk;
	
	public DialogSeekBarTaxa(Context context, int  resTitle, String value) {
		super(context);
		setContentView(R.layout.dialog_seekbar);
		setTitle(resTitle);
		setCanceledOnTouchOutside(false);
		
		txtValue = (TextView) findViewById(R.id.txtValue);
		txtValue.setText(value);
		
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setProgress(Integer.parseInt(value));
		seekBar.setMax(20);

		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogSeekBarTaxa.this.dismiss();
			}
		});
	}
	
	public String getValue() {
		if(txtValue.getText().toString().equals("")) {
			return "0";
		} else {
			return txtValue.getText().toString();	
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        txtValue.setText("" + progress);
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {}
}
