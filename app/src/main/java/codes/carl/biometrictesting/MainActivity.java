package codes.carl.biometrictesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "AUTH-TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatButton authButton = findViewById(R.id.authenticate);

        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth();
            }
        });
    }

    private void auth() {
        Executor newExecutor = Executors.newSingleThreadExecutor();

        final BiometricPrompt myBiometricPrompt = new BiometricPrompt(this, newExecutor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                } else {
                    Log.d(TAG, "An unrecoverable error occurred");
                }

                int result = BiometricManager.from(MainActivity.this).canAuthenticate();
                Log.d(TAG, String.valueOf(result));
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d(TAG, "Fingerprint recognised successfully");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d(TAG, "Fingerprint not recognised");
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Title text goes here")
                .setSubtitle("Subtitle goes here")
                .setDescription("This is the description")
                .setDeviceCredentialAllowed(true)
                //.setNegativeButtonText("Cancel")
                .build();

        myBiometricPrompt.authenticate(promptInfo);
    }
}
