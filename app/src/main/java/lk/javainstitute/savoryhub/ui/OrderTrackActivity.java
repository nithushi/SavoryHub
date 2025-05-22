package lk.javainstitute.savoryhub.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.Manifest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import lk.javainstitute.savoryhub.R;

public class OrderTrackActivity extends AppCompatActivity {

    TextView orderIdTxt;
    ImageView call;
    private SharedPreferences sharedPreferences;
    private LatLng deliveryLatLng;
    private LatLng currentLatLng;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_track);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPreferences = getSharedPreferences("lk.javainstitute.savoryhub.data",MODE_PRIVATE);

        SupportMapFragment supportMapFragment = new SupportMapFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout1 , supportMapFragment);
        fragmentTransaction.commit();

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                Log.i("GoogleMap", "Map Is Ready!");

                if (hasLocationPermissions()) {
                    getCurrentLocation();
                } else {
                    requestLocationPermissions();
                }
                loadDeliveryAddress();
            }
        });

        //set orderId
        Intent intent = getIntent();
        int orderId = intent.getIntExtra("orderId", 0);

        orderIdTxt = findViewById(R.id.map_order_id);
        orderIdTxt.setText(String.valueOf("#" +orderId));

        //animate call btn
        ConstraintLayout constraintLayout = findViewById(R.id.call_layout);
        Animation shakeAnimation = AnimationUtils.loadAnimation(OrderTrackActivity.this, R.anim.shake_button);
        constraintLayout.startAnimation(shakeAnimation);

        callPhone();
        loadDeliveryAddress();


    }

    private void callPhone() {

        call = findViewById(R.id.call_img);
        if (call == null) {
            Log.e("OrderTrackActivity", "call ImageView is null");
        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_CALL);
                Uri uri = Uri.parse("tel: 0771234567");
                intent1.setData(uri);
                if (ContextCompat.checkSelfPermission(OrderTrackActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Log.i("OrderTrackActivity", "Requesting CALL_PHONE permission");
                    ActivityCompat.requestPermissions(OrderTrackActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    Log.i("OrderTrackActivity", "Starting call activity");
                    startActivity(intent1);
                }
            }
        });
    }

    private boolean hasLocationPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
    }

    private void loadDeliveryAddress(){

        TextView deliveryAddressTxt = findViewById(R.id.delivery_location);

        String lane1Txt =sharedPreferences.getString("lane1","");
        String lane2Txt = sharedPreferences.getString("lane2", "");
        String fullAddress = lane1Txt +" "+ lane2Txt;

        deliveryAddressTxt.setText(fullAddress);
        getLatLngFromAddress(fullAddress);

    }

    // Get current location
    private void getCurrentLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.i("Location", "Current Location: " + currentLatLng);

                    if (map != null) {
                        map.addMarker(new MarkerOptions()
                                .position(currentLatLng)
                                .title("Your Location")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                        drawPolyline(); // Draw the polyline if delivery location is already set
                    } else {
                        Log.e("Location", "Google Map is not initialized");
                    }
                } else {
                    Log.e("Location", "Failed to get current location");
                }
            }
        });
    }

    private void getLatLngFromAddress(String fullAddress) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(fullAddress, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                deliveryLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                Log.i("Geocoding", "Delivery LatLng: " + deliveryLatLng);

                if (map != null) {
                    map.addMarker(new MarkerOptions()
                            .position(deliveryLatLng)
                            .title("Delivery Address"));
                    drawPolyline(); // Draw the polyline if current location is already set
                } else {
                    Log.e("Geocoding", "Google Map is not initialized");
                }
            } else {
                Log.e("Geocoding", "No coordinates found for address: " + fullAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawPolyline() {
        if (currentLatLng != null && deliveryLatLng != null) {
            Log.i("Polyline", "Drawing polyline between: " + currentLatLng + " and " + deliveryLatLng);

            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(currentLatLng)
                    .add(deliveryLatLng)
                    .width(8)
                    .color(ContextCompat.getColor(this, R.color.red));

            map.addPolyline(polylineOptions);
        } else {
            Log.e("Polyline", "Cannot draw polyline, location data is missing. Current LatLng: " + currentLatLng + ", Delivery LatLng: " + deliveryLatLng);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Log.e("Permissions", "Location permissions denied");
            }
        }
    }


}