package com.example.chattingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chattingapp.R;
import com.example.chattingapp.databinding.ActivityUserProfileBinding;
import com.example.chattingapp.listeners.UserListener;
import com.example.chattingapp.models.Request;
import com.example.chattingapp.models.User;
import com.example.chattingapp.utilities.Constants;
import com.example.chattingapp.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;


public class UserProfile extends AppCompatActivity implements UserListener {

    private ActivityUserProfileBinding binding;

    private User receiverUser;
    private MainActivity mactivity;
    private View layout2, layout1;
    String userEmail;
    private FirebaseFirestore firestore;

    private PreferenceManager preferenceManager;
    ImageView back_btn;
    RecyclerView usersRecyclerView2;


    //Bu kısmı daha sonra dene profil görüntülemek için layoutta her bir görüntüleme için textview oluştur.
    private TextView nameTextView, bolumTextView, sinifTextView, egitimTextView, emailTextView;
    private TextView durumTextView, uzaklıkTextView, telTextView, sureTextView;

    private String userId_k;

    private Button requestButton, fabNewStudent;
    private RoundedImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        preferenceManager = new PreferenceManager(getApplicationContext());
        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
        //back_btn = findViewById(R.id.imageBackMain);
        loadReceiverDetails();


        //setContentView(R.layout.activity_user_profile);

        firestore = FirebaseFirestore.getInstance();

        getUserProfile(currentUserId);


        image = findViewById(R.id.imageProfileUpdate2);
        usersRecyclerView2 = findViewById(R.id.usersRecyclerView2);
        layout2 = findViewById(R.id.layout2);
        layout1 = findViewById(R.id.layout1);



        setListeners();


        nameTextView = findViewById(R.id.nameTextView2);
        requestButton = findViewById(R.id.requestButton);
        fabNewStudent = findViewById(R.id.fabNewStudent);
        egitimTextView = findViewById(R.id.egitimTextView2);
        bolumTextView = findViewById(R.id.bolum_bilgi);
        sinifTextView = findViewById(R.id.sinif_bilgi);
        durumTextView = findViewById(R.id.durum);
        uzaklıkTextView = findViewById(R.id.uzaklık_bilgi);
        sureTextView = findViewById(R.id.sure_bilgi);
        emailTextView = findViewById(R.id.emailTextView2);
        telTextView = findViewById(R.id.telTextView2);


    }


    private void setListeners() {
        binding.imageBackMain.setOnClickListener(v -> onBackPressed());

        // binding.requestButton.setOnClickListener(v ->matching());


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, UsersActivity.class);
        startActivity(i);
        finish();
    }


    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        //binding.nameTextView2.setText(receiverUser.name);
        userEmail = receiverUser.email;


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserEmail = preferenceManager.getString(Constants.KEY_EMAIL);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (userEmail.equals(queryDocumentSnapshot.getString("email"))) {


                                userId_k = queryDocumentSnapshot.getString(Constants.KEY_USER_ID);

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
                                egitimTextView.setText("Eğitim Durumu: " + egitim);
                                emailTextView.setText("Email: " + email);
                                bolumTextView.setText("Bölüm: " + bolum);
                                sinifTextView.setText("Sınıf: " + sinif);
                                durumTextView.setText("Durum: " + durum);
                                uzaklıkTextView.setText("Şehir: " + uzaklik);
                                sureTextView.setText("Ülke: " + sure);
                                telTextView.setText("Telefon: " + tel);


                            }

                        }
                    } else {
                        Toast.makeText(UserProfile.this, "Profil yüklenemedi", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public void onUserClicked(User user) {

    }


    private void getUserProfile(String currentUserId) {
        // Kullanıcının profil bilgilerini Firestore'dan alın
        // Alınan verileri kullanarak bileşenlere yerleştirme işlemleri

        Button requestButton = findViewById(R.id.requestButton);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMatchingRequest(currentUserId);
            }
        });
    }

    private void sendMatchingRequest(String currentUserId) {
        // Eşleşme talebi gönderme işlemleri
        // Firestore'da belirli bir koleksiyona öğrenciye ait bir belge oluşturma
        // Talep mesajını ve gerekli bilgileri belgeye ekleyin


        String currentUserName = preferenceManager.getString(Constants.KEY_NAME);

        firestore.collection("users")
                .document(userId_k)
                .collection("requests")
                .add(new Request(currentUserName + " sizinle eşleşmek istiyor."))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // İşlem başarılı oldu
                        Toast.makeText(UserProfile.this, "Eşleşme talebi gönderildi.", Toast.LENGTH_SHORT).show();


                        sendNotificationToCurrentUser();
                        sendMatchRequestNotificationToUser(userId_k,currentUserName,currentUserId);


                        //showPopupMessage();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // İşlem başarısız oldu
                        Toast.makeText(UserProfile.this, "Eşleşme talebi gönderilirken hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private static final String CHANNEL_ID = "my_channel_id";

    private void createNotificationChannel() {
        // Sadece Android Oreo (API seviye 26) ve üstü için bildirim kanalı oluşturma
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Bildirim Kanalı Adı";
            String channelDescription = "Bildirim Kanalı Açıklaması";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            // Bildirim yöneticisine kanalı ekleme
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void showPopupMessage2(String recipientUserId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        Query query = usersRef.whereEqualTo("userId", recipientUserId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        String email = document.getString("email");
                        String phoneNumber = document.getString("telefon");

                        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
                        builder.setTitle("İletişim Bilgileri");
                        builder.setMessage("E-posta: " + email + "\nTelefon Numarası: " + phoneNumber);
                        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Tamam butonuna tıklanınca yapılacak işlemler
                                Toast.makeText(UserProfile.this, "İletişim bilgileri gösterildi", Toast.LENGTH_SHORT).show();
                                dialog.dismiss(); // Popup'ı kapat
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        // Kullanıcı belirtilen userId'ye sahip değil
                        Toast.makeText(UserProfile.this, "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Firestore'dan veri alınamadı
                    Toast.makeText(UserProfile.this, "Veri alınamadı", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUserStatus(String userId, String newStatus) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        usersRef.whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference userRef = usersRef.document(document.getId());
                                userRef.update("status", newStatus)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Güncelleme başarılı olduğunda yapılacak işlemler
                                                Toast.makeText(UserProfile.this, "Status güncellendi.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Güncelleme başarısız olduğunda yapılacak işlemler
                                                Toast.makeText(UserProfile.this, "Status güncellenirken hata oluştu.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            // Firestore'dan veri alınamadı
                            Toast.makeText(UserProfile.this, "Kullanıcı bulunamadı.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




    private void showPopupMessage(String recipientUserId, String currentUserId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setTitle("Eşleşmeyi kabul etmek istediğine emin misin?");
        builder.setMessage("Eminseniz evete tıklayınız.");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Evet butonuna tıklanınca yapılacak işlemler
                Toast.makeText(UserProfile.this, "Eşleşme işlemi başarıyla gerçekleşti.", Toast.LENGTH_SHORT).show();
                updateUserStatus(recipientUserId,"Aramıyor");
                updateUserStatus(currentUserId,"Aramıyor");
                Toast.makeText(UserProfile.this, "Durumunuz 'Aramıyor' olarak güncellendi.", Toast.LENGTH_SHORT).show();
                showPopupMessage2(recipientUserId);

                dialog.dismiss(); // Popup'ı kapat
            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Hayır butonuna tıklanınca yapılacak işlemler
                Toast.makeText(UserProfile.this, "Eşleşme talebi reddedildi.", Toast.LENGTH_SHORT).show();

                dialog.dismiss(); // Popup'ı kapat
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void sendMatchRequestNotificationToUser(String recipientUserId, String currentUserName, String currentUserId) {



        Snackbar.make(layout2, "Yeni eşleşme teklifi geldi.", Snackbar.LENGTH_LONG)
                .setAction("Görüntüle", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Görüntüle'ye tıklanınca yapılacak işlemler
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
                        builder.setTitle("Bir Eşleşme Talebi Aldınız.");
                        builder.setMessage(currentUserName + (" kişisi sizinle eşleşmek istiyor. Talebi onaylamak için " +
                                "kabul et butonuna tıklayınız."));
                        builder.setPositiveButton("Kabul Et", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Kabul et butonuna tıklanınca yapılacak işlemler
                                showPopupMessage(recipientUserId, currentUserId);

                                dialog.dismiss(); // Popup'ı kapat
                            }
                        });
                        builder.setNegativeButton("Reddet", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Reddet butonuna tıklanınca yapılacak işlemler
                                Toast.makeText(UserProfile.this, "Eşleşme talebi reddedildi.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss(); // Popup'ı kapat
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                })
                .show();
       /* createNotificationChannel(); // Bildirim kanalını oluştur

        String CHANNEL_ID = "my_channel_id";

        // Bildirim yapısı
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Bir Eşleşme Talebi Aldınız.")
                .setContentText(currentUserName + ("kişisi sizinle eşleşmek istiyor. Talebi onaylamak için " +
                        "bildirime tıklayınız."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);



        // Bildirimi gönderme
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = 2; // Farklı bir bildirim için farklı bir id kullanın
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());*/
    }





    private void sendNotificationToCurrentUser() {

        // Bildirim kanalını oluşturma
        createNotificationChannel();
        String CHANNEL_ID = "my_channel_id";


        // Bildirim yapısı
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Teklifiniz İletilmiştir")
                .setContentText("Eşleşme talebiniz gönderildi.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Bildirimi gönderme
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        int notificationId=1;
        notificationManager.notify(notificationId, builder.build());

    }






}
