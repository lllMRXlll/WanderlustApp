package com.example.wanderlustapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wanderlustapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    long timestamp = System.currentTimeMillis();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.buttonSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editTextEmailAddress.getText().toString().isEmpty() || binding.editTextPassword.getText().toString().isEmpty() || binding.editTextUsername.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.editTextEmailAddress.getText().toString(),binding.editTextPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Account successfully created", Toast.LENGTH_SHORT).show();
                                        HashMap<String, Object> userInfo = new HashMap<>();
                                        userInfo.put("email", binding.editTextEmailAddress.getText().toString());
                                        userInfo.put("password", binding.editTextPassword.getText().toString());
                                        userInfo.put("username", binding.editTextUsername.getText().toString());
                                        userInfo.put("timestamp",timestamp);
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(userInfo);

                                        startActivity(new Intent(SignUpActivity.this, WelcomeScreenActivity.class));
                                    }

                                }
                            });
                }
            }
        });
    }
}