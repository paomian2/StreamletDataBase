package com.streamlet.db.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.streamlet.database.R;
import com.streamlet.db.sys.SQLExport;

/**
 * @author Linxz
 * 创建日期：2021年11月04日 12:06 下午
 * version：
 * 描述：
 */
public class TableDDLFragment extends BaseFragment{

    private TextView tvDDL;

    @SuppressLint("InflateParams")
    @Override
    public View initLayout(@NonNull LayoutInflater inflater) {
        return inflater.inflate(R.layout.frag_table_ddl,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        String ddl = SQLExport.getInstance(getContext(),dbName).queryCreateTableDDL(tableName);
        tvDDL.setText(ddl);
    }

    private void initUI(){
        tvDDL = findViewById(R.id.tvDDL);
    }
}