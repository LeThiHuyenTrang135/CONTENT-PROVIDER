package com.example.contentprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_SMS = 100;
    private ListView smsListView;
    private ContactsAdapter adapter;
    private ArrayList<String> smsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        smsListView = findViewById(R.id.contacts_list_view);
        smsList = new ArrayList<>();
        adapter = new ContactsAdapter(this, smsList);
        smsListView.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, PERMISSIONS_REQUEST_READ_SMS);
        } else {
            readSms();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readSms();
            } else {
                Toast.makeText(this, "Permission denied to read SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readSms() {
        ContentResolver contentResolver = getContentResolver();
        Uri smsUri = Uri.parse("content://sms/inbox");
        Cursor cursor = contentResolver.query(smsUri, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int bodyIndex = cursor.getColumnIndex("body");
                int addressIndex = cursor.getColumnIndex("address");

                if (bodyIndex >= 0 && addressIndex >= 0) {
                    String body = cursor.getString(bodyIndex);
                    String address = cursor.getString(addressIndex);
                    String smsInfo = "From: " + address + ", Message: " + body;
                    smsList.add(smsInfo);
                    Log.i("SMS", smsInfo);
                }
            }
            cursor.close();
            adapter.notifyDataSetChanged();
        } else {
            Log.i("SMS", "No SMS found.");
        }
    }
}