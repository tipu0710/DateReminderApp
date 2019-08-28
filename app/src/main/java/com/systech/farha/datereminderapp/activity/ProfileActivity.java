package com.systech.farha.datereminderapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import com.systech.farha.datereminderapp.model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.systech.farha.datereminderapp.activity.MainActivity.REG_PREFS_NAME;

public class ProfileActivity extends AppCompatActivity {
    private ImageButton camera, gallery;
    private CircleImageView profilePic;
    private Button register;
    private EditText phoneEt, addressEt;
    DatabaseHelper databaseHelper;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        camera = findViewById(R.id.camera);
        gallery = findViewById(R.id.gallery);
        register = findViewById(R.id.register_);
        profilePic = findViewById(R.id.profile_image);
        phoneEt = findViewById(R.id.user_phone);
        addressEt = findViewById(R.id.user_address);


        session = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = addressEt.getText().toString();
                String phone = phoneEt.getText().toString();
                if (address.isEmpty()){
                    address = "";
                }
                if (phone.isEmpty()){
                    phoneEt.setError("Enter phone number");
                }else {
                    Bitmap bitmap = ((BitmapDrawable)profilePic.getDrawable()).getBitmap();
                    byte[] imageByte = getBitmapAsByteArray(bitmap);
                    SharedPreferences prefs = getSharedPreferences(REG_PREFS_NAME, MODE_PRIVATE);
                    User user = new User();
                    user.setName(prefs.getString("name", null));
                    user.setEmail(prefs.getString("email",null));
                    user.setPassword(prefs.getString("password", null));
                    user.setQuestion1(prefs.getString("ques1", null));
                    user.setQuestion2(prefs.getString("ques2",null));
                    user.setAnswer1(prefs.getString("ans1", null));
                    user.setAnswer2(prefs.getString("ans2", null));
                    user.setPhone(phone);
                    user.setAddress(address);
                    user.setProfile(imageByte);

                    databaseHelper.addUser(user);

                    if (databaseHelper.checkUser(user.getEmail())) {
                        Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_LONG).show();

                        int userId = databaseHelper.getUserIdByEmail(user.getEmail());

                        session.storeLoginSession(String.valueOf(userId), user.getEmail());
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        getSharedPreferences(REG_PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
                        startActivity(i);
                        finish();
                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 0:
                    assert data != null;
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profilePic.setImageBitmap(bitmap);
                    break;
                case 1:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        profilePic.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    Toast.makeText(ProfileActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }
}
