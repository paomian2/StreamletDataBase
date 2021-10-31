package com.streamlet.db;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtSQL;
    private Button btnCreateDB;
    private Button btnCreateTable;
    private Button btnNormalSQL;
    private Button btnInsert;
    private Button btnQuery;
    private TextView tvSQLResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtSQL=findViewById(R.id.edtSQL);
        btnCreateDB=findViewById(R.id.btnCreateDB);
        btnCreateTable=findViewById(R.id.btnCreateTable);
        btnNormalSQL=findViewById(R.id.btnNormalSQL);
        btnInsert=findViewById(R.id.btnInsert);
        btnQuery=findViewById(R.id.btnQuery);
        tvSQLResult=findViewById(R.id.tvSQLResult);
        btnCreateDB.setOnClickListener(v ->
           DataBaseHelper.getInstance(MainActivity.this,"xpd")
        );
        btnNormalSQL.setOnClickListener(v -> {
            DataBaseHelper.getInstance(MainActivity.this,"xpd").executeSQL(edtSQL.getText().toString().trim());
        });
        btnCreateTable.setOnClickListener(v ->
            DataBaseHelper.getInstance(MainActivity.this,"xpd").createTable(User.class)
        );
        btnInsert.setOnClickListener(v -> {
            User user = new User();
            user.setId(100);
            user.setName("风满楼");
            DataBaseHelper.getInstance(MainActivity.this,"xpd").insert(user);
        });
        btnQuery.setOnClickListener(v -> {
            List<User> users = DataBaseHelper.getInstance(MainActivity.this,"xpd").queryAll(User.class);
            tvSQLResult.setText(new Gson().toJson(users));
        });
    }
}