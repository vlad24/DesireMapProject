package logic.gcm;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class GcmIntentService extends IntentService {

	String message;
	String sender;
	private Handler handler;
	private GoogleCloudMessaging gcm;
	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		handler = new Handler();
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();

		if(gcm == null)
			gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);
		
		if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
			sender = extras.getString("sender");
			message = extras.getString("message");
			showToast();
			sendNotification();
			Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("message"));
		}
		
		GcmBroadcastReceiver.completeWakefulIntent(intent);

	}

	public void showToast(){
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(),message , Toast.LENGTH_LONG).show();
			}
		});

	}
	
	private void sendNotification() {
		
		Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("GCM_RECEIVED_ACTION");
        broadcastIntent.putExtra("sender", sender);
        broadcastIntent.putExtra("message", message);
     
        getApplicationContext().sendBroadcast(broadcastIntent);
	  }

}
