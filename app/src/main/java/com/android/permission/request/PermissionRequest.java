package com.android.permission.request;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import com.android.permission.callback.PermissionCallBack;
import com.android.permission.dialog.DialogCommon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Func：Android 6.0 (即sdk 23) 以后运行时权限申请
 * User：jihf
 * Data：2016-09-21
 * Time: 16:23
 * Mail：jihaifeng@raiyi.com
 */
public class PermissionRequest {
  //跳转到权限设置界面的请求码
  public static final int SETTING_REQUEST_CODE = 161;
  //申请权限的对象
  private Object object;
  //申请的权限
  private String[] mPermissions;
  //确认按钮的文字
  private String mPositiveButtonText = "Ok";
  //取消按钮的文字
  private String mNegativeButtonText = "Cancel";
  //申请权限的提示语
  private String mReminderText;
  //申请权限的请求码
  private int mRequestCode;
  //上下文
  public static Context mContext;
  //
  private static DialogCommon dialogCheck;

  private PermissionRequest(Object object) {
    this.object = object;
  }

  /**
   * @param activity activity中申请
   */

  public static PermissionRequest with(Activity activity) {
    mContext = activity;
    return new PermissionRequest(activity);
  }

  public static PermissionRequest with(Application application) {
    mContext = application;
    return new PermissionRequest(application);
  }
  public static PermissionRequest with(Context context) {
    mContext = context;
    return new PermissionRequest(context);
  }

  /**
   * @param fragment fragment中申请
   */
  public static PermissionRequest with(Fragment fragment) {
    mContext = fragment.getActivity();
    return new PermissionRequest(fragment);
  }

  /**
   * @param permissions 添加申请的权限（可以是多个）
   */
  public PermissionRequest addPermissions(String... permissions) {
    this.mPermissions = permissions;
    return this;
  }

  /**
   * @param requestCode 请求码
   */
  public PermissionRequest addRequestCode(int requestCode) {
    this.mRequestCode = requestCode;
    return this;
  }

  /**
   * @param reminderText 自定义权限申请时的提示语
   */
  public PermissionRequest setReminderText(String reminderText) {
    this.mReminderText = reminderText;
    return this;
  }

  /**
   * @param positiveButtonText 自定义权限申请时确认按钮的文字
   */
  public PermissionRequest setPositiveButtonText(String positiveButtonText) {
    this.mPositiveButtonText = positiveButtonText;
    return this;
  }

  /**
   * @param negativeButtonText 自定义权限申请时取消按钮的文字
   */
  public PermissionRequest setNegativeButtonText(String negativeButtonText) {
    this.mNegativeButtonText = negativeButtonText;
    return this;
  }

  /**
   * 开始请求权限
   */
  public void request() {
    requestPermission(object, mReminderText, mRequestCode, mPositiveButtonText, mNegativeButtonText, mPermissions);
  }

  public static void requestPermissions(final Object object, String reminderText, final int requestCode, final String... permissions) {
    requestPermission(object, reminderText, requestCode, "Ok", "Cancel", permissions);
  }

  private static void requestPermission(final Object object, String reminderText, final int requestCode, String positiveButtonText, String negativeButtonText, String... permissions) {
    checkCallingObjectSuitability(object);
    PermissionCallBack mCallBack = (PermissionCallBack) object;
    if (!PermissionRequestUtils.isOverMarshmallow()) {
      mCallBack.onPermissionGranted(requestCode, Arrays.asList(permissions));
      return;
    }

    final List<String> deniedPermissions = PermissionRequestUtils.findDeniedPermissions(PermissionRequestUtils.getActivity(object), permissions);

    boolean shouldShowRationale = false;
    for (String perm : deniedPermissions) {
      shouldShowRationale = shouldShowRationale || PermissionRequestUtils.shouldShowRequestPermissionRationale(object, perm);
    }

    if (PermissionRequestUtils.isEmpty(deniedPermissions)) {
      mCallBack.onPermissionGranted(requestCode, Arrays.asList(permissions));
    } else {

      final String[] deniedPermissionArray = deniedPermissions.toArray(new String[deniedPermissions.size()]);

      if (shouldShowRationale) {
        Activity activity = PermissionRequestUtils.getActivity(object);
        if (null == activity) {
          return;
        }

        DialogCommon dialogCommon = new DialogCommon(mContext, 0, "116114提醒您", 0, reminderText, positiveButtonText, negativeButtonText, new DialogCommon.OnDialogCommonClickListener() {
          @Override public void submitClick(DialogCommon dialogCommon) {
            executePermissionsRequest(object, deniedPermissionArray, requestCode);
            dialogCommon.dismissDialog();
          }

          @Override public void cancelClick(DialogCommon dialogCommon) {
            ((PermissionCallBack) object).onPermissionDenied(requestCode, deniedPermissions);
            dialogCommon.dismissDialog();
          }

          @Override public void knowClick(DialogCommon dialogCommon) {

          }
        });
        dialogCommon.setCancelable(false);
        if (dialogCommon.isShowing()) {
          dialogCommon.dismissDialog();
        }
        dialogCommon.showDialog();
      } else {
        executePermissionsRequest(object, deniedPermissionArray, requestCode);
      }
    }
  }

  @TargetApi(23) private static void executePermissionsRequest(Object object, String[] perms, int requestCode) {
    checkCallingObjectSuitability(object);

    if (object instanceof Activity) {
      ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
    } else if (object instanceof Fragment) {
      ((Fragment) object).requestPermissions(perms, requestCode);
    } else if (object instanceof android.app.Fragment) {
      ((android.app.Fragment) object).requestPermissions(perms, requestCode);
    }
  }

  /**
   * 权限申请返回结果
   */
  public static void onRequestPermissionsResult(Object object, int requestCode, String[] permissions, int[] grantResults) {
    checkCallingObjectSuitability(object);

    PermissionCallBack mCallBack = (PermissionCallBack) object;

    List<String> deniedPermissions = new ArrayList<>();
    for (int i = 0; i < grantResults.length; i++) {
      if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
        deniedPermissions.add(permissions[i]);
      }
    }

    if (PermissionRequestUtils.isEmpty(deniedPermissions)) {
      mCallBack.onPermissionGranted(requestCode, Arrays.asList(permissions));
    } else {
      mCallBack.onPermissionDenied(requestCode, deniedPermissions);
    }
  }

  /**
   * 在OnActivityResult中接收判断是否已经授权
   * 使用{@link PermissionRequest#hasPermissions(Context, String...)}进行判断
   *
   * If user denied permissions with the flag NEVER ASK AGAIN, open a dialog explaining the
   * permissions rationale again and directing the user to the app settings. After the user
   * returned to the app, {@link Activity#onActivityResult(int, int, Intent)} or
   * {@link Fragment#onActivityResult(int, int, Intent)} or
   * {@link android.app.Fragment#onActivityResult(int, int, Intent)} will be called with
   * {@value #SETTING_REQUEST_CODE} as requestCode
   * <p>
   *
   * NOTE: use of this method is optional, should be called from
   * {@link PermissionCallBack#onPermissionDenied(int, List)}
   *
   * @return {@code true} if user denied at least one permission with the flag NEVER ASK AGAIN.
   */
  public static boolean checkDeniedPermissionsNeverAskAgain(final Object object, String rationale, String positiveButton, String negativeButton, List<String> deniedPerms) {
    boolean shouldShowRationale;
    for (String perm : deniedPerms) {
      shouldShowRationale = PermissionRequestUtils.shouldShowRequestPermissionRationale(object, perm);

      if (!shouldShowRationale) {
        final Activity activity = PermissionRequestUtils.getActivity(object);
        if (null == activity) {
          return true;
        }
        dialogCheck = new DialogCommon(mContext, 0, "116114提醒您", 0, rationale, positiveButton, negativeButton, new DialogCommon.OnDialogCommonClickListener() {
          @Override public void submitClick(DialogCommon dialogCommon) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            startAppSettingsScreen(object, intent);
            dialogCheck.dismissDialog();
          }

          @Override public void cancelClick(DialogCommon dialogCommon) {
            dialogCommon.dismissDialog();
          }

          @Override public void knowClick(DialogCommon dialogCommon) {

          }
        });
        dialogCheck.setCancelable(false);
        if (dialogCheck.isShowing()) {
          dialogCheck.dismissDialog();
        }
        dialogCheck.showDialog();
        return true;
      }
    }
    return false;
  }

  public static boolean isCheckDialogDismiss() {
    return null == dialogCheck || !dialogCheck.isShowing();
  }

  /**
   * 进入权限设置界面
   */
  @TargetApi(11) private static void startAppSettingsScreen(Object object, Intent intent) {
    if (object instanceof Activity) {
      ((Activity) object).startActivityForResult(intent, SETTING_REQUEST_CODE);
    } else if (object instanceof Fragment) {
      ((Fragment) object).startActivityForResult(intent, SETTING_REQUEST_CODE);
    } else if (object instanceof android.app.Fragment) {
      ((android.app.Fragment) object).startActivityForResult(intent, SETTING_REQUEST_CODE);
    }
  }

  /**
   * @param perms 判断权限是否存在
   */
  public static boolean hasPermissions(Context context, String... perms) {
    // Always return true for SDK < M, let the system deal with the permissions
    if (!PermissionRequestUtils.isOverMarshmallow()) {
      return true;
    }

    for (String perm : perms) {
      boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED);
      if (!hasPerm) {
        return false;
      }
    }

    return true;
  }

  /**
   * @param object 检查权限申请的对象
   */
  private static void checkCallingObjectSuitability(Object object) {

    if (!((object instanceof Fragment) || (object instanceof Activity) || (object instanceof android.app.Fragment))) {
      throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
    }

    if (!(object instanceof PermissionCallBack)) {
      throw new IllegalArgumentException("Caller must implement PermissionCallback.");
    }
  }
}
