package com.autoai.readnotification;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.autoai.readnotification.adapter.FavoritesAdapter;
import com.autoai.readnotification.models.ContactList;
import com.autoai.readnotification.models.Contacts;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
ArrayList<ContactList> contactLists=new ArrayList<>();
ArrayList<Contacts> contactLists2=new ArrayList<>();
RecyclerView rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ContactList contactList=new ContactList("CDE","1234546879","sober");
        ContactList contactList2=new ContactList("Zahirr","8141119182","hello z");
        contactLists.add(contactList);
        contactLists.add(contactList2);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
        Button button = findViewById(R.id.button);
        rc = findViewById(R.id.rc);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        editText = findViewById(R.id.editText);
        Intent intent = new Intent(MainActivity.this, MyNotifiService.class);//启动服务
        startService(intent);//Start service
        final SharedPreferences sp = getSharedPreferences("msg", MODE_PRIVATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// Loop arrayList2 items
                for (Contacts person2 : contactLists2) {
                    // Loop arrayList1 items
                    boolean found = false;
                    for (ContactList person1 : contactLists) {
                        if (person2.getNumber().trim().replaceAll("[^0-9]", "").equalsIgnoreCase(person1.getNumber().trim().replaceAll("[^0-9]", ""))) {
                            found = true;
                            Log.d("adadadad",person2.getNumber()+"-->"+person1.getName());

                            person1.setName(person2.getName());
                        contactLists.set(getIndexForKey(person1.getName()),person1);
                        }
                        else
                        {
                            Log.d("adadadad",person1.getNumber()+"not matched "+person2.getNumber());
                        }
                    }

                }
                Toast.makeText(MainActivity.this, ""+sp.getString("getMsg", ""), Toast.LENGTH_SHORT).show();
                String getMsg = sp.getString("getMsg", "");
                if (!TextUtils.isEmpty(getMsg)) {
                    editText.setText(getMsg);
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {

                for (int i=0;i<contactLists.size();i++)
                {
                    ContactList contactList1=contactLists.get(i);
                    Log.d("asas",contactList1.getName()+"--"+contactList1.getNumber());
                }
                //Open listener reference message // Notification access
                /*Intent intent_s = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                startActivity(intent_s);*/
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<contactLists.size();i++)
                {
                    ContactList contactList1=contactLists.get(i);
                    Log.d("asas",contactList1.getName()+"--"+contactList1.getNumber());
                }
            }
        });

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        Log.d("nnnnn",cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))+"-"+cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                        Contacts contacts=new Contacts(""+cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),""+cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        contactLists2.add(contacts);
                    } while (cursor.moveToNext());
                    rc.setAdapter(new FavoritesAdapter(contactLists2,MainActivity.this));
                }
            }
        }

    }
    private int getIndexForKey(String key) {
        int index = 0;
        for (ContactList snapshot : contactLists) {
            if (snapshot.getName().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }
    public void openNotificationSettings(View view) {
        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }
}