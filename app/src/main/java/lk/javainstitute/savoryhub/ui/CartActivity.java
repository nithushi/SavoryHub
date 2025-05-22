package lk.javainstitute.savoryhub.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.adapters.CartAdapter;
import lk.javainstitute.savoryhub.databinding.ActivityCartBinding;
import lk.javainstitute.savoryhub.helper.ChangeNumberItemsListener;
import lk.javainstitute.savoryhub.helper.ManagmentCart;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;

    private double total;
    private double itemTotal;
    private double delivery;

    private int trackId;
    private String date;
    private TextView cartAddressTxt;
    private SharedPreferences sharedPreferences;

    private static final int PAYHERE_REQUEST = 11010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_price), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("lk.javainstitute.savoryhub.data",MODE_PRIVATE);

        managmentCart = new ManagmentCart(this);
        checkLocation();
        setVariable();
        calculateCart();
        loadList();

        Button checkout_btn = findViewById(R.id.checkout_btn);
        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocation();
                checkout();
            }
        });

        //create random number
        Random random = new Random();
        trackId = random.nextInt(999999);

        //get date and time
        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(date1);

    }

    private void loadList() {
        TextView cart_empty_txt = findViewById(R.id.cart_empty_txt);
        ScrollView cart_scrol_view = findViewById(R.id.cart_scroll_view);
        RecyclerView cart_card_recycleView = findViewById(R.id.cart_card_recycleView);

        if(managmentCart.getListCart().isEmpty()){
            cart_empty_txt.setVisibility(View.VISIBLE);
            cart_scrol_view.setVisibility(View.GONE);
        }else{
            cart_empty_txt.setVisibility(View.GONE);
            cart_scrol_view.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this,LinearLayoutManager.VERTICAL, false);
        cart_card_recycleView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                calculateCart();
            }
        });

        cart_card_recycleView.setAdapter(adapter);

    }

    private void calculateCart() {

        TextView cart_subtotal_txt = findViewById(R.id.cart_subtotal_txt);
        TextView cart_delivery_txt = findViewById(R.id.cart_delivery_txt);
        TextView cart_total_txt = findViewById(R.id.cart_total_txt);

         delivery = 10;
         total = Math.round((managmentCart.getTotalFee()+delivery)*100/100);
         itemTotal = Math.round(managmentCart.getTotalFee()*100)/100;

        cart_subtotal_txt.setText("$"+ itemTotal);
        cart_delivery_txt.setText("$"+ delivery);
        cart_total_txt.setText("$"+ total);

    }

    private void checkout() {

        cartAddressTxt = findViewById(R.id.cartAddress);

        String lane1Txt = sharedPreferences.getString("lane1", "");
        String lane2Txt = sharedPreferences.getString("lane2", "");

        if (lane1Txt.isEmpty() && lane2Txt.isEmpty()) {
            // Show alert if address is missing
            new AlertDialog.Builder(this)
                    .setTitle("Incomplete Information")
                    .setMessage("Please complete your home address before proceeding to checkout.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        // Navigate to UserProfileActivity
                        Intent intent = new Intent(CartActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                    })
                    .show();
            return;
        }

        try {
            InitRequest req = new InitRequest();
            req.setMerchantId("1229501"); // Replace with your actual Merchant ID
            req.setMerchantSecret("MjIyODU4OTE4OTM1MjE1NDA2Mjc0MTQ2NDA0NDg4MTYyODQ2NDczMA=="); // Replace with your actual Merchant Secret
            req.setCurrency("LKR");
            req.setAmount(1000.00);
            req.setOrderId("230000123");
            req.setItemsDescription("Door bell wireless");
            req.setCustom1("This is the custom message 1");
            req.setCustom2("This is the custom message 2");
            req.getCustomer().setFirstName("Saman");
            req.getCustomer().setLastName("Perera");
            req.getCustomer().setEmail("samanp@gmail.com");
            req.getCustomer().setPhone("+94771234567");
            req.getCustomer().getAddress().setAddress("No.1, Galle Road");
            req.getCustomer().getAddress().setCity("Colombo");
            req.getCustomer().getAddress().setCountry("Sri Lanka");

            //Log.d("PayHere", "Merchant ID: " + req.getMerchantId());
            //Log.d("PayHere", "Request details: " + req.toString());

            PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
            //Log.d("PayHere", "Setting base URL to: " + PHConfigs.SANDBOX_URL);

            Intent intent = new Intent(CartActivity.this, PHMainActivity.class);
            intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
            Log.d("PayHere", "Starting PaymentActivity");

            startActivityForResult(intent, PAYHERE_REQUEST);

        } catch (Exception e) {
            Log.e("PayHere", "Error initiating payment: " + e.getMessage(), e);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("PayHere", "Request code: " + requestCode + ", result code: " + resultCode);

        if (requestCode == PAYHERE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
                    PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
                    if (response != null) {
                        if (response.isSuccess()) {
                            Log.i("PayHere", "Payment successful: " + response.getData().toString());
                            Intent intent = new Intent(CartActivity.this,CheckoutActivity.class);
                            intent.putExtra("itemTotal", itemTotal);
                            intent.putExtra("delivery", delivery);
                            intent.putExtra("total", total);
                            intent.putExtra("trackId", trackId);
                            intent.putExtra("date", date);
                            startActivity(intent);
                        } else {
                            Log.e("PayHere", "Payment failed: " + response.toString());
                        }
                    } else {
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("PayHere", "Payment canceled by user");
                if (data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
                    PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
                }
            } else {
                Log.e("PayHere", "Unhandled result code: " + resultCode);
            }

            if (data != null && data.hasExtra("error_message")) {
                String errorMessage = data.getStringExtra("error_message");
                Log.e("PayHere", "Error message: " + errorMessage);
            }
        }
    }

    private void checkLocation(){

        cartAddressTxt = findViewById(R.id.cartAddress);

        String lane1Txt = sharedPreferences.getString("lane1","");
        String lane2Txt =sharedPreferences.getString("lane2", "");

        if(!lane1Txt.isEmpty() || !lane2Txt.isEmpty()){
            cartAddressTxt.setText(lane1Txt +" "+ lane2Txt);
        }else{
            Toast.makeText(CartActivity.this, "Home Address Is Required", Toast.LENGTH_LONG).show();

        }
    }

    private void setVariable() {

    }


}