package lk.javainstitute.savoryhub.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import lk.javainstitute.savoryhub.R;

public class SignUpActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private boolean isSensorCovered= false;

    private HashMap<String,Object> userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_price), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if(proximitySensor==null){
            Toast.makeText(SignUpActivity.this, "Proximity Sensor Is Not Available", Toast.LENGTH_LONG).show();
            return;
        }

        Button signup_btn = findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText1 = findViewById(R.id.signUp_fname_text);
                EditText editText2 = findViewById(R.id.signUp_lname_text);
                EditText editText3 = findViewById(R.id.signUp_email_text);
                EditText editText4 = findViewById(R.id.signUp_mobile_text);
                EditText editText5 = findViewById(R.id.signUp_password_text);

                String fname = String.valueOf(editText1.getText());
                String lname = String.valueOf(editText2.getText());
                String email = String.valueOf(editText3.getText());
                String mobile = String.valueOf(editText4.getText());
                String password = String.valueOf(editText5.getText());

                if(fname.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Enter Your First Name", Toast.LENGTH_LONG).show();

                } else if (lname.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Your Last Name", Toast.LENGTH_LONG).show();

                }else if (mobile.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Your Mobile", Toast.LENGTH_LONG).show();

                } else if (!mobile.matches("^[0]{1}[7]{1}[01245678]{1}[0-9]{7}$")) {
                    Toast.makeText(SignUpActivity.this, "Enter Validate Mobile", Toast.LENGTH_LONG).show();

                }  else if (email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Your Email", Toast.LENGTH_LONG).show();

                } else if (password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Your Password", Toast.LENGTH_LONG).show();

                }else{

                    Snackbar snackbar = Snackbar.make(view, "Please cover the PROXIMITY SENSOR to complete Sign Up", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();

                    TextView snackbarTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    snackbarTextView.setTypeface(ResourcesCompat.getFont(SignUpActivity.this, R.font.poppins_medium)); // Replace 'your_custom_font' with the actual font resource
                    snackbarTextView.setTextSize(12); // Optional: change text size
                    snackbar.show();

                    //use map insert data in to db
                    userData = new HashMap<>();
                    userData.put("fname", fname);
                    userData.put("lname", lname);
                    userData.put("mobile", mobile);
                    userData.put("email", email);
                    userData.put("password", password);
                    userData.put("isAdmin", false);

                    handleSensorCover(userData);
                }
            }
        });
    }

    private void handleSensorCover(HashMap<String, Object> userData) {
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
       // saveUserInSharedPreferences(userData);
    }

    //save user into the firebase
    private void insertUserIntoFirebase(HashMap<String,Object> userData){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore
                .collection("user")
                .add(userData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.i("SavoryHubSignUp", "Data Added to the db");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("SavoryHubSignUp", "fail");
                    }
                }
        );
    }

    //Save to Shared Preferences user data
    private void saveUserInSharedPreferences(HashMap<String,Object> userData){
        SharedPreferences sharedPreferences = getSharedPreferences("lk.javainstitute.savoryhub.data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("fname", (String)userData.get("fname"));
        editor.putString("lname", (String)userData.get("lname"));
        editor.putString("mobile", (String)userData.get("mobile"));
        editor.putString("email", (String)userData.get("email"));
        editor.putString("password", (String)userData.get("password"));
        editor.putBoolean("isAdmin", (Boolean)userData.get("isAdmin"));
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY){
            isSensorCovered = sensorEvent.values[0]< proximitySensor.getMaximumRange();
            if(isSensorCovered){
                Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_LONG).show();
                sensorManager.unregisterListener(this);

                insertUserIntoFirebase(userData);

                //move to sign in
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}