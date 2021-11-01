package com.streamlet.db.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import com.streamlet.database.R;
/**
 * @author Linxz
 * 创建日期：2021年11月01日 4:30 下午
 * version：
 * 描述：
 */
public class TableDataFragment extends BaseFragment {

    @SuppressLint("InflateParams")
    @Override
    public View initLayout(@NonNull LayoutInflater inflater) {
        return inflater.inflate(R.layout.frag_table_data,null);
    }

    @Override
    public void refreshDatabaseName(String name) {
        super.refreshDatabaseName(name);
    }
}