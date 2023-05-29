package com.example.chattingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chattingapp.R;
import com.example.chattingapp.utilities.Constants;
import com.example.chattingapp.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    RoundedImageView photo;
    EditText nname, eemail, kampuseUzaklıkk, KalmaSuresii, telefon, sosyal_medya_hesap, bolumm, siniff;
    CheckBox chk1, chk2, chk3;
    Button buttonUpdate;
    ImageView back_btn;
    DatabaseReference databaseReference;

    private Spinner spinner;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;


    String currentUserId;
    String c;

    // private ActivitySignInBinding binding;
    //private ActivityUsersBinding binding;

    private PreferenceManager preferenceManager;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        preferenceManager = new PreferenceManager(getApplicationContext());
        spinner = findViewById(R.id.spinner);
        db = FirebaseFirestore.getInstance();


        photo = findViewById(R.id.imageProfileUpdate);
        nname = findViewById(R.id.inputName);
        eemail = findViewById(R.id.inputEmail);
        kampuseUzaklıkk = findViewById(R.id.kampuseUzaklık);
        KalmaSuresii = findViewById(R.id.KalmaSuresi);
        bolumm = findViewById(R.id.bolum);
        siniff = findViewById(R.id.sinif);
        telefon = findViewById(R.id.telefon);
        chk1 = findViewById(R.id.chk1);

        chk2 = findViewById(R.id.chk2);
        chk3 = findViewById(R.id.chk3);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        back_btn = findViewById(R.id.imageBackMain);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.secenekler, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        chk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk1.isChecked()){
                    //chk1.setText(c1);
                    c = "Lisans";


                }
            }
        });

        chk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk2.isChecked()){
                    //chk2.setText(c2);
                    c = "Yüksek Lisans";


                }
            }
        });

        chk3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk3.isChecked()){
                    //chk3.setText(c3);
                    c = "Doktora";


                }
            }
        });





        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nname.getText().toString();
                String email = eemail.getText().toString();
                String kampuseUzaklık = kampuseUzaklıkk.getText().toString();
                String KalmaSuresi = KalmaSuresii.getText().toString();
                String bolum = bolumm.getText().toString();
                String sinif = siniff.getText().toString();
                //String girisyili = giris_yili.getText().toString();
                //String mezunyili = mezun_yili.getText().toString();
                String telefonn = telefon.getText().toString();
                //String ulkee = ulke.getText().toString();
                //String sehirr = sehir.getText().toString();
                //String firmaa = firma.getText().toString();
                //String sosyal = sosyal_medya_hesap.getText().toString();
                // String cd = c.toString();

                String selectedOption = spinner.getSelectedItem().toString();
                //kaydet(selectedOption);



                if (!chk1.isChecked() & !chk2.isChecked() & !chk3.isChecked()){
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    firestore.collection("users").document(currentUserId)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        String egitimDurumu = documentSnapshot.getString("egitim_durumu");
                                        //og.d(TAG, "egitimDurumu: " + egitimDurumu);
                                        // egitim_durumu alanını burada kullanabilirsiniz
                                        UpdateData(name,email,telefonn,bolum,sinif,egitimDurumu,selectedOption,kampuseUzaklık,KalmaSuresi);
                                    } else {

                                        Toast.makeText(EditProfile.this, "Belirtilen belge yok", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfile.this, "Hata oluştu:", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    UpdateData(name,email,telefonn,bolum,sinif,c,selectedOption,kampuseUzaklık,KalmaSuresi);
                }


            }
        });

        setListeners();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void setListeners(){
        back_btn.setOnClickListener(v -> onBackPressed());
    }

    private void UpdateData(String name, String email, String telefonn, String bolum,String sinif, String cb, String selectedOption,String kampuseUzaklık, String KalmaSuresi) {
        Map<String, Object> userDetail = new HashMap<>();
        if(name != null && !name.isEmpty()){
            userDetail.put("name",name);
        }
        if (email != null && !email.isEmpty()){
            userDetail.put("email",email);
        }

        if (telefonn != null && !telefonn.isEmpty()){
            userDetail.put("telefon",telefonn);
        }
        if (bolum != null && !bolum.isEmpty()){
            userDetail.put("bölüm",bolum);
        }


        if (sinif != null && !sinif.isEmpty()) {
            userDetail.put("sınıf",sinif);
        }
        if (cb != null && !cb.isEmpty()) {
            userDetail.put("egitim_durumu",cb);
        }
        if (selectedOption != null && !selectedOption.isEmpty()) {
            userDetail.put("status",selectedOption);
        }
        if(kampuseUzaklık != null && !kampuseUzaklık.isEmpty()){
            userDetail.put("kampuse_uzaklık",kampuseUzaklık);
        }
        if(KalmaSuresi != null && !KalmaSuresi.isEmpty()){
            userDetail.put("kalma_suresi",KalmaSuresi);
        }




        FirebaseFirestore database =  FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {

                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (currentUserId != null) {
                        database.collection("users")
                                .document(currentUserId)
                                .update(userDetail)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(EditProfile.this, "Profil Güncellendi", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProfile.this, "Hata oluştu", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(EditProfile.this, "Kullanıcı kimliği bulunamadı", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void kaydet(String selectedOption) {
        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
        DocumentReference docRef = db.collection("users").document(currentUserId);
        Map<String, Object> data = new HashMap<>();
        data.put("status", selectedOption);

        docRef.update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Kaydetme işlemi başarılı
                        } else {
                            // Kaydetme işlemi başarısız
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedOption = parent.getItemAtPosition(position).toString();
        // Seçilen öğeyi kullanabilirsiniz
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Hiçbir şey seçilmediğinde yapılacak işlemler
    }
}