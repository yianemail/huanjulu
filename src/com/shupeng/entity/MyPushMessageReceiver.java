package com.shupeng.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.example.android_robot.MainActivity;
import com.example.android_robot.R;
import com.shupeng.util.Utils;

/**
 * Push��Ϣ����receiver�����д����Ҫ�Ļص������� һ����˵�� onBind�Ǳ���ģ���������startWork����ֵ��
 * onMessage��������͸����Ϣ�� onSetTags��onDelTags��onListTags��tag��ز����Ļص���
 * onNotificationClicked��֪ͨ�����ʱ�ص��� onUnbind��stopWork�ӿڵķ���ֵ�ص�
 * 
 * ����ֵ�е�errorCode���������£� 
 *  0 - Success
 *  10001 - Network Problem
 *  30600 - Internal Server Error
 *  30601 - Method Not Allowed 
 *  30602 - Request Params Not Valid
 *  30603 - Authentication Failed 
 *  30604 - Quota Use Up Payment Required 
 *  30605 - Data Required Not Found 
 *  30606 - Request Time Expires Timeout 
 *  30607 - Channel Token Timeout 
 *  30608 - Bind Relation Not Found 
 *  30609 - Bind Number Too Many
 * 
 * �����������Ϸ��ش���ʱ��������Ͳ����������⣬����ͬһ����ķ���ֵrequestId��errorCode��ϵ����׷�����⡣
 * 
 */
public class MyPushMessageReceiver extends FrontiaPushMessageReceiver {
    /** TAG to Log */
    public static final String TAG = MyPushMessageReceiver.class
            .getSimpleName();

    /**
     * ����PushManager.startWork��sdk����push
     * server�������������������첽�ġ�������Ľ��ͨ��onBind���ء� �������Ҫ�õ������ͣ���Ҫ�������ȡ��channel
     * id��user id�ϴ���Ӧ��server�У��ٵ���server�ӿ���channel id��user id�������ֻ������û����͡�
     * 
     * @param context
     *            BroadcastReceiver��ִ��Context
     * @param errorCode
     *            �󶨽ӿڷ���ֵ��0 - �ɹ�
     * @param appid
     *            Ӧ��id��errorCode��0ʱΪnull
     * @param userId
     *            Ӧ��user id��errorCode��0ʱΪnull
     * @param channelId
     *            Ӧ��channel id��errorCode��0ʱΪnull
     * @param requestId
     *            �����˷��������id����׷������ʱ���ã�
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
            String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d(TAG, responseString);
        System.out.println(responseString);
        // �󶨳ɹ��������Ѱ�flag��������Ч�ļ��ٲ���Ҫ�İ�����
        if (errorCode == 0) {
            Utils.setBind(context, true);
        }
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
      //  updateContent(context, responseString);
        StorageReceiverInfo(context, userId,channelId);
    }

    /**
     * ����͸����Ϣ�ĺ�����
     * 
     * @param context
     *            ������
     * @param message
     *            ���͵���Ϣ
     * @param customContentString
     *            �Զ�������,Ϊ�ջ���json�ַ���
     */
    @Override
    public void onMessage(Context context, String message,
            String customContentString) {
        String messageString = "͸����Ϣ message=\"" + message
                + "\" customContentString=" + customContentString;
       // Toast.makeText(context, messageString, Toast.LENGTH_SHORT).show();
        Log.d(TAG, messageString);
        // �Զ������ݻ�ȡ��ʽ��mykey��myvalue��Ӧ͸����Ϣ����ʱ�Զ������������õļ���ֵ
       boolean flag= Util.isRunning(context, "com.example.android_robot");
       if(flag==false){
    	   String svcName = Context.NOTIFICATION_SERVICE;  
	        NotificationManager notificationManager;  
	        notificationManager = (NotificationManager)context.getSystemService(svcName);  
	        //ͨ��ʹ��֪ͨ�����������Դ����µ�֪ͨ���޸����е�֪ͨ����ɾ����Щ������Ҫ��֪ͨ��  
	          
	        /**Android�ṩ��ʹ��֪ͨ���û�������Ϣ�Ķ��ַ�ʽ�� 
	         * 1.״̬��ͼ�� 
	         * 2.��չ��֪ͨ״̬������ 
	         * 3.�����Ч���������������� 
	         */  
	        //����һ��֪ͨ  
	        //ѡ��һ��Drawable����Ϊ״̬��ͼ�����ʾ  
	        int icon = R.drawable.ic_launcher;  
	        //������֪ͨʱ��״̬����ʾ���ı�  
	        String tickerText = "���ܣ���á�";  
	        //��չ��״̬����ʱ��˳������֪ͨ  
	        long when = System.currentTimeMillis();  
	        Notification notification = new Notification(icon, tickerText, when);  
	          
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;  
	        //չ����״̬��������ʾ���ı�  
	        String expandedText = "�Ұ���";  
	        //չ����״̬�ı���  
	        String expandedTitle = "ez";  
	        //������չ�����ı�ʱ����������һ�������ͼ��  
	        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);  
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        Util.logStringCache=messageString;
	        PendingIntent launchIntent = PendingIntent.getActivity(context, 0, intent, 0);  
	        notification.setLatestEventInfo(context, expandedTitle, expandedText, launchIntent);  
	        //Ҫ����һ��֪ͨ����Ҫ������һ�����͵�����IDһ�𴫵ݸ�NotificationManager��notify����  
	        int notificationRef = 1;  
	        notificationManager.notify(notificationRef, notification);  
	        return;
       }else if(flag==true){
    	   StartIntent(context,message);
    	   return;
       }
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
         
    }

    private void StartIntent(Context context, String messageString) {
		// TODO Auto-generated method stub
    	Util.logStringCache=messageString;
    	Intent intent = new Intent();
      intent.setClass(context.getApplicationContext(), MainActivity.class);
      intent.putExtra("content", messageString);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.getApplicationContext().startActivity(intent);
		
	}

	/**
     * ����֪ͨ����ĺ�����ע������֪ͨ���û����ǰ��Ӧ���޷�ͨ���ӿڻ�ȡ֪ͨ�����ݡ�
     * 
     * @param context
     *            ������
     * @param title
     *            ���͵�֪ͨ�ı���
     * @param description
     *            ���͵�֪ͨ������
     * @param customContentString
     *            �Զ������ݣ�Ϊ�ջ���json�ַ���
     */
    @Override
    public void onNotificationClicked(Context context, String title,
            String description, String customContentString) {
        String notifyString = "֪ͨ��� title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Log.d(TAG, notifyString);

        // �Զ������ݻ�ȡ��ʽ��mykey��myvalue��Ӧ֪ͨ����ʱ�Զ������������õļ���ֵ
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                    Toast.makeText(context, myvalue, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
        StartIntent(context, description);
    }

    /**
     * setTags() �Ļص�������
     * 
     * @param context
     *            ������
     * @param errorCode
     *            �����롣0��ʾĳЩtag�Ѿ����óɹ�����0��ʾ����tag�����þ�ʧ�ܡ�
     * @param successTags
     *            ���óɹ���tag
     * @param failTags
     *            ����ʧ�ܵ�tag
     * @param requestId
     *            ������������͵������id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
      //  updateContent(context, responseString);
    }

    /**
     * delTags() �Ļص�������
     * 
     * @param context
     *            ������
     * @param errorCode
     *            �����롣0��ʾĳЩtag�Ѿ�ɾ���ɹ�����0��ʾ����tag��ɾ��ʧ�ܡ�
     * @param successTags
     *            �ɹ�ɾ����tag
     * @param failTags
     *            ɾ��ʧ�ܵ�tag
     * @param requestId
     *            ������������͵������id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
      //  updateContent(context, responseString);
    }

    /**
     * listTags() �Ļص�������
     * 
     * @param context
     *            ������
     * @param errorCode
     *            �����롣0��ʾ�о�tag�ɹ�����0��ʾʧ�ܡ�
     * @param tags
     *            ��ǰӦ�����õ�����tag��
     * @param requestId
     *            ������������͵������id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
            String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Log.d(TAG, responseString);

        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
       // updateContent(context, responseString);
    }

    /**
     * PushManager.stopWork() �Ļص�������
     * 
     * @param context
     *            ������
     * @param errorCode
     *            �����롣0��ʾ�������ͽ�󶨳ɹ�����0��ʾʧ�ܡ�
     * @param requestId
     *            ������������͵������id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Log.d(TAG, responseString);

        // ��󶨳ɹ�������δ��flag��
        if (errorCode == 0) {
            Utils.setBind(context, false);
        }
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
       // updateContent(context, responseString);
    }

//    private void updateContent(Context context, String content) {
//        Log.d(TAG, "updateContent");
//       SharedPreferences mSharedPreferences= context.getSharedPreferences("TAG", Activity.MODE_PRIVATE);
//       SharedPreferences.Editor editor = mSharedPreferences.edit(); 
//       editor.putString("channled", content); 
//       editor.putString("userid", content); 
//       editor.commit(); 
//       Toast.makeText(context, "SUCCESS", Toast.LENGTH_LONG).show();
//        String logText = "" + Utils.logStringCache;
//
//        if (!logText.equals("")) {
//            logText += "\n";
//        }
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
//        logText += sDateFormat.format(new Date()) + ": ";
//        logText += content;
//
//        Utils.logStringCache = logText;
//
//        Intent intent = new Intent();
//        intent.setClass(context.getApplicationContext(), PushDemoActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.getApplicationContext().startActivity(intent);
//    }
    private void StorageReceiverInfo(Context context, String userId,String channelId) {
    	// TODO Auto-generated method stub
    		 SharedPreferences mSharedPreferences= context.getSharedPreferences("TuiSongIC", Activity.MODE_PRIVATE);
    	     SharedPreferences.Editor editor = mSharedPreferences.edit(); 
    	       editor.putString("userid", userId); 
    	       editor.putString("channelid", channelId); 
    	       editor.commit(); 
    }
}
