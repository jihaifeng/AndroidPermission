package com.android.permission.Activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.permission.R;
import com.android.permission.callback.PermissionCallBack;
import com.android.permission.config.PermissionConfig;
import com.android.permission.request.PermissionRequest;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PermissionCallBack {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button btn_camera = (Button) findViewById(R.id.camera);
    btn_camera.setOnClickListener(this);
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.camera:
        PermissionRequest.with(this).addPermissions(Manifest.permission.CAMERA).addRequestCode(PermissionConfig.CAMERA_CODE).setPositiveButtonText("确定").setNegativeButtonText("取消").setReminderText("是否授权").request();
        break;
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    PermissionRequest.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
  }

  @Override public void onPermissionGranted(int requestCode, List<String> perms) {
    //已经授权
    Toast.makeText(this, "已经授权", Toast.LENGTH_SHORT).show();
  }

  @Override public void onPermissionDenied(int requestCode, List<String> perms) {
    //未授权
    Toast.makeText(this, "未授权", Toast.LENGTH_SHORT).show();
  }
}
