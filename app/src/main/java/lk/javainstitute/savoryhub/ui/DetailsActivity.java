package lk.javainstitute.savoryhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.helper.ManagmentCart;
import lk.javainstitute.savoryhub.model.Foods;

public class DetailsActivity extends AppCompatActivity {

    private Foods object;
    private int num = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_price), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getIntentExtra();
        setVariable();

    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        object = (Foods) intent.getSerializableExtra("object");

        if (object != null) {
            Log.d("DetailsActivity", "Received object: " + object.getTitle());
        } else {
            Log.e("DetailsActivity", "Failed to receive object");
        }
    }

    private void setVariable() {
        Log.d("DetailsActivity", "setVariable started");

        managmentCart = new ManagmentCart(this);

        ImageView detail_img = findViewById(R.id.detail_img);
        TextView detail_price = findViewById(R.id.detail_price_txt);
        TextView detail_title = findViewById(R.id.detail_title);
        TextView detail_description = findViewById(R.id.detail_description);
        TextView detail_rate = findViewById(R.id.detail_rate);
        TextView detail_time = findViewById(R.id.detail_time);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView detail_total = findViewById(R.id.detail_total);

        //Quantity Update
        TextView detail_plus_btn = findViewById(R.id.detail_plus_btn);
        TextView detail_minus_btn = findViewById(R.id.detail_minus_btn);
        TextView detail_num_txt = findViewById(R.id.detail_num_txt);

        //add to cart btn
        Button detail_addToCartBtn = findViewById(R.id.detail_addToCartBtn);

        if (object != null) {
            // Uncomment and use Glide to load image if needed
            int imageResource = getResources().getIdentifier(object.getImagePath(), "drawable", getPackageName());

            Glide.with(this)
                    .load(imageResource)
                    .into(detail_img);

            detail_price.setText("$" + String.valueOf(object.getPrice()));
            detail_title.setText(object.getTitle());
            detail_description.setText(object.getDescription());
            detail_rate.setText(String.valueOf(object.getStar()) + " Rating");
            ratingBar.setRating((float) object.getStar());
            detail_time.setText(String.valueOf(object.getTimeValue()) + " min");
            detail_total.setText("$" + String.valueOf(num * object.getPrice()));
        } else {
            Log.e("DetailsActivity", "Object is null");
        }
        Log.d("DetailsActivity", "setVariable finished");

        //plus qty
        detail_plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=num+1;
                detail_num_txt.setText(num+"");
                detail_total.setText(("$"+ num* object.getPrice()) +" ");
            }
        });

        //minus qty
        detail_minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num>1){
                    num=num-1;
                    detail_num_txt.setText(num+"");
                    detail_total.setText(("$"+ num* object.getPrice()) +" ");
                }
            }
        });

        //add to cart
        detail_addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.getNumberInCart();
                managmentCart.insertFood(object);
            }
        });

    }
}

