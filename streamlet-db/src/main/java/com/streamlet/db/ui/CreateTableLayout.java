package com.streamlet.db.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.streamlet.database.R;
import com.streamlet.db.bean.TableAttrs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Linxz
 * 创建日期：2021年11月01日 7:30 下午
 * version：
 * 描述：
 */
public class CreateTableLayout extends FrameLayout {

    public CreateTableLayout(@NonNull Context context) {
        super(context);
    }

    public CreateTableLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CreateTableLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Adapter adapter;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_create_table, this);
        RecyclerView gridAttrs = findViewById(R.id.gridAttrs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        adapter = new Adapter();
        gridAttrs.setLayoutManager(layoutManager);
        gridAttrs.setAdapter(adapter);
    }

    public void update(List<TableAttrs> list) {
        adapter.refreshData(list);
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final List<TableAttrs> entityList = new ArrayList<>();

        @SuppressLint("NotifyDataSetChanged")
        public void refreshData(List<TableAttrs> list) {
            entityList.clear();
            entityList.add(0, null);//头部
            entityList.addAll(list);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.item_create_table, parent, false);
            if (viewType == 0) {
                return new AdapterHeaderHolder(headerView);
            } else {
                return new AttrsAdapterHolder(headerView);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof AttrsAdapterHolder) {
                TextView tvName = holder.itemView.findViewById(R.id.tvName);
                TextView tvType = holder.itemView.findViewById(R.id.tvType);
                TextView tvKey = holder.itemView.findViewById(R.id.tvKey);
                TextView tvForeignKey = holder.itemView.findViewById(R.id.tvForeignKey);
                TextView tvUnique = holder.itemView.findViewById(R.id.tvUnique);
                TextView tvNotNull = holder.itemView.findViewById(R.id.tvNotNull);
                TextView tvDefault = holder.itemView.findViewById(R.id.tvDefault);
                TableAttrs entity = entityList.get(position);
                tvName.setText(entity.getFieldName());
                tvType.setText(entity.getFieldType());
                tvKey.setText(entity.isPrimary() ? "是" : "");
                tvForeignKey.setText("");
                tvUnique.setText(entity.isUnique() ? "是" : "");
                tvNotNull.setText(entity.isNotNull() ? "是" : "");
                tvDefault.setText(entity.getDefValue() != null ? entity.getDefValue().toString() : "NULL");
            }
            if (holder instanceof AttrsAdapterHolder || holder instanceof AdapterHeaderHolder){
                View viewEndLine = holder.itemView.findViewById(R.id.viewEndLine);
                viewEndLine.setVisibility(position == entityList.size()-1?View.VISIBLE:View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return entityList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }
    }

    private static class AttrsAdapterHolder extends RecyclerView.ViewHolder {
        public AttrsAdapterHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private static class AdapterHeaderHolder extends RecyclerView.ViewHolder {
        public AdapterHeaderHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}