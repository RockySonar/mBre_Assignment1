package com.thesanjoy.assignment1.assignment1;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng loc1,loc2,loc3=null,loc4=null,loc5;

        //ArrayList<UserDetails> datas = new ArrayList<UserDetails>(5);
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            int j=0;
            while (cur != null && cur.moveToNext()) {
                j++;
                int i=0;

                String email="";
                String[] nos = new String[2];
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
                        nos[i++] = phoneNo;
                        Log.i( "Contact_phone" , phoneNo);
                        //Log.i( "Contact_email" , email);
                        if(i>1)
                            break;
                    }
                    pCur.close();
                    Cursor mCur1 = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (mCur1.moveToNext()) {

                        email = mCur1.getString(mCur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        //Log.i("Contact_name", "Name: " + name);
                        //Log.e("Email", email);
                        if(email!=null){
                            Log.i("Contact_email",email);
                        }
                    }
                    mCur1.close();


                }
                if(j==1) {
                    loc1 = new LatLng(Long.parseLong(nos[1]), Long.parseLong(nos[0]));
                    mMap.addMarker(new MarkerOptions().position(loc1).title(name).snippet(email));
                }
                else if(j==2) {
                    loc2 = new LatLng(Long.parseLong(nos[1]), Long.parseLong(nos[0]));
                    mMap.addMarker(new MarkerOptions().position(loc2).title(name).snippet(email));
                }
                else if(j==3) {
                    loc3 = new LatLng(Long.parseLong(nos[1]), Long.parseLong(nos[0]));
                    mMap.addMarker(new MarkerOptions().position(loc3).title(name).snippet(email));
                }
                else if(j==4) {
                    loc4 = new LatLng(Long.parseLong(nos[1]), Long.parseLong(nos[0]));
                    mMap.addMarker(new MarkerOptions().position(loc4).title(name).snippet(email));
                }
                else if(j==5) {
                    loc5 = new LatLng(Long.parseLong(nos[1]), Long.parseLong(nos[0]));
                    mMap.addMarker(new MarkerOptions().position(loc5).title(name).snippet(email));
                }
           //     UserDetails user = new UserDetails(name,email,nos[0],nos[1]);
             //   datas.add(user);

                //Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, nos[1], Toast.LENGTH_SHORT).show();

            }

        }
        if(cur!=null){
            cur.close();
        }
        /*LatLng loc;
        for(int i=0;i<datas.size();i++){
            UserDetails u = new UserDetails();



        }*/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc4));
        googleMap.setOnMarkerClickListener(this);

        /*googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {
                Toast.makeText(MapsActivity2.this, "Lat : "+arg0.latitude+", Long : "+arg0.longitude, Toast.LENGTH_SHORT).show();
                android.util.Log.i("onMapClick", "Horray!");
            }
        });*/
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //Toast.makeText(this, marker.getSnippet(), Toast.LENGTH_SHORT).show();
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
                //set title
                .setTitle("Name : "+marker.getTitle())
                //set message
                .setMessage("Email : "+marker.getSnippet())
                //set positive button
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        //finish();
                    }
                })

                .show();
        return false;
    }
}
