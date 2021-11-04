package com.streamlet.db.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Linxz
 * 创建日期：2021年11月01日 4:42 下午
 * version：
 * 描述：
 */
public abstract class BaseFragment extends Fragment {

    public String dbName;
    public String tableName;
    public View contentView;

    public void refreshDatabaseName(String name) {
        this.dbName = name;
    }

    public void refreshTableName(String dbName,String name) {
        this.dbName = dbName;
        this.tableName = name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = initLayout(inflater);
        return contentView;
    }

    public abstract View initLayout(@NonNull LayoutInflater inflater);

    public <T extends View> T findViewById(@IdRes int id) {
        return contentView.findViewById(id);
    }

}