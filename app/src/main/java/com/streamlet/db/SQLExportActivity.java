package com.streamlet.db;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class SQLExportActivity extends AppCompatActivity {

    private TextView tvDataBaseName;
    private RecyclerView rvList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sql_export);
        tvDataBaseName = findViewById(R.id.tvDataBaseName);
        rvList = findViewById(R.id.rvTable);
        RVAdapter rvAdapter = new RVAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(rvAdapter);
        rvAdapter.addData(SQLExport.getDBList(this));


        findViewById(R.id.btnSQL).setVisibility(View.GONE);
    }





    private class RVAdapter extends RecyclerView.Adapter<RVHolder>{

        private List<String> dbs = new ArrayList<>();

        public void addData(List<String> tables){
            this.dbs.addAll(tables);
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
          textView.setOnClickListener(v -> {
              Intent intent = new Intent(SQLExportActivity.this,TableActivity.class);
              intent.putExtra("dbName",dbs.get(position));
              SQLExportActivity.this.startActivity(intent);
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
