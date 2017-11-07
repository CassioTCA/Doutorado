package br.com.doutorado;

import br.com.doutorado.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;

public class ActivitySobre extends Activity implements OnTouchListener {

	View rootView;
	boolean zoomedIn = false;
	boolean firstTouch = false;
	private long time = 0;
	PointF relativePoint;
	PointF viewSize;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sobre);

		rootView = findViewById(R.id.rootView);

		rootView.setOnTouchListener(this);
		
		rootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				viewSize = new  PointF(rootView.getWidth(), rootView.getHeight());
				rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}
	
	@Override
    protected void onResume() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogLayout = inflater.inflate(R.layout.dialog_dont_show_again, null);
        final CheckBox checkBox = (CheckBox) dialogLayout.findViewById(R.id.checkBox1);
        
        dialog.setView(dialogLayout);
        dialog.setTitle(R.string.mostrar_novamente_titulo);
        dialog.setMessage(R.string.mostrar_novamente_sobre_texto);
        
        final SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
        
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	
            	
                if (checkBox.isChecked()) {
                	SharedPreferences.Editor editor = settings.edit();
                	editor.putString(Utils.PREF_SOBRE, "not_show_again");
                	editor.commit();
                }
                
                return;
            }
        });
        
        if (settings.getString(Utils.PREF_SOBRE, "").equals("")) {
        	dialog.show();
        }

        super.onResume();
    }
	
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
	}

	
	private void zoom() {
		if(zoomedIn) { // zoom out
			ScaleAnimation anim = new ScaleAnimation(2f, 1f, 2f, 1f, Animation.RELATIVE_TO_SELF, relativePoint.x, Animation.RELATIVE_TO_SELF, relativePoint.y);
			anim.setDuration(500);
			anim.setFillAfter(true);
			rootView.startAnimation(anim);
			zoomedIn = false;
		} else {
			ScaleAnimation anim = new ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, relativePoint.x, Animation.RELATIVE_TO_SELF, relativePoint.y);
			anim.setDuration(500);
			anim.setFillAfter(true);
			rootView.startAnimation(anim);
			zoomedIn = true;
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		int action = event.getAction();

		switch(action)
		{
			case MotionEvent.ACTION_DOWN:
			{
				if(firstTouch && (System.currentTimeMillis() - time) <= 300) {
					if(!zoomedIn) {
						relativePoint = new PointF(event.getX() / viewSize.x, event.getY() / viewSize.y);
					}
					zoom();
					firstTouch = false;
				} else {
					firstTouch = true;
					time = System.currentTimeMillis();
				}
				break;
			}
	
			case MotionEvent.ACTION_MOVE:
			{
				firstTouch = false;
				if(zoomedIn) {
					return true;
				}
				break;
			}
		}

		return false;
	}

}