package graphics;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardClient {
	public static void hideKeyboard(Activity activity){
		InputMethodManager inputManager = (InputMethodManager) activity
	            .getSystemService(Context.INPUT_METHOD_SERVICE);
		 View v= activity.getCurrentFocus();
		inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
