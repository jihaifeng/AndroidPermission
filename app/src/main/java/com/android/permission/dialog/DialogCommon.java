package com.android.permission.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnKeyListener;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.permission.R;
import java.util.ArrayList;
import java.util.List;

public class DialogCommon implements OnClickListener {

  private Context context;

  private ImageView dialogTitleImg;// 弹出框标题图片
  private TextView dialogTitleTxt;// 弹出框标题文字
  private ImageView dialogBodyImg;// 弹出框内容图片
  private TextView dialogBodyTxt;// 弹出框内容文字
  private Button dialogButConfirm;// 弹出框确认按钮
  private Button dialogButCancel;// 弹出框取消按钮
  private Button dialogButKnow02;// 弹出框知道按钮
  private OnDialogCommonClickListener listener;// 弹出框单击监听
  private Dialog dialog;
  private LinearLayout dialogBodyView;// 内容view
  private LinearLayout dialogAddView;// 添加自定义view
  private LinearLayout dialogListLayout;// 列表视图
  private ListView dialogListView;//
  private DialogAdapter adapter = new DialogAdapter();
  private OnItemClickListener onItemClickListener;

  private int titleImgResouce;
  private String titleTxt;
  private int bodyImgResouce;
  private String bodyTxt;
  private String butConfirmStr;
  private String butCancelStr;
  private String butKnowStr;
  private List<String> items;

  private ImageView itstView;
  private LinearLayout itstLayout;

  private LinearLayout mLlTitle;
  private LinearLayout mLlBtns;

  /**
   * 两个按钮
   *
   * @param titleImgResouce 弹出框标题图片 0代表无
   * @param titleTxt 弹出框标题文字
   * @param bodyImgResouce 弹出框内容图片 0代表无
   * @param bodyTxt 弹出框内容文字
   * @param butConfirm 弹出框确认按钮文字
   * @param butCancel 弹出框取消按钮文字
   * @param onDialogCommonClickListener 弹出框单击监听
   */
  public DialogCommon(Context context, int titleImgResouce, String titleTxt, int bodyImgResouce, String bodyTxt, String butConfirm, String butCancel, OnDialogCommonClickListener onDialogCommonClickListener) {
    this.context = context;

    this.titleImgResouce = titleImgResouce;
    this.titleTxt = titleTxt;
    this.bodyImgResouce = bodyImgResouce;
    this.bodyTxt = bodyTxt;
    this.butConfirmStr = butConfirm;
    this.butCancelStr = butCancel;
    this.listener = onDialogCommonClickListener;

    init(1);
  }

  public void setTitleImgResouce(int titleImgResouce) {
    this.titleImgResouce = titleImgResouce;
    if (titleImgResouce != 0) {
      dialogTitleImg.setImageResource(titleImgResouce);
    }
  }

  public void setTitleTxt(String titleTxt) {
    this.titleTxt = titleTxt;
    if (titleTxt != null) {
      dialogTitleTxt.setText(titleTxt);
    }
  }

  public void setBodyImgResouce(int bodyImgResouce) {
    this.bodyImgResouce = bodyImgResouce;
    if (bodyImgResouce != 0) {
      dialogBodyImg.setImageResource(bodyImgResouce);
    }
  }

  public void setBodyTxt(String bodyTxt) {
    this.bodyTxt = bodyTxt;
    if (bodyTxt != null) {
      dialogBodyTxt.setText(bodyTxt);
    }
  }

  public void setButConfirmStr(String butConfirmStr) {
    this.butConfirmStr = butConfirmStr;
    if (butConfirmStr != null) {
      dialogButConfirm.setText(butConfirmStr);
    }
  }

  public void setButCancelStr(String butCancelStr) {
    this.butCancelStr = butCancelStr;
    if (butCancelStr != null) {
      dialogButCancel.setText(butCancelStr);
    }
  }

  /**
   * 一个按钮
   *
   * @param titleImgResouce 弹出框标题图片 0代表无
   * @param titleTxt 弹出框标题文字
   * @param bodyImgResouce 弹出框内容图片 0代表无
   * @param bodyTxt 弹出框内容文字
   * @param butKnow 弹出框知道按钮文字
   * @param onDialogCommonClickListener 弹出框单击监听
   */
  public DialogCommon(Context context, int titleImgResouce, String titleTxt, int bodyImgResouce, String bodyTxt, String butKnow, OnDialogCommonClickListener onDialogCommonClickListener) {
    this.context = context;

    this.titleImgResouce = titleImgResouce;
    this.titleTxt = titleTxt;
    this.bodyImgResouce = bodyImgResouce;
    this.bodyTxt = bodyTxt;
    this.butKnowStr = butKnow;
    this.listener = onDialogCommonClickListener;

    init(2);
  }

  /**
   * 自定义view
   *
   * @param titleImgResouce 弹出框标题图片 0代表无
   * @param titleTxt 弹出框标题文字
   * @param butConfirm 弹出框确认按钮文字
   * @param butCancel 弹出框取消按钮文字
   * @param onDialogCommonClickListener 弹出框单击监听
   */
  public DialogCommon(Context context, int titleImgResouce, String titleTxt, String butConfirm, String butCancel, OnDialogCommonClickListener onDialogCommonClickListener) {
    this.context = context;
    this.titleImgResouce = titleImgResouce;
    this.titleTxt = titleTxt;
    this.butConfirmStr = butConfirm;
    this.butCancelStr = butCancel;
    this.listener = onDialogCommonClickListener;
    init(3);
  }

  /**
   * 列表view
   *
   * @param titleImgResouce 弹出框标题图片 0代表无
   * @param titleTxt 弹出框标题文字
   * @param butKnow 弹出框知道按钮文字
   * @param items 列表数据
   * @param onDialogCommonClickListener 弹出框单击监听
   */
  public DialogCommon(Context context, int titleImgResouce, String titleTxt, String butKnow, List<String> items, OnDialogCommonClickListener onDialogCommonClickListener, OnItemClickListener onItemClickListener) {
    this.context = context;

    this.titleImgResouce = titleImgResouce;
    this.titleTxt = titleTxt;
    this.butKnowStr = butKnow;
    this.items = items;
    this.listener = onDialogCommonClickListener;
    this.onItemClickListener = onItemClickListener;

    init(4);
  }

  /**
   * 初始化组件
   */
  private void init(int flag) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.dialog_common, null);
    mLlBtns = (LinearLayout) view.findViewById(R.id.dialog_but_01);
    mLlTitle = (LinearLayout) view.findViewById(R.id.dialog_ll_title);
    dialogAddView = (LinearLayout) view.findViewById(R.id.dialog_add_view);
    dialogBodyView = (LinearLayout) view.findViewById(R.id.dialog_body_view);
    dialogListLayout = (LinearLayout) view.findViewById(R.id.dialog_list_layout);
    // 增加广告获取
    itstView = (ImageView) view.findViewById(R.id.dialog_itst_img);
    itstLayout = (LinearLayout) view.findViewById(R.id.dialog_itst_view);
    if (flag == 1) {
      view.findViewById(R.id.dialog_but_01).setVisibility(View.VISIBLE);
      view.findViewById(R.id.dialog_but_02).setVisibility(View.GONE);
      dialogAddView.setVisibility(View.GONE);
      dialogBodyView.setVisibility(View.VISIBLE);
      dialogListLayout.setVisibility(View.GONE);
    }
    if (flag == 2) {
      view.findViewById(R.id.dialog_but_01).setVisibility(View.GONE);
      view.findViewById(R.id.dialog_but_02).setVisibility(View.VISIBLE);
      dialogAddView.setVisibility(View.GONE);
      dialogBodyView.setVisibility(View.VISIBLE);
      dialogListLayout.setVisibility(View.GONE);
    }
    if (flag == 3) {
      view.findViewById(R.id.dialog_but_01).setVisibility(View.VISIBLE);
      view.findViewById(R.id.dialog_but_02).setVisibility(View.GONE);
      dialogAddView.setVisibility(View.VISIBLE);
      dialogBodyView.setVisibility(View.GONE);
      dialogListLayout.setVisibility(View.GONE);
    }
    if (flag == 4) {
      view.findViewById(R.id.dialog_but_01).setVisibility(View.GONE);
      view.findViewById(R.id.dialog_but_02).setVisibility(View.VISIBLE);
      dialogAddView.setVisibility(View.GONE);
      dialogBodyView.setVisibility(View.GONE);
      dialogListLayout.setVisibility(View.VISIBLE);
    }

    dialogTitleImg = (ImageView) view.findViewById(R.id.dialog_title_img);
    dialogTitleImg.setOnClickListener(this);
    dialogTitleTxt = (TextView) view.findViewById(R.id.dialog_title_txt);
    dialogTitleTxt.setOnClickListener(this);
    dialogBodyImg = (ImageView) view.findViewById(R.id.dialog_body_img);
    dialogBodyImg.setOnClickListener(this);
    dialogBodyTxt = (TextView) view.findViewById(R.id.dialog_body_txt);
    dialogBodyTxt.setOnClickListener(this);
    dialogButConfirm = (Button) view.findViewById(R.id.dialog_but_confirm_01);
    dialogButConfirm.setOnClickListener(this);
    dialogButCancel = (Button) view.findViewById(R.id.dialog_but_cancel_01);
    dialogButCancel.setOnClickListener(this);
    dialogButKnow02 = (Button) view.findViewById(R.id.dialog_but_know_02);
    dialogButKnow02.setOnClickListener(this);
    dialogListView = (ListView) view.findViewById(R.id.dialog_list_view);
    if (items != null) {
      adapter.setList(items);
      if (items.size() > 4) {
        LayoutParams lp = dialogListLayout.getLayoutParams();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = context.getResources().getDimensionPixelSize(R.dimen.dialog_common_list_view_maxheight);
        dialogListLayout.setLayoutParams(lp);
      }
    }
    dialogListView.setAdapter(adapter);

    if (titleImgResouce != 0) {
      dialogTitleImg.setImageResource(titleImgResouce);
    }
    if (titleTxt != null) {
      dialogTitleTxt.setText(titleTxt);
    }
    if (bodyImgResouce != 0) {
      dialogBodyImg.setImageResource(bodyImgResouce);
    }
    if (bodyTxt != null) {
      dialogBodyTxt.setText(bodyTxt);
    }
    if (butConfirmStr != null) {
      dialogButConfirm.setText(butConfirmStr);
    }
    if (butCancelStr != null) {
      dialogButCancel.setText(butCancelStr);
    }
    if (butKnowStr != null) {
      dialogButKnow02.setText(butKnowStr);
    }

    dialog = new Dialog(context, R.style.commentDialog);
    dialog.setContentView(view);
    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
    lp.width = context.getResources().getDimensionPixelSize(R.dimen.dialog_common_width);
    lp.height = LayoutParams.WRAP_CONTENT;
    dialog.getWindow().setAttributes(lp);
  }

  /**
   * 单击事件操作
   */
  @Override public void onClick(View v) {
    if (v.getId() == R.id.dialog_but_know_02) {// 单击弹出框知道按钮
      if (listener != null) {
        listener.knowClick(this);
      }
      return;
    }
    if (v.getId() == R.id.dialog_but_confirm_01) {// 单击弹出框确认按钮
      if (listener != null) {
        listener.submitClick(this);
      }
      return;
    }
    if (v.getId() == R.id.dialog_but_cancel_01) {// 单击弹出框取消按钮
      if (listener != null) {
        listener.cancelClick(this);
      }
    }
  }

  /**
   * 设置事件监听
   */
  public void setListener(OnDialogCommonClickListener listener) {
    this.listener = listener;
  }

  /**
   * 设置list 事件
   */
  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    if (onItemClickListener != null && dialogListView != null) {
      dialogListView.setOnItemClickListener(onItemClickListener);
    }
  }

  /**
   * 设置弹出框标题图片显示
   */
  public void setTitleImgShow() {
    if (dialogTitleImg != null) {
      dialogTitleImg.setVisibility(View.VISIBLE);
    }
  }

  /**
   * 设置弹出框标题图片隐藏
   */
  public void setTitleImgHide() {
    if (dialogTitleImg != null) {
      dialogTitleImg.setVisibility(View.GONE);
    }
  }

  /**
   * 设置弹出框内容图片显示
   */
  public void setBodyImgShow() {
    if (dialogBodyImg != null) {
      dialogBodyImg.setVisibility(View.VISIBLE);
    }
  }

  /**
   * 设置弹出框内容图片隐藏
   */
  public void setBodyImgHide() {
    if (dialogBodyImg != null) {
      dialogBodyImg.setVisibility(View.GONE);
    }
  }

  public void setItstViewShow() {
    itstLayout.setVisibility(View.VISIBLE);
    dialogAddView.setVisibility(View.GONE);
  }

  /**
   * 显示弹出框
   */
  public void showDialog() {
    try {
      if (context != null && context instanceof Activity) {
        if (((Activity) context).isFinishing()) {
          return;
        }
      }
      if (dialog != null) {
        dialog.show();
      }
    } catch (Exception e) {
    }
  }

  /**
   * 隐藏弹出框
   */
  public void hideDialog() {
    try {
      if (dialog != null) {
        dialog.hide();
      }
    } catch (Exception e) {
    }
  }

  /**
   * 判断弹出框是否显示
   */
  public boolean isShowing() {
    if (dialog != null) {
      return dialog.isShowing();
    }
    return false;
  }

  /**
   * 隐藏弹出框，并释放对话框所占的资源
   */
  public void dismissDialog() {
    try {
      if (dialog != null) {
        dialog.dismiss();
      }
    } catch (Exception e) {
    }
  }

  /**
   * 设置点击dialog以外区域是否隐藏
   */
  public void setCancelable(boolean flag) {
    try {
      if (dialog != null) {
        dialog.setCancelable(flag);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
    }
  }

  /**
   * 添加自定义视图
   */
  public void addView(View view) {
    if (dialogAddView != null) {
      dialogAddView.removeAllViews();
      LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
      dialogAddView.addView(view, params);
      dialogAddView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * 设置title隐藏显示
   */
  public void setTitleVisibility(int visibility) {
    mLlTitle.setVisibility(visibility);
    setContextViewBg();
  }

  /**
   * 设置底部按钮隐藏显示
   */
  public void setllBtnsVisibility(int visibility) {
    mLlBtns.setVisibility(visibility);
    setContextViewBg();
  }

  private void setContextViewBg() {
    if (mLlTitle.getVisibility() == View.VISIBLE && mLlBtns.getVisibility() == View.VISIBLE) {
      dialogAddView.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
    } else if (mLlTitle.getVisibility() == View.VISIBLE && mLlBtns.getVisibility() == View.GONE) {
      dialogAddView.setBackgroundResource(R.drawable.dialog_common_view_bt_confirm);
    } else if (mLlTitle.getVisibility() == View.GONE && mLlBtns.getVisibility() == View.VISIBLE) {
      dialogAddView.setBackgroundResource(R.drawable.dialog_common_title_drawbale);
    } else {
      dialogAddView.setBackgroundResource(R.drawable.dialog_common_view);
    }
  }

  /**
   *
   */
  public void setOnKeyListener(OnKeyListener onKeyListener) {
    if (dialog != null) {
      dialog.setOnKeyListener(onKeyListener);
    }
  }

  /**
   * 设置body文本
   */
  public void setDialogBodyTxt(String bodyTxt) {
    if (bodyTxt != null && dialogBodyTxt != null) {
      dialogBodyTxt.setText(bodyTxt);
    }
  }

  public ImageView getItstImageView() {
    return itstView;
  }

  /**
   * @author raiyi-suzhou
   */
  public interface OnDialogCommonClickListener {
    public void submitClick(DialogCommon dialogCommon);

    public void cancelClick(DialogCommon dialogCommon);

    public void knowClick(DialogCommon dialogCommon);
  }

  public class DialogAdapter extends BaseAdapter {

    private List<String> list = new ArrayList<String>();

    public void setList(List<String> list) {
      this.list = list;
    }

    @Override public int getCount() {
      if (list != null) {
        return list.size();
      }
      return 0;
    }

    @Override public Object getItem(int position) {
      if (list != null && list.size() > 0) {
        return list.get(position);
      }
      return null;
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder;
      if (convertView == null) {
        holder = new ViewHolder();
        convertView = View.inflate(context, R.layout.dialog_common_item_layout, null);
        holder.dialogItemTitle = (TextView) convertView.findViewById(R.id.dialog_item_title);
        convertView.setTag(holder);
      } else {
        holder = (ViewHolder) convertView.getTag();
      }

      if (list != null && list.size() > 0) {
        holder.dialogItemTitle.setText(list.get(position));
      }
      return convertView;
    }

    class ViewHolder {
      TextView dialogItemTitle;
    }
  }

  public Dialog getDialog() {
    return dialog;
  }
}
