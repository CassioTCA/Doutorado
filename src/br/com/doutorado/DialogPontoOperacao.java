package br.com.doutorado;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import br.com.doutorado.helper.PontoOperacaoHelper;
import br.com.doutorado.interfaces.HelperFinish;

public class DialogPontoOperacao extends Activity {
	
	private EditText editX;
	private EditText editY;
	private Button btnOk;
	
	private PontoOperacaoHelper tabelaPontoOperacao;
	
	//?? ponto de operacao pode ser inteiro? por enquanto está sendo
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_ponto_operacao);
		
		setTheme(R.style.AppTheme);
		
		String x = getIntent().getStringExtra("pointx");
		String y = getIntent().getStringExtra("pointy");
		
		editX = (EditText) findViewById(R.id.editPontoX);
		editX.setText(x);
		editX.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaPontoOperacao.startDialogs();
				return true;
			}
		});
		
		editY = (EditText) findViewById(R.id.editPontoY);
		editY.setText(y);
		editY.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaPontoOperacao.startDialogs();
				return true;
			}
		});
		
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult();
				DialogPontoOperacao.this.finish();
			}
		});

		
		tabelaPontoOperacao = new PontoOperacaoHelper(this);
		tabelaPontoOperacao.setListener(new HelperFinish() {

			@Override
			public void helperFinished(String... results) {
				editX.setText(results[0]);
				editY.setText(results[1]);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		setResult();
		finish();
	}
	
	private void setResult() {
		if(editY.getText().toString().equals("")) {
			editY.setText("0");
		}
		
		if(editX.getText().toString().equals("")) {
			editX.setText("0");
		}
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra("pointx", editX.getText().toString());
		returnIntent.putExtra("pointy", editY.getText().toString());
		setResult(RESULT_OK, returnIntent);
	}
}