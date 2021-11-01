package com.streamlet.db.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.streamlet.database.R;
import com.streamlet.db.client.DataBaseHelper;
import com.streamlet.db.sys.SQLExport;
import java.util.ArrayList;
import java.util.List;

public class SQLExportActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView tvDataBaseName;
    private RecyclerView rvList;
    private RVAdapter rvAdapter;
    private BaseFragment tableHomeFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_sql_export);
        drawerLayout = findViewById(R.id.drawerLayout);
        tvDataBaseName = findViewById(R.id.tvDataBaseName);
        rvList = findViewById(R.id.rvDB);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvAdapter = new RVAdapter();
        rvList.setAdapter(rvAdapter);


        initBottomSheetDialog();
        findViewById(R.id.btnSQL).setOnClickListener(v -> {
            bottomSheetDialog.show();
        });
        rvAdapter.setOnItemClickListener(dbName -> {

        });
        rvAdapter.refreshData(SQLExport.getDBList(this));
        tableHomeFragment = new TableHomeFragment();
        if (rvAdapter.dbs.size()>0){
            tableHomeFragment.refreshDatabaseName(rvAdapter.dbs.get(0));
        }
        replaceFragment();
    }


    private void replaceFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragContainer, tableHomeFragment);
        transaction.commit();
        if (rvAdapter.dbs.size()>0){
            tableHomeFragment.refreshDatabaseName(rvAdapter.dbs.get(0));
        }
    }


    private BottomSheetDialog bottomSheetDialog;
    private void initBottomSheetDialog(){
        bottomSheetDialog = new BottomSheetDialog(SQLExportActivity.this);
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_sql,null);
        bottomSheetDialog.setContentView(dialogView);
        EditText edtSQL = dialogView.findViewById(R.id.edtSQL);
        Button btnExeCuteSQL = dialogView.findViewById(R.id.btnExeCuteSQL);
        TextView tvSQLResult = dialogView.findViewById(R.id.tvSQLResult);
        btnExeCuteSQL.setOnClickListener(v -> {
        });
    }

    public void createDataBaseClick(View view){
        DialogHelper.showCreateDialog(this, dbName -> {
            DataBaseHelper.createDataBase(this,dbName);
            rvAdapter.refreshData(SQLExport.getDBList(this));
        });
    }

    public void openCreateTableClick(View view){
        drawerLayout.openDrawer(Gravity.RIGHT);
    }

    @SuppressLint("NotifyDataSetChanged")
    private class RVAdapter extends RecyclerView.Adapter<RVHolder>{

        private List<String> dbs = new ArrayList<>();
        private int selectPos = -1;
        private OnItemClickListener onItemClickListener;

        public void refreshData(List<String> tables){
            this.dbs.clear();
            this.dbs.addAll(tables);
            if (this.dbs.size()>0){
                selectPos = 0;
            }
            notifyDataSetChanged();
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(SQLExportActivity.this).inflate(R.layout.item_sql_export,parent,false);
            return new RVHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RVHolder holder, int position) {
          TextView textView =  holder.itemView.findViewById(R.id.tvItem);
          textView.setText(dbs.get(position));
          if (position == selectPos){
              textView.setBackgroundResource(R.drawable.rect_light_blue);
          }else{
              textView.setBackgroundResource(R.drawable.rect_white);
          }
          textView.setOnClickListener(v -> {
              if (onItemClickListener!=null){
                  onItemClickListener.onClick(dbs.get(position));
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
            return dbs.size();
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
