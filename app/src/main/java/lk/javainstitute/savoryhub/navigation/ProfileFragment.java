package lk.javainstitute.savoryhub.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.ui.UserProfileActivity;

public class ProfileFragment extends Fragment {


    private SharedPreferences sharedPreferences;
    private TextView fnameTxt,lnameTxt,mobileTxt,emailTxt;
    private ImageView pic;
    Button changeBtn, deleteBtn;
    private static final String IMAGE_KEY = "AdminProfileImage";
    private Uri selectedImageUri;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);

        Context context= getContext();
        if(context!=null){
            sharedPreferences = context.getSharedPreferences("lk.javainstitute.savoryhub.data", Context.MODE_PRIVATE);
        }

        deleteBtn = rootView.findViewById(R.id.admin_delete_pic);
        changeBtn = rootView.findViewById(R.id.admin_change_pic);
        fnameTxt = rootView.findViewById(R.id.admin_profile_fname);
        lnameTxt = rootView.findViewById(R.id.admin_profile_lname);
        mobileTxt = rootView.findViewById(R.id.admin_profile_mobile);
        emailTxt = rootView.findViewById(R.id.admin_profile_email);
        pic = rootView.findViewById(R.id.admin_profile_pic);

        loadProfileData();

         //Set up the image picker
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    selectedImageUri = data.getData();
                    pic.setImageURI(selectedImageUri);
                }
            }
        });

        // Handle button click to pick and save image
        pic.setOnClickListener(v -> pickImageFromGallery());

        changeBtn.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                saveImageUriToSharedPreferences(selectedImageUri);
                Toast.makeText(getContext(), "Profile image updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(v -> {
            deleteImageUriFromSharedPreferences();
            // Set to default profile image
            pic.setImageResource(R.drawable.admin_profile);
            Toast.makeText(getContext(), "Profile image deleted!", Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

    private void deleteImageUriFromSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(IMAGE_KEY);
        editor.apply();
    }

    private void loadProfileData() {
        String fname =sharedPreferences.getString("fname", null);
        String lname =sharedPreferences.getString("lname", null);
        String mobile =sharedPreferences.getString("mobile", null);
        String email =sharedPreferences.getString("email", null);
        String encodedImg =sharedPreferences.getString("AdminProfileImage", null);

        fnameTxt.setText(fname);
        lnameTxt.setText(lname);
        mobileTxt.setText(mobile);
        emailTxt.setText(email);

        if (encodedImg != null) {
            byte[] decodedBytes = Base64.decode(encodedImg, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            pic.setImageBitmap(bitmap);
        }
    }


    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void saveImageUriToSharedPreferences(Uri imageUri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(IMAGE_KEY, encodedImage);
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

}