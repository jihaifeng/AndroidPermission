package com.android.permission.callback;

import android.support.v4.app.ActivityCompat;
import java.util.List;

/**
 * Func：权限申请回调
 * User：jihf
 * Data：2016-09-21
 * Time: 16:25
 * Mail：jihaifeng@raiyi.com
 */
public interface PermissionCallBack extends ActivityCompat.OnRequestPermissionsResultCallback {

  void onPermissionGranted(int requestCode, List<String> perms);

  void onPermissionDenied(int requestCode, List<String> perms);
}
