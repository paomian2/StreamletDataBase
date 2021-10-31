package com.streamlet.db;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.streamlet.db.sys.SQLExport;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    private String dbName;
    private TextView tvDataBaseName;
    private RecyclerView rvList;
    private RVAdapter rvAdapter = new RVAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sql_export);
        tvDataBaseName = findViewById(R.id.tvDataBaseName);
        tvDataBaseName.setText("表列表");
        rvList = findViewById(R.id.rvTable);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(rvAdapter);

        dbName = getIntent().getStringExtra("dbName");
        List<String> tables = SQLExport.getInstance(this,dbName).queryTables();
        rvAdapter.addData(tables);

        initBottomSheetDialog();
        findViewById(R.id.btnSQL).setOnClickListener(v -> {
            bottomSheetDialog.show();
        });
    }

    private BottomSheetDialog bottomSheetDialog;
    private void initBottomSheetDialog(){
        bottomSheetDialog = new BottomSheetDialog(TableActivity.this);
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_sql,null);
        bottomSheetDialog.setContentView(dialogView);
        EditText edtSQL = dialogView.findViewById(R.id.edtSQL);
        Button btnExeCuteSQL = dialogView.findViewById(R.id.btnExeCuteSQL);
        TextView tvSQLResult = dialogView.findViewById(R.id.tvSQLResult);
        btnExeCuteSQL.setOnClickListener(v -> {
            String sql = edtSQL.getText().toString().trim();
            DataBaseHelper.getInstance(TableActivity.this,dbName).executeSQL(sql);

        });
    }


    private class RVAdapter extends RecyclerView.Adapter<RVHolder>{

        private List<String> dbs = new ArrayList<>();

        public void addData(List<String> tables){
            this.dbs.clear();
            this.dbs.addAll(tables);
            notifyDataSetChanged();
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(TableActivity.this).inflate(R.layout.item_sql_export,parent,false);
            return new RVHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RVHolder holder, int position) {
          TextView textView =  holder.itemView.findViewById(R.id.tvItem);
          textView.setText(dbs.get(position));
          textView.setOnClickListener(v -> {

          });
        }

        @Override
        public int getItemCount() {
            return dbs.size();
        }
    }

    private static class RVHolder extends RecyclerView.ViewHolder{
        public RVHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
