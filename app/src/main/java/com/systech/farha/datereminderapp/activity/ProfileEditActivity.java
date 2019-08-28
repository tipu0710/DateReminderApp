package com.systech.farha.datereminderapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {
    private ImageButton camera, gallery;
    private CircleImageView profilePic;
    private Button update, securityQues, changePassBtn;
    private EditText nameEt, phoneEt, addressEt;
    HashMap<String, String> user;
    Integer userId;
    DatabaseHelper databaseHelper;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        camera = findViewById(R.id.edit_camera);
        gallery = findViewById(R.id.edit_gallery);
        update = findViewById(R.id.edit_register_);
        profilePic = findViewById(R.id.profile_edit_image);
        nameEt = findViewById(R.id.edit_name);
        phoneEt = findViewById(R.id.edit_phone);
        addressEt = findViewById(R.id.edit_address);
        securityQues = findViewById(R.id.edit_security_question);
        changePassBtn = findViewById(R.id.edit_change_pass);


        session = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());
        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));

        final User userData = databaseHelper.getUserById(userId);
        Bitmap bitmap = BitmapFactory.decodeByteArray(userData.getProfile(), 0, userData.getProfile().length);
        profilePic.setImageBitmap(bitmap);
        nameEt.setText(userData.getName());
        phoneEt.setText(userData.getPhone());
        addressEt.setText(userData.getAddress());

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileEditActivity.this, ChangePasswordActivity.class));
            }
        });

        securityQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeQuestionsDialog();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEt.getText().toString();
                String phone = phoneEt.getText().toString();
                String address = addressEt.getText().toString();
                if (name.isEmpty()){
                    nameEt.setError("This field is required!");
                }else if (phone.isEmpty()){
                    phoneEt.setError("This field is required");
                }else {
                    if (address.isEmpty()){
                        address = "";
                    }
                    userData.setName(name);
                    userData.setPhone(phone);
                    userData.setAddress(address);
                    Bitmap bitmap = ((BitmapDrawable)profilePic.getDrawable()).getBitmap();
                    userData.setProfile(ProfileActivity.getBitmapAsByteArray(bitmap));
                    boolean b = databaseHelper.updateUser(userData);
                    if (b){
                        startActivity(new Intent(ProfileEditActivity.this, MainActivity.class));
                        Toast.makeText(ProfileEditActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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
    }

    private void changeQuestionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.security_dialog, null);
        final EditText passEt = view.findViewById(R.id.dialog_pass);
        Button checkButton = view.findViewById(R.id.check_pass_btn);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = passEt.getText().toString();
                if (pass.isEmpty()){
                    passEt.setError("Enter your password");
                }else {
                    User user = databaseHelper.getUserById(userId);
                    if (user.getPassword().equals(pass)){
                        Intent intent = new Intent(ProfileEditActivity.this, QuestionActivity.class);
                        intent.putExtra("id", userId);
                        intent.putExtra("fromEdit", true);
                        startActivity(intent);
                    }else {
                        Toast.makeText(ProfileEditActivity.this, "Password incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 0:
                if (data!=null){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profilePic.setImageBitmap(bitmap);
                }
                break;
            case 1:
                try {
                    if (data!=null){
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        profilePic.setImageBitmap(selectedImage);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileEditActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Toast.makeText(ProfileEditActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileEditActivity.this, MainActivity.class));
        finish();
    }
}
