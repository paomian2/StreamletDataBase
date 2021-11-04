package com.streamlet.db.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.streamlet.database.R;
import com.streamlet.db.client.DbLog;
import com.streamlet.db.utils.PhoneManager;

/**
 * @author Linxz
 * 创建日期：2021年11月01日 1:53 下午
 * version：
 * 描述：
 */
public class DialogHelper {

    public static Dialog editTableColumn(Context mContext,OnResultClickListener<String> okListener){
        final Dialog dialog = new Dialog(mContext, R.style.promptDialog);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_table_column, null);
        dialog.setContentView(view);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.show();
        return dialog;
    }


    public static Dialog showCreateDialog(Context mContext,OnResultClickListener<String> okListener){
        final Dialog dialog = new Dialog(mContext, R.style.promptDialog);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_create_db, null);
        TextView tvPath=view.findViewById(R.id.tvPath);
        EditText edtName=view.findViewById(R.id.edtName);
        TextView tvExist=view.findViewById(R.id.tvExist);
        TextView tvOk=view.findViewById(R.id.tvOk);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        @SuppressLint("SdCardPath") String path ="文件地址：" + "/data/data/" + PhoneManager.getPackageName(mContext)+"/databases";
        tvPath.setText(path);
        tvExist.setOnClickListener(v -> dialog.dismiss());
        tvOk.setOnClickListener(v -> {
            if (edtName.getText().toString().trim().isEmpty()){
                Toast.makeText(mContext,"请输入数据库名字",Toast.LENGTH_SHORT).show();
                return;
            }
            okListener.onResult(edtName.getText().toString().trim());
            dialog.dismiss();
        });
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.show();
        return dialog;
    }

    public interface OnRightClickListener{
        void onRightClick();
    }

    public interface OnResultClickListener<T>{
        void onResult(T object);
    }


} 