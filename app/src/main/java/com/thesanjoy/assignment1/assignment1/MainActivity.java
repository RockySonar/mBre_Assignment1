package com.thesanjoy.assignment1.assignment1;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public TextView outputText;
    public Button btn_load,btn_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        outputText = (TextView) findViewById(R.id.textView);

        btn_load = (Button) findViewById(R.id.btn_load);
        btn_map = (Button) findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MapsActivity2.class);
                startActivity(intent);
            }
        });

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable(){
                    ArrayList<String> urls;
                    public void run(){


                        urls=new ArrayList<String>(); //to read each line
                        //TextView t; //to show the result, please declare and find it inside onCreate()
                        try {
                            // Create a URL for the desired page
                            URL url = new URL("http://www.cs.columbia.edu/~coms6998-8/assignments/homework2/contacts/contacts.txt"); //My text file location
                            //First open the connection
                            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(60000); // timing out in a minute

                            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                            //t=(TextView)findViewById(R.id.TextView1); // ideally do this in onCreate()
                            String str;
                            while ((str = in.readLine()) != null) {
                                urls.add(str);
                            }
                            in.close();
                        } catch (Exception e) {
                            Log.d("MyTag",e.toString());
                        }

                        //since we are in background thread, to post results we have to go back to ui thread. do the following for that

                        MainActivity.this.runOnUiThread(new Runnable(){
                            String str="";
                            public void run(){
                                for(int i=0;i<urls.size();i++) {
                                    String str1 = urls.get(i);
                                    String[] str2 = str1.split(" ");
                                    //str += "Name : " +str2[0]+" Email : "+str2[1]+" Mobile : "+str2[2]+" Home : "+str2[3]+ "\n";



                                ArrayList<ContentProviderOperation> ops =
                                        new ArrayList<ContentProviderOperation>();

                                int rawContactID = ops.size();

                                // Adding insert operation to operations list
                                // to insert a new raw contact in the table ContactsContract.RawContacts
                                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                        .build());

                                // Adding insert operation to operations list
                                // to insert display name in the table ContactsContract.Data
                                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, str2[0])
                                        .build());

                                // Adding insert operation to operations list
                                // to insert Mobile Number in the table ContactsContract.Data
                                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, str2[2])
                                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                        .build());

                                // Adding insert operation to operations list
                                // to  insert Home Phone Number in the table ContactsContract.Data
                                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, str2[3])
                                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                                        .build());

                                // Adding insert operation to operations list
                                // to insert Home Email in the table ContactsContract.Data
                                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, str2[1])
                                        .build());

                                // Adding insert operation to operations list
                                // to insert Work Email in the table ContactsContract.Data

                                try{
                                    // Executing all the insert operations as a single database transaction
                                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                                    Toast.makeText(getBaseContext(), "Contact is successfully added", Toast.LENGTH_SHORT).show();
                                }catch (RemoteException e) {
                                    e.printStackTrace();
                                }catch (OperationApplicationException e) {
                                    e.printStackTrace();
                                }
                                //outputText.setText(str);
                            }
                            }
                        });

                    }
                }).start();

            }
        });

        //getContactList();

    }


    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Log.i( "Contact_phone" , phoneNo);
                        //Log.i( "Contact_email" , email);

                    }
                    pCur.close();
                    Cursor mCur1 = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (mCur1.moveToNext()) {

                        String email = mCur1.getString(mCur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        Log.i("Contact_name", "Name: " + name);
                        Log.e("Email", email);
                        if(email!=null){
                            Log.i("Contact_email",email);
                        }
                    }
                    mCur1.close();


                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }
}
