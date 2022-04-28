package com.example.seccion_13_notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Notification;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

// FIND FILE = CTRL + N
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editTextTextTitle)
    EditText editTextTitle;
    @BindView(R.id.editTextTextMessage)
    EditText editTextMessage;
    @BindView(R.id.switchImportance)
    SwitchCompat switchImportance;

    @BindString(R.string.switch_notifications_on)
    String switchTextOn;
    @BindString(R.string.switch_notifications_off)
    String switchTextOff;

    private boolean isHighImportance = false;
    private NotificationsHandler notificationsHandler;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //MUST Bind Right after sentContentView to avoid NullPointerException
        ButterKnife.bind(this);

        notificationsHandler = new NotificationsHandler(this);
    }

    @OnClick(R.id.buttonSend)
    public void click() {
        sendNotification();
    }

    @OnCheckedChanged(R.id.switchImportance)
    public void change(CompoundButton compoundButton, boolean b) {
        isHighImportance = b;
        switchImportance.setText((isHighImportance) ? switchTextOn : switchTextOff);
    }

    private void sendNotification() {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        if (!title.isEmpty() && !message.isEmpty()) {
            Notification.Builder nb = notificationsHandler.createNotification(title, message, isHighImportance);
            notificationsHandler.getManager().notify(++counter, nb.build());
            notificationsHandler.publishNotificationSummaryGroup(isHighImportance);
        }
    }
}