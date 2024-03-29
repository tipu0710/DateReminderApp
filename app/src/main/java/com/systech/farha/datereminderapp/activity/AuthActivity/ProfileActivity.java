package com.systech.farha.datereminderapp.activity.AuthActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.activity.Others.MainActivity;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import com.systech.farha.datereminderapp.model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.systech.farha.datereminderapp.activity.Others.MainActivity.REG_PREFS_NAME;

public class ProfileActivity extends AppCompatActivity {
    private ImageButton camera, gallery;
    private CircleImageView profilePic;
    private Button register;
    private EditText phoneEt, addressEt;
    private ProgressBar progressBar;
    private Sprite doubleBounce;
    private byte[] imageByte;
    DatabaseHelper databaseHelper;
    SessionManager session;

    String phone, address;

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
        progressBar = findViewById(R.id.progress);
        doubleBounce = new Wave();


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
                address = addressEt.getText().toString();
                phone = phoneEt.getText().toString();
                if (address.isEmpty()){
                    address = "";
                }
                if (phone.isEmpty()){
                    phoneEt.setError("Enter phone number");
                }else {
                    new LoadData().execute();
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

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        byte[] size = outputStream.toByteArray();
        float length = (float) ((float) size.length/(1024.0*2));
        Log.v("CheckSize", length+"");

//        if (length>2048){
//            Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
//            byte[] newByte = getBitmapAsByteArray(finalBitmap);
//            return newByte;
//        }else {
//            return size;
//        }
        return size;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }

    private class LoadData extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap bitmap = ((BitmapDrawable)profilePic.getDrawable()).getBitmap();
            imageByte = getBitmapAsByteArray(getResizedBitmap(bitmap,500));
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminateDrawable(doubleBounce);
            register.setClickable(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            SharedPreferences prefs = getSharedPreferences(REG_PREFS_NAME, MODE_PRIVATE);
            User user = new User();
            user.setName(prefs.getString("name", null));
            user.setUserName(prefs.getString("userName", null));
            user.setEmail(prefs.getString("email",null));
            user.setPassword(prefs.getString("password", null));
            user.setQuestion1(prefs.getString("ques1", null));
            user.setQuestion2(prefs.getString("ques2",null));
            user.setAnswer1(prefs.getString("ans1", null));
            user.setAnswer2(prefs.getString("ans2", null));
            user.setQuestionSkipped(prefs.getBoolean("isQuestionSkipped", false));
            user.setPhone(phone);
            user.setAddress(address);
            user.setProfile(imageByte);

            databaseHelper.addUser(user);

            if (databaseHelper.checkUserName(user.getUserName())) {
                int userId = databaseHelper.getUserIdByUserName(user.getUserName());

                session.storeLoginSession(String.valueOf(userId), user.getUserName());
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                getSharedPreferences(REG_PREFS_NAME, MODE_PRIVATE).edit().clear().apply();

                register.setClickable(true);
                startActivity(i);
                finish();
            }
        }
    }
}
