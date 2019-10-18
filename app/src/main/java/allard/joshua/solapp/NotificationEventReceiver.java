package allard.joshua.solapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import allard.joshua.solapp.parser.BaseTemplateParser;
import allard.joshua.solapp.parser.PageParser;

public class NotificationEventReceiver extends WakefulBroadcastReceiver {

    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";
    private static final String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION";
    private static final String ACTION_CHECK_NO_LOAD_NOTIFICATION = "ACTION_CHECK_NO_LOAD_NOTIFICATION";
    private static final int NOTIFICATIONS_INTERVAL_IN_MILLI = 60000;

    private static int lastMailCount = 0;
    private static int lastEventCount = 0;
    private static int lastAnnouncementCount = 0;

    public static void setupAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                getTriggerAt(new Date()),
                NOTIFICATIONS_INTERVAL_IN_MILLI,
                alarmIntent);
    }

    public static void triggerNow(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getCheckNoLoadIntent(context);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                getTriggerAt(new Date()),
                alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Login-Notification", Connector.loggedIn + "");
        String action = intent.getAction();
        if(!Connector.loggedIn){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Connector.login(context, prefs.getString("usernameKey", ""), prefs.getString("passwordKey", ""));
            Log.d("Login-Notification", Connector.loggedIn + "");
        }
        try{
            if(!ACTION_CHECK_NO_LOAD_NOTIFICATION.equals(action)) {
                Connector.loadPage(null, "/explore.php", context);
            }

            BaseTemplateParser parser = PageParser.GetTemplateInfo();
            int mailCount = parser.GetMailCount();
            int eventCount = parser.GetEventCount();
            int announcementCount = parser.GetAnnouncementCount();
            int totalCount = mailCount + eventCount + announcementCount;
            Intent serviceIntent = null;
            if((mailCount != lastMailCount ||  eventCount != lastEventCount || announcementCount != lastAnnouncementCount) && totalCount > 0){
                if (!ACTION_DELETE_NOTIFICATION.equals(action)) {
                    Log.i(getClass().getSimpleName(), "onReceive from alarm, starting notification service");
                    serviceIntent = NotificationService.createIntentStartNotificationService(context);
                }
            }
            else if(totalCount == 0) {
                serviceIntent = NotificationService.createIntentDeleteNotification(context);
            }

            if (serviceIntent != null) {
                startWakefulService(context, serviceIntent);
            }
            lastMailCount = mailCount;
            lastEventCount = eventCount;
            lastAnnouncementCount = announcementCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long getTriggerAt(Date now) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(now);
        return calendar.getTimeInMillis();
    }

    private static PendingIntent getStartPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDeleteIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        intent.setAction(ACTION_DELETE_NOTIFICATION);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getCheckNoLoadIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        intent.setAction(ACTION_CHECK_NO_LOAD_NOTIFICATION);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
