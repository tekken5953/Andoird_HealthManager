package app.healthmanager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;
    Integer count = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentActivity = new Intent(context, DetailActivity.class);
        intentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, count, intentActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"default");
            builder.setContentText(count + "번째 알림입니다");
            builder.setSmallIcon(R.drawable.notification_img);
            builder.setContentTitle("약 드실 시간이에요:)");
            builder.setWhen(System.currentTimeMillis());
            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
            Bitmap large = BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.notification_img);
            builder.setLargeIcon(large);
            Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context,
                    RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(ringtoneUri);
            long[] vibrate = {0, 100, 200, 300};
            builder.setVibrate(vibrate);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                assert notificationManager != null;
                notificationManager.createNotificationChannel(new NotificationChannel("default", "Channel",
                        NotificationManager.IMPORTANCE_DEFAULT)); }
            assert notificationManager != null;
            notificationManager.notify(1,builder.build());
            count++;
    }
}