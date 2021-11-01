package com.streamlet.db.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.streamlet.database.R;
import com.streamlet.db.sys.SQLExport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Linxz
 * 创建日期：2021年11月01日 4:53 下午
 * version：
 * 描述：
 */
public class TableHomeFragment extends BaseFragment{

    private RecyclerView rvTables;
    private RVAdapter adapter;
    private View contentView;
    @SuppressLint("InflateParams")
    @Override
    public View initLayout(@NonNull LayoutInflater inflater) {
        contentView = inflater.inflate(R.layout.frag_table_home,null);
        rvTables = contentView.findViewById(R.id.rvTables);
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvTables.setLayoutManager(layoutManager);
        adapter = new RVAdapter();
        rvTables.setAdapter(adapter);
        if (!TextUtils.isEmpty(dbName)){
            adapter.refreshData(SQLExport.getInstance(getActivity(),dbName).queryTables());
        }
        initTabView();
    }

    private final List<BaseFragment> fragments = new ArrayList<>();
    private void initTabView(){
        TextView tvStruct = contentView.findViewById(R.id.tvStruct);
        TextView tvData = contentView.findViewById(R.id.tvData);
        tvStruct.setOnClickListener(v -> {
            replaceFragment(fragments.get(0));
        });
        tvData.setOnClickListener(v -> {
            replaceFragment(fragments.get(1));
        });
        fragments.add(new TableStructFragment());
        fragments.add(new TableDataFragment());
        replaceFragment(fragments.get(0));
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragContainer, fragment);
        transaction.commit();
    }

    @Override
    public void refreshDatabaseName(String name) {
        super.refreshDatabaseName(name);
        if (adapter!=null){
            adapter.refreshData(SQLExport.getInstance(getActivity(),dbName).queryTables());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private class RVAdapter extends RecyclerView.Adapter<RVHolder>{
        private final List<String> tables = new ArrayList<>();
        private int selectPos = -1;
        private OnItemClickListener onItemClickListener;

        public void refreshData(List<String> tables){
            this.tables.clear();
            this.tables.addAll(tables);
            notifyDataSetChanged();
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_table_home,parent,false);
            return new RVHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RVHolder holder, int position) {
            TextView textView =  holder.itemView.findViewById(R.id.tvTableName);
            textView.setText(tables.get(position));
            if (position == selectPos){
                textView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()),R.color.light_blue));
            }else{
                textView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()),R.color.black));
            }
            textView.setOnClickListener(v -> {
                if (onItemClickListener!=null){
                    onItemClickListener.onClick(tables.get(position));
                    notifyItemChanged(position);
                    setCurrentTab(position);
                }
            });
        }

        private void setCurrentTab(int position){
            this.selectPos = position;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return tables.size();
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.onItemClickListener = onItemClickListener;
        }
    }

    private static class RVHolder extends RecyclerView.ViewHolder{
        public RVHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private interface OnItemClickListener{
        void onClick(String dbName);
    }
}