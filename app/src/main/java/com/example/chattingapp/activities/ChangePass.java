package com.example.chattingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chattingapp.R;
import com.example.chattingapp.utilities.Constants;
import com.example.chattingapp.utilities.PreferenceManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class ChangePass extends AppCompatActivity {

    EditText inputOldPass, inputPass, inputConfirmPass;
    Button buttonSave;
    ImageView back_btn;
    DatabaseReference databaseReference;
    private PreferenceManager preferenceManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        inputOldPass = findViewById(R.id.inputOldPassword);
        inputPass = findViewById(R.id.inputPassword);
        inputConfirmPass = findViewById(R.id.inputConfirmPassword);
        buttonSave = findViewById(R.id.buttonSave);
        back_btn = findViewById(R.id.imageBackMain);
        preferenceManager = new PreferenceManager(getApplicationContext());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_pass = inputOldPass.getText().toString();
                String new_pass = inputPass.getText().toString();
                String conf_pass = inputConfirmPass.getText().toString();



                UpdatePass(old_pass,new_pass,conf_pass);
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

    private void UpdatePass(String old_pass, String new_pass, String conf_pass) {
        //  Map<String, Object> userPass = new HashMap<>();
        //  userPass.put("password",new_pass);

        FirebaseFirestore database = FirebaseFirestore.getInstance();


        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {


                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {

                                //String currentUserPass = preferenceManager.getString(Constants.KEY_PASSWORD);
                                String currentUserPass = queryDocumentSnapshot.getString("password");
                                System.out.println(old_pass);
                                System.out.println(currentUserPass);

                                if (currentUserPass.equals(old_pass)) {
                                    if (new_pass.equals(conf_pass)) {
                                        Map<String, Object> userPass = new HashMap<>();
                                        userPass.put(Constants.KEY_PASSWORD, new_pass);

                                        database.collection(Constants.KEY_COLLECTION_USERS)
                                                .document(currentUserId)
                                                .update(userPass)
                                                .addOnSuccessListener(unused -> {
                                                    Toast.makeText(ChangePass.this, "Parola Güncellendi", Toast.LENGTH_SHORT).show();
                                                }).addOnFailureListener(e -> {
                                                    Toast.makeText(ChangePass.this, "Hata oluştu", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        Toast.makeText(ChangePass.this, "Parolanız doğrulanamadı", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ChangePass.this, "Hata oluştu2", Toast.LENGTH_SHORT).show();
                                }

                            }else {

                            }
                        }


                    } else {
                        Toast.makeText(ChangePass.this, "Task is not successfull", Toast.LENGTH_SHORT).show();
                    }
                });











       /* FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            if(new_pass.equals(conf_pass)) {

                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                String documentID = documentSnapshot.getId();

                                database.collection("users")
                                        .document(documentID)
                                        .update(userPass)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ChangePass.this, "Parola Güncellendi", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ChangePass.this, "Hata oluştu", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }else{
                                Toast.makeText(ChangePass.this, "Parolanız doğrulanamadı", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(ChangePass.this, "Hata oluştu2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

    }

}