package com.example.chattingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chattingapp.R;
import com.example.chattingapp.models.User;
import com.example.chattingapp.utilities.Constants;
import com.example.chattingapp.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    ImageView back_btn;
    RecyclerView usersRecyclerView2;

    //Bu kısmı daha sonra dene profil görüntülemek için layoutta her bir görüntüleme için textview oluştur.
    private TextView nameTextView, bolumTextView,sinifTextView, egitimTextView, emailTextView;
    private TextView durumTextView,uzaklıkTextView,telTextView,sureTextView ;
    private RoundedImageView image;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        back_btn = findViewById(R.id.imageBackMain);
        image = findViewById(R.id.imageProfileUpdate);
        usersRecyclerView2 = findViewById(R.id.usersRecyclerView2);

        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getMyProfile();

        nameTextView = findViewById(R.id.nameTextView);
        egitimTextView = findViewById(R.id.egitimTextView);
        bolumTextView = findViewById(R.id.bolum_bilgi);
        sinifTextView = findViewById(R.id.sinif_bilgi);
        durumTextView = findViewById(R.id.durum);
        uzaklıkTextView = findViewById(R.id.uzaklık_bilgi);
        sureTextView = findViewById(R.id.sure_bilgi);
        emailTextView = findViewById(R.id.emailTextView);
        telTextView = findViewById(R.id.telTextView);


    }



    private void setListeners(){
        back_btn.setOnClickListener(v -> onBackPressed());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private void getMyProfile() {
        FirebaseFirestore database =  FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {


                                String name = queryDocumentSnapshot.getString("name");
                                String egitim = queryDocumentSnapshot.getString("egitim_durumu");
                                String email = queryDocumentSnapshot.getString("email");
                                String bolum = queryDocumentSnapshot.getString("bölüm");
                                String sinif = queryDocumentSnapshot.getString("sınıf");
                                String durum = queryDocumentSnapshot.getString("status");
                                String uzaklik = queryDocumentSnapshot.getString("kampuse_uzaklık");
                                String sure = queryDocumentSnapshot.getString("kalma_suresi");
                                String tel = queryDocumentSnapshot.getString("telefon");
                                String encodedImage = queryDocumentSnapshot.getString("image");
                                image.setImageBitmap(getUserImage(encodedImage));



                                nameTextView.setText("İsim: " + name);
                                egitimTextView.setText("Eğitim Durumu: "+egitim);
                                emailTextView.setText("Email: "+email);
                                bolumTextView.setText("Bölüm: "+bolum);
                                sinifTextView.setText("Sınıf: "+sinif);
                                durumTextView.setText("Durum: "+durum);
                                uzaklıkTextView.setText("Evin Kampüse Uzaklığı: "+uzaklik);
                                sureTextView.setText("Evde Kalma/Paylaşma Süresi: "+sure);
                                telTextView.setText("Telefon: "+tel);



                            }

                        }
                    }else {
                        Toast.makeText(Profile.this, "Profil yüklenemedi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }




}