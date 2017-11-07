package br.com.doutorado.widget;

import br.com.doutorado.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.graphics.PorterDuff;
import android.widget.ImageView;

public class CustomImageView extends ImageView {

	/**
	 * @param context
	 */
	public CustomImageView(Context context)
	{
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CustomImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.MULTIPLY);
		} else if(event.getAction() == MotionEvent.ACTION_UP) {
			clearColorFilter();
		} else if(event.getAction() == MotionEvent.ACTION_CANCEL) {
			clearColorFilter();
		}
		
		super.onTouchEvent(event);
		return true;

	}

}
