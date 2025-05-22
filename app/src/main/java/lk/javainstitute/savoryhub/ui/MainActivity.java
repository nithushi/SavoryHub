package lk.javainstitute.savoryhub.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import lk.javainstitute.savoryhub.R;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_price), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("lk.javainstitute.savoryhub.data",MODE_PRIVATE);
        checkSignedInStatus();


        //Move to SignIn
        TextView intro_login_btn = findViewById(R.id.intro_login_btn);
        intro_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        //Move to SignUp
        TextView intro_register_btn = findViewById(R.id.intro_register_btn);
        intro_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkSignedInStatus() {
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);

        String fname = sharedPreferences.getString("fname",null);
        String lname = sharedPreferences.getString("lname",null);

//        if(isLoggedIn){
//            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//
//            intent.putExtra("fname",fname);
//            intent.putExtra("lname",lname);
//
//            startActivity(intent);
//            finish();
//
//        }else{
//            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
//            startActivity(intent);
//            finish();
//        }

        if(isLoggedIn){

            if(isAdmin){
                Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);

                //intent.putExtra("fname",fname);
                //intent.putExtra("lname",lname);

                startActivity(intent);
                finish();
            }

            Intent intent1 = new Intent(MainActivity.this, HomeActivity.class);

            intent1.putExtra("fname",fname);
            intent1.putExtra("lname",lname);

            startActivity(intent1);
            finish();

        }else{
//            Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
//            startActivity(intent2);
//            finish();
        }
    }
}