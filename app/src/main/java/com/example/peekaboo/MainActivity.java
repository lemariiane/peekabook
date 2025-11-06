package com.example.peekaboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends AppCompatActivity {

    // Tempo que o logo ficará na tela (2 segundos = 2000 milissegundos)
    private static final long SPLASH_DELAY_MS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 1. INSTALAÇÃO DA SPLASH SCREEN (Obrigatoriamente antes de super.onCreate)
        // O método 'installSplashScreen()' é um método estático em Java,
        // mas a importação não é necessária se você usar o nome da classe.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // 2. CONFIGURAÇÃO DO REDIRECIONAMENTO
        // Usamos um Handler para garantir que o logo permaneça por SPLASH_DELAY_MS antes de redirecionar.
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Cria a Intent para iniciar a LoginActivity
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);

                // Finaliza a MainActivity (Splash) para que o usuário não possa voltar
                finish();
            }
        }, SPLASH_DELAY_MS);

        // IMPORTANTE: Não chame setContentView() aqui, pois a UI é controlada pelo tema
        // até o fim do delay, quando a LoginActivity será iniciada.
    }
}