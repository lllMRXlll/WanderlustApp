package com.example.wanderlustapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wanderlustapp.databinding.ActivityWelcomeScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeScreenActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000; // Задержка в миллисекундах (здесь 3 секунды)
    private ActivityWelcomeScreenBinding binding;
    private TextView usernameTv;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usernameTv = binding.usernameTv;
        // Получаем ссылку на текущего пользователя и на узел "Users"
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usernameRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .child("username"); //  Запрашиваем только поле "username"

        // Добавляем слушатель для получения данных пользователя
        usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    // Получаем данные из snapshot
                    username = snapshot.getValue(String.class);
                    Animation fadeIn = AnimationUtils.loadAnimation(WelcomeScreenActivity.this, R.anim.fade_in);
                    usernameTv.setText(username);
                    usernameTv.startAnimation(fadeIn);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Задержка в 3 секунды (можно убрать, если не нужна)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeScreenActivity.this, MainActivity.class);
                startActivity(intent);
                // Применяем анимацию после запуска startActivity()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish(); //Завершение
            }
        }, SPLASH_DELAY);
    }
}