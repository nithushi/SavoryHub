package lk.javainstitute.savoryhub.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.model.Orders;

public class CheckoutActivity extends AppCompatActivity {

    private TextView itemTotalTxt,deliveryTxt,trackIdTxt,dateTxt;
    private Button totalTxt, orderTrackBtn;
    private int trackId;
    private SharedPreferences sharedPreferences;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        double itemTotal = intent.getDoubleExtra("itemTotal", 0.0);
        double delivery = intent.getDoubleExtra("delivery", 0.0);
        double total = intent.getDoubleExtra("total", 0.0);
        trackId = intent.getIntExtra("trackId", 0);
        String date = intent.getStringExtra("date");

        itemTotalTxt = findViewById(R.id.checkout_itemTotal);
        deliveryTxt = findViewById(R.id.checkout_delivery);
        trackIdTxt = findViewById(R.id.checkout_trackId);
        dateTxt = findViewById(R.id.checkout_date);
        totalTxt = findViewById(R.id.checkout_total);

        itemTotalTxt.setText("$" + itemTotal);
        deliveryTxt.setText("$" + delivery);
        trackIdTxt.setText("#"+ trackId);
        dateTxt.setText(date);
        totalTxt.setText("Total : $" + total);

        loggedUserEmail();

        if (email != null && !email.isEmpty()) {
            int status_Id =3;
            Orders order = new Orders(trackId, itemTotal, delivery, total, email, date, status_Id);
            addOrderInToDb(order);
        } else {
            Toast.makeText(CheckoutActivity.this,"Please Login First", Toast.LENGTH_LONG).show();
        }
        trackOrder();

    }

    private void loggedUserEmail() {
        sharedPreferences = getSharedPreferences("lk.javainstitute.savoryhub.data",MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        Log.i("email", email);
    }

    private void addOrderInToDb(Orders orders) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore
                .collection("Orders")
                .add(orders)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(CheckoutActivity.this,"Order Added Successfully!", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    private void trackOrder() {
        orderTrackBtn= findViewById(R.id.order_track_btn);

        Animation shakeAnim = AnimationUtils.loadAnimation(CheckoutActivity.this, R.anim.fade_anim);
        orderTrackBtn.startAnimation(shakeAnim);

        orderTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(CheckoutActivity.this, OrderTrackActivity.class);
                intent.putExtra("orderId", trackId);
                startActivity(intent);
            }
        });
    }
}