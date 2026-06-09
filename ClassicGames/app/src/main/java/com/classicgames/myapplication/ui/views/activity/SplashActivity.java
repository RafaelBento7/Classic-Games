package com.classicgames.myapplication.ui.views.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.ui.dialog.CustomDialog;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private AppUpdateManager appUpdateManager;
    private ActivityResultLauncher<IntentSenderRequest> updateLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        updateLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
            if (result.getResultCode() != RESULT_OK) {
                // If update cancelled/failed, close app (since it's a required update)
                finish();
            }
        });

        handler.postDelayed(this::checkUpdateFlow, 2000);
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void checkUpdateFlow() {
        if (!hasInternet()) {
            goToMain();
            return;
        }

        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(info -> {
            Log.d("TESTES", info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) ? "1" : "0");
            Log.d("TESTES", info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ? "1" : "0");
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                showUpdateDialog(info);
            } else {
                goToMain();
            }
        }).addOnFailureListener(e -> {
            goToMain();
        });;
    }

    private void showUpdateDialog(AppUpdateInfo info) {
        CustomDialog dialog = new CustomDialog(this, getString(R.string.update_required), getString(R.string.new_version_available), new CustomDialog.DialogButtonClick() {
            @Override
            public void onPositiveButtonClicked() {
                // dialog.dismiss() is called automatically by CustomDialog
                startImmediateUpdate(info);
            }

            @Override
            public void onNegativeButtonClicked() {
                finish(); // Exit app if they refuse a required update
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private boolean hasInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities caps = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return caps != null && (caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) || caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED));
        } else {
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }
    }

    private void startImmediateUpdate(AppUpdateInfo info) {
        try {
            appUpdateManager.startUpdateFlowForResult(info, updateLauncher, AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
        } catch (Exception ex) {
            goToMain();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appUpdateManager == null) return;

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(info -> {
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startImmediateUpdate(info);
            }
        });
    }
}