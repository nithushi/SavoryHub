package lk.javainstitute.savoryhub.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.model.Mail;

public class SignInActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private boolean isActivityRunning;
    private TextView signUp_btn;
    private CheckBox passwordCheckBox;
    private EditText editTextMobile, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_price), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("lk.javainstitute.savoryhub.data", MODE_PRIVATE);

        editTextMobile = findViewById(R.id.signIn_usernameText);
        editTextPassword = findViewById(R.id.signIn_passwordText);
        passwordCheckBox = findViewById(R.id.showPasswordCheckBox);

        //check password visibility
        passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                editTextPassword.setSelection(editTextPassword.getText().length());
            }
        });

        Button signIn_loginBtn = findViewById(R.id.verify_email_btn);
        signIn_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobile = String.valueOf(editTextMobile.getText());
                String password = String.valueOf(editTextPassword.getText());

                if(mobile.isEmpty()){
                    Toast.makeText(SignInActivity.this ,"Enter Your Mobile Number", Toast.LENGTH_LONG).show();

                } else if (!mobile.matches("^[0]{1}[7]{1}[01245678]{1}[0-9]{7}$")) {
                    Toast.makeText(SignInActivity.this, "Enter Validate Mobile", Toast.LENGTH_LONG).show();

                }else if (password.isEmpty()) {
                    Toast.makeText(SignInActivity.this ,"Enter Your Password", Toast.LENGTH_LONG).show();
                }else{

                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore
                            .collection("user")
                            .where(
                                    Filter.and(
                                            Filter.equalTo("mobile", mobile),
                                            Filter.equalTo("password", password)
                                    )
                            )
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        QuerySnapshot documentSnapshots = task.getResult();
                                        if(documentSnapshots.size()==1){

                                            DocumentSnapshot document = documentSnapshots.getDocuments().get(0);
                                            String firstName = document.getString("fname");
                                            String lastName = document.getString("lname");
                                            Boolean isAdmin = document.getBoolean("isAdmin");

                                            //
                                            String email = document.getString("email");
                                            String mobile = document.getString("mobile");
                                            String password = document.getString("password");

                                            //store data in shared preferences
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("fname", firstName);
                                            editor.putString("lname",lastName);
                                            editor.putString("email", email);
                                            editor.putString("mobile", mobile);
                                            editor.putString("password", password);
                                            editor.putBoolean("isAdmin", isAdmin!=null && isAdmin);
                                            editor.putBoolean("isLoggedIn", true);
                                            editor.apply();

                                            //Toast.makeText(SignInActivity.this ,"Sign In Successfully!", Toast.LENGTH_LONG).show();

                                            Intent intent;
                                            if (isAdmin != null && isAdmin) {

                                                intent = new Intent(SignInActivity.this, AdminDashboardActivity.class);
                                                startActivity(intent);

//                                                String verificationCode = generateVerificationCode();
//                                                SharedPreferences.Editor codeEditor = sharedPreferences.edit();
//                                                codeEditor.putString("verificationCode", verificationCode);
//                                                codeEditor.apply();
//
//                                                Mail mail = new Mail();
//                                                mail.sendVerificationEmail(email, verificationCode);
//
//                                                Intent verifyIntent = new Intent(SignInActivity.this, VerifyEmailActivity.class);
//                                                verifyIntent.putExtra("email", email);
//                                                startActivity(verifyIntent);
//
//                                                Log.i("Redirect", "Redirecting to VerifyEmailActivity");

                                            } else {
                                                Log.i("Redirect", "Redirecting to HomeActivity");
                                                intent = new Intent(SignInActivity.this, HomeActivity.class);
                                                intent.putExtra("fname", firstName);
                                                intent.putExtra("lname", lastName);
                                                startActivity(intent);
                                            }

                                        }else {
                                            Toast.makeText(SignInActivity.this ,"Invalid Credentials..", Toast.LENGTH_LONG).show();
                                        }

                                    }else{
                                        Toast.makeText(SignInActivity.this, "Error Signing In. Please Try Again.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                    );
                }

//                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
//                startActivity(intent);
            }
        });

        signUp_btn = findViewById(R.id.signIn_signupBtn);
        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("DefaultLocale")
    private String generateVerificationCode(){
        Random random = new Random();
        int code = random.nextInt(999999);
        return String.format("%06d", code);
    }
}


