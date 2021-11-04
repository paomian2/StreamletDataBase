package com.streamlet.db.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.streamlet.database.R;
import com.streamlet.db.bean.TableAttrs;
import com.streamlet.db.sys.SQLExport;

import java.util.List;

/**
 * @author Linxz
 * 创建日期：2021年11月01日 4:02 下午
 * version：
 * 描述：
 */
public class TableStructFragment extends BaseFragment {


    private EditText edtTableName;
    private ImageView ivEditColumn;
    private CreateTableLayout layoutTable;
    private View contentView;

    @SuppressLint("InflateParams")
    @Override
    public View initLayout(@NonNull LayoutInflater inflater) {
        contentView = inflater.inflate(R.layout.frag_table_struct,null);
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        refreshTableName(dbName,tableName);
    }

    private void initUI(){
        edtTableName = contentView.findViewById(R.id.edtTableName);
        layoutTable = contentView.findViewById(R.id.layoutTable);
        ivEditColumn = contentView.findViewById(R.id.ivEditColumn);
        ivEditColumn.setOnClickListener(v -> {
            DialogHelper.editTableColumn(getContext(),null);
        });
    }

    @Override
    public void refreshTableName(String dbName,String name) {
        super.refreshTableName(dbName,name);
        if (edtTableName == null) return;
        edtTableName.setText(name);
        //获取表属性
        List<TableAttrs> tableAttrs = SQLExport.getInstance(getContext(),dbName).queryTableStructure(tableName,TableAttrs.class);
        layoutTable.update(tableAttrs);
    }
}