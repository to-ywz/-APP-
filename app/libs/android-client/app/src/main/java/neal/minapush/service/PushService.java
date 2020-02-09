package neal.minapush.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import neal.minapush.activity.MainActivity;
import neal.minapush.applicaiton.BaseApplication;
import neal.minapush.push.PushEventListener;
import neal.minapush.push.PushManager;

public class PushService extends Service{

    PushManager pushManager=PushManager.getInstance();

    @Override
    public void onCreate() {
        System.out.println("service start"+android.os.Process.myPid()+'-'+android.os.Process.myTid());
        super.onCreate();
        pushManager.openPush();
        pushManager.setPushEventListener(new PushEventListener() {
            @Override
            public void onPushConnected() {
                String string = "service push open" + android.os.Process.myPid()+ '-' + android.os.Process.myTid();
                //System.out.println("service push open"+android.os.Process.myPid()+'-'+android.os.Process.myTid());
                Toast.makeText(BaseApplication.getAppContext(), string, Toast.LENGTH_LONG);
            }

            @Override
            public void onPushExceptionCaught() {
                //System.out.println("service push exception"+android.os.Process.myPid()+'-'+android.os.Process.myTid());
                //Toast.makeText(BaseApplication.getAppContext());
                String string = "service push exception"+android.os.Process.myPid()+'-'+android.os.Process.myTid();
                Toast.makeText(BaseApplication.getAppContext(), string, Toast.LENGTH_LONG);
            }

            @Override
            public void onPushMessageSent() {
                //System.out.println("service push sent"+android.os.Process.myPid()+'-'+android.os.Process.myTid());
                String string = "service push sent"+android.os.Process.myPid()+'-'+android.os.Process.myTid();
                Toast.makeText(BaseApplication.getAppContext(), string, Toast.LENGTH_LONG);
            }

            @Override
            public void onPushMessageReceived() {

            }

            @Override
            public void onPushDisConnected() {
                //System.out.println("service push close"+android.os.Process.myPid()+'-'+android.os.Process.myTid());
                String string = "service push close"+android.os.Process.myPid()+'-'+android.os.Process.myTid();
                Toast.makeText(BaseApplication.getAppContext(), string, Toast.LENGTH_LONG);
            }
        });

        //System.out.println(pushManager.Connect());
        boolean staut = pushManager.Connect();
        String temp = null;

        if (staut){
            temp = "true";
        }
        else {
            temp = "false";
        }
        Toast toast = Toast.makeText(BaseApplication.getAppContext(), temp, Toast.LENGTH_LONG);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("service start command"+android.os.Process.myPid()+'-'+android.os.Process.myTid());
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        pushManager.disConnect();
    }


}
