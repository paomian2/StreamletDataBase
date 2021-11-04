package com.streamlet.db.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.streamlet.database.R;
import com.streamlet.db.sys.SQLExport;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Linxz
 * 创建日期：2021年11月01日 4:30 下午
 * version：
 * 描述：
 */
public class TableDataFragment extends BaseFragment {

    private RecyclerView rvData;
    private Adapter adapter;

    @SuppressLint("InflateParams")
    @Override
    public View initLayout(@NonNull LayoutInflater inflater) {
        return inflater.inflate(R.layout.frag_table_data, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {
        rvData = findViewById(R.id.rvData);
        adapter = new Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvData.setLayoutManager(layoutManager);
        rvData.setAdapter(adapter);
        List<LinkedHashMap<String,Object>> list= SQLExport.getInstance(getContext(),dbName).queryAll(tableName);
        adapter.refreshData(list);
    }

    @Override
    public void refreshDatabaseName(String name) {
        super.refreshDatabaseName(name);
    }


    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final List<LinkedHashMap<String, Object>> entityList = new ArrayList<>();

        @SuppressLint("NotifyDataSetChanged")
        public void refreshData(List<LinkedHashMap<String, Object>> list) {
            entityList.clear();
            if (list.size()>0){
                list.add(0,list.get(0));
            }
            entityList.addAll(list);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.item_table_data_wrapper, parent, false);
            return new AttrsAdapterHolder(headerView);
        }

        @SuppressLint("InflateParams")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            LinearLayout layoutRows = holder.itemView.findViewById(R.id.layoutDataRow);
            LinkedHashMap<String, Object> rowsData = entityList.get(position);
            List<String> keyList = new ArrayList<>();
            List<String> valueList = new ArrayList<>();
            layoutRows.removeAllViews();
            for (Map.Entry<String, Object> entry : rowsData.entrySet()) {
                keyList.add(entry.getKey());
                valueList.add(entry.getValue() == null ? "NULL" : entry.getValue().toString());
            }
            for (int i = 0; i < keyList.size(); i++) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_table_row_item, null);
                TextView tvName = view.findViewById(R.id.tvName);
                tvName.setText(position == 0 ? keyList.get(i) : valueList.get(i));
                layoutRows.addView(view);
                if (i == 0) {
                    tvName.setBackgroundResource(R.drawable.attrs_border_left);
                } else if (i == entityList.size() - 1) {
                    tvName.setBackgroundResource(R.drawable.attrs_border_right);
                } else {
                    tvName.setBackgroundResource(R.drawable.attrs_border_center);
                }
            }
            holder.itemView.findViewById(R.id.viewEndLine).setVisibility(position == entityList.size()-1?View.VISIBLE:View.GONE);
        }

        @Override
        public int getItemCount() {
            return entityList.size();
        }


    }

    private static class AttrsAdapterHolder extends RecyclerView.ViewHolder {
        public AttrsAdapterHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}