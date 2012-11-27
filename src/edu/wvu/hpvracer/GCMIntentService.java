package edu.wvu.hpvracer;
/* http://developer.android.com/guide/google/gcm/gs.html
 * STEP 3: WRITE THE my_app_package.GCMIntentService CLASS
 * 
 * Next write the my_app_package.GCMIntentService class, 
 * overriding the following callback methods 
 * (which are called by GCMBroadcastReceiver):
 */
import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	
	public GCMIntentService() {
		// this constructor according to: http://stackoverflow.com/questions/11332034/google-gcmintentservice-instantiate
        super("Test");
    }
	
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		/* Called when the device tries to register or unregister, 
		 * but GCM returned an error. Typically, there is nothing 
		 * to be done other than evaluating the error (returned by 
		 * errorId) and trying to fix the problem.
		 */
		
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		/* Called when your server sends a message to GCM, 
		 * and GCM delivers it to the device. If the message has 
		 * a pay-load, its contents are available as extras in the intent.
		 */
		
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		/* Called after a registration intent is received, 
		 * passes the registration ID assigned by GCM to that 
		 * device/application pair as parameter. 
		 * Typically, you should send the regid to your server 
		 * so it can use it to send messages to this device.
		 */
		
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		/* Called after the device has been unregistered from GCM. 
		 * Typically, you should send the regid to the server so 
		 * it unregisters the device.
		 */
		
	}
	
	public boolean onRecoverableError(Context context, String errorId){
		/* Called when the device tries to register or unregister,
		 * but the GCM servers are unavailable. The GCM library
		 * will retry the operation using exponential backup,
		 * unless this method is overridden and returns false.
		 * This method is optional and should be overridden only
		 * if you want to display the message to the user or 
		 * cancel the retry attempts.
		 */
		Boolean myReturn = false;
		return myReturn;
 	}

}
