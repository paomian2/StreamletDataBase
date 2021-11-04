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
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Linxz
 * 创建日期：2021年11月03日 5:05 下午
 * version：
 * 描述：
 */
public class TableIndexesFragment extends BaseFragment {

    private RecyclerView rvConstraint;
    private Adapter adapter;

    @SuppressLint("InflateParams")
    @Override
    public View initLayout(@NonNull LayoutInflater inflater) {
        return inflater.inflate(R.layout.frag_table_constraint, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI(){
        rvConstraint = findViewById(R.id.rvConstraint);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvConstraint.setLayoutManager(layoutManager);
        adapter = new Adapter();
        rvConstraint.setAdapter(adapter);
        List<HashMap<String ,Object>> constraintList  = SQLExport.getInstance(getContext(),dbName).getAllIndexesInfoList(tableName);
        adapter.refreshData(constraintList);
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final List<HashMap<String, Object>> entityList = new ArrayList<>();

        @SuppressLint("NotifyDataSetChanged")
        public void refreshData(List<HashMap<String, Object>> list) {
            entityList.clear();
            list.add(0, null);
            entityList.addAll(list);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.item_table_data_wrapper, parent, false);
            return new AttrsAdapterHolder(headerView);
        }

        @SuppressLint({"InflateParams", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (position ==0){
                //标题
                LinearLayout layoutRows = holder.itemView.findViewById(R.id.layoutDataRow);
                layoutRows.removeAllViews();
                for (int i = 0;i<3;i++){
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.item_constraint_row_item, null);
                    TextView titleName = view.findViewById(R.id.tvName);
                    switch (i){
                        case 0:
                            titleName.setText("索引名称");
                            titleName.setBackgroundResource(R.drawable.attrs_border_left);
                            break;
                        case 1:
                            titleName.setText("是否UNIQUE");
                            titleName.setBackgroundResource(R.drawable.attrs_border_center);
                            break;
                        case 2:
                            titleName.setText("字段");
                            titleName.setBackgroundResource(R.drawable.attrs_border_right);
                            break;
                        default:
                            break;
                    }
                    layoutRows.addView(view);
                }
                return;
            }
            LinearLayout layoutRows = holder.itemView.findViewById(R.id.layoutDataRow);
            HashMap<String, Object> rowsData = entityList.get(position);
            List<HashMap<String, Object>> infoList = (List<HashMap<String, Object>>) rowsData.get("info");
            String ids = "";
            if (infoList != null){
                List<String> idsList = infoList.stream().map(e->e.get("name")+"").collect(Collectors.toList());
                ids = String.join(",",idsList);
            }
            layoutRows.removeAllViews();
            for (int i = 0;i<3;i++){
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_constraint_row_item, null);
                TextView tvName = view.findViewById(R.id.tvName);
                switch (i){
                    case 0:
                        tvName.setText(rowsData.get("name")+"");
                        tvName.setBackgroundResource(R.drawable.attrs_border_left);
                        break;
                    case 1:
                        tvName.setText("1".equals(rowsData.get("unique")+"")?"是":"否");
                        tvName.setBackgroundResource(R.drawable.attrs_border_center);
                        break;
                    case 2:
                        tvName.setText(ids);
                        tvName.setBackgroundResource(R.drawable.attrs_border_right);
                        break;
                    default:
                        break;
                }
                layoutRows.addView(view);
            }
            holder.itemView.findViewById(R.id.viewEndLine).setVisibility(position == entityList.size() - 1 ? View.VISIBLE : View.GONE);
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