package lk.javainstitute.savoryhub.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lk.javainstitute.savoryhub.R;

public class UserProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profilePic, userLogoutBtn;
    private TextView fnameTxt,lnameTxt,emailTxt,mobileTxt,passwordTxt, userFullName, userEmail, lettersTxt;
    private SharedPreferences sharedPreferences;
    private Button editProfileBtn;
    private Uri selectedImageUri;
    private EditText userLane1, userLane2;
    private CheckBox profileCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_price), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        profilePic = findViewById(R.id.user_profile_pic);
        fnameTxt = findViewById(R.id.user_txt_1);
        lnameTxt = findViewById(R.id.user_txt_2);
        emailTxt = findViewById(R.id.user_txt_3);
        mobileTxt = findViewById(R.id.user_txt_4);
        passwordTxt = findViewById(R.id.user_txt_5);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        profileCheckBox = findViewById(R.id.profileCheckBox);
        lettersTxt = findViewById(R.id.letters_txt);

        //home address
        userLane1 = findViewById(R.id.profile_lane1);
        userLane2 = findViewById(R.id.profile_lane2);

        //logout btn
        userLogoutBtn = findViewById(R.id.user_logout_btn1);

        //view name, email
        userFullName = findViewById(R.id.user_fullname_txt);
        userEmail = findViewById(R.id.user_email);

        //get shared preferences
        sharedPreferences=getSharedPreferences("lk.javainstitute.savoryhub.data", MODE_PRIVATE);
        loadUserData();

        //view password
        profileCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    passwordTxt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    passwordTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        //user logout
        userLogoutBtn.setOnClickListener(view -> userLogout());

        profilePic.setOnClickListener(view -> openImagePicker());
        //editProfileBtn.setOnClickListener(view -> saveImageToSharedPreference());

        //save address
        editProfileBtn.setOnClickListener(v -> {
            String lane1 = userLane1.getText().toString().trim();
            String lane2 = userLane2.getText().toString().trim();

            if (!lane1.isEmpty() && !lane2.isEmpty()) {
                saveAddressInSP(lane1,lane2);  // Save the address
            } else {
                Toast.makeText(this, "Please enter a valid address", Toast.LENGTH_SHORT).show();
            }
        });

        userLane1.setText(sharedPreferences.getString("lane1", ""));
        userLane2.setText(sharedPreferences.getString("lane2", ""));

    }

    //SAVE ADDRESS
    private void saveAddressInSP(String lane1, String lane2) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lane1", lane1);
        editor.putString("lane2", lane2);
        editor.apply();

        Toast.makeText(UserProfileActivity.this, "Address saved successfully", Toast.LENGTH_SHORT).show();
    }

    //load user data
    private void loadUserData(){
        String firstName = sharedPreferences.getString("fname", null);
        String lastName = sharedPreferences.getString("lname", null);
        String email = sharedPreferences.getString("email", null);
        String mobile = sharedPreferences.getString("mobile", null);
        String password = sharedPreferences.getString("password", null);
        String profilePicUri = sharedPreferences.getString("ProfileImage", null);

        char fLetter = firstName.charAt(0);
        char lLetter = lastName.charAt(0);

        String fLetterTxt = String.valueOf(fLetter);
        String lLetterTxt = String.valueOf(lLetter);

        fnameTxt.setText(firstName);
        lnameTxt.setText(lastName);
        emailTxt.setText(email);
        mobileTxt.setText(mobile);
        passwordTxt.setText(password);

        lettersTxt.setText(fLetterTxt +""+ lLetterTxt);
        userFullName.setText(firstName +" "+lastName);
        userEmail.setText(email);
//
//        if(profilePicUri !=null){
//            Bitmap bitmap = decodeBase64ToImage(profilePicUri);
//            //profilePic.setImageBitmap(bitmap);
//
//            String bitmapUri = getImageUri(this, bitmap).toString();
//            // Use Glide to load the rounded image
//            Glide.with(this)
//                    .load(bitmapUri)
//                    .transform(new CircleCrop())
//                    .into(profilePic);
//
//        }
    }

    private void userLogout() {
        // Clear the sign-in status in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);

        editor.remove( "lane1");
        editor.remove("lane2");

        editor.apply();

        // Navigate to SignInActivity
        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                profilePic.setImageBitmap(bitmap);

                Glide.with(this)
                        .load(selectedImageUri)
                        .transform(new CenterCrop())
                        .into(profilePic);

                saveImageToSharedPreference();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToSharedPreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (profilePic.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
            String imageEncoded = encodeImageToBase64(bitmap);
            editor.putString("ProfileImage", imageEncoded);
            editor.apply();
            Toast.makeText(this, "Image saved successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap decodeBase64ToImage(String profilePicUri) {
        byte[] decodedBytes = Base64.decode(profilePicUri, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private Object getImageUri(UserProfileActivity userProfileActivity, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(userProfileActivity.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

}