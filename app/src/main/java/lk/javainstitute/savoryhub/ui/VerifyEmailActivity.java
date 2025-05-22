package lk.javainstitute.savoryhub.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import lk.javainstitute.savoryhub.R;

public class VerifyEmailActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Show Snackbar message when VerifyEmailActivity starts
        Snackbar.make(findViewById(android.R.id.content),
                        "Verification code sent to your email.",
                        Snackbar.LENGTH_LONG)
                .setAction("Dismiss", view -> {})
                .show();

        sharedPreferences = getSharedPreferences("lk.javainstitute.savoryhub.data", MODE_PRIVATE);

        Button verifyBtn = findViewById(R.id.verify_email_btn);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText= findViewById(R.id.verification_code);
                String verification_code= editText.getText().toString();

                String storedCode = sharedPreferences.getString("verificationCode", "");
                if (verification_code.equals(storedCode)) {
                    Toast.makeText(VerifyEmailActivity.this, "Email Verified Successfully!", Toast.LENGTH_LONG).show();

                    // Proceed to admin dashboard or next step
                     Intent intent = new Intent(VerifyEmailActivity.this, AdminDashboardActivity.class);
                     startActivity(intent);
                     finish();
                } else {
                    Toast.makeText(VerifyEmailActivity.this, "Invalid Verification Code", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}