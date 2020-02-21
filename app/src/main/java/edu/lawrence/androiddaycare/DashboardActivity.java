package edu.lawrence.androiddaycare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DashboardActivity extends AppCompatActivity {
    private Gson gson;
    private String userID;
    private String parentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            userID = savedInstanceState.getString(LoginActivity.USER_ID);
        } else {
            Intent intent = getIntent();
            userID = intent.getStringExtra(LoginActivity.USER_ID);
            parentID = intent.getStringExtra(ParentActivity.PARENT_ID);
        }
        setContentView(R.layout.activity_dashboard);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }
    public void newParent(View view) {
        Intent intent = new Intent(this, ParentActivity.class);
        intent.putExtra(LoginActivity.USER_ID, userID);
        intent.putExtra("makeNew", "YES");
        startActivity(intent);
    }
    public void editParent(View view) {
        Intent intent = new Intent(this, ParentActivity.class);
        intent.putExtra(LoginActivity.USER_ID, userID);
        intent.putExtra("makeNew", "NO");
        startActivity(intent);

    }
    public void childrenDashboard(View view) {
        Intent intent = new Intent(this, ChildrenDashboard.class);
        intent.putExtra(LoginActivity.USER_ID, userID);
        intent.putExtra(ParentActivity.PARENT_ID, parentID);
        startActivity(intent);

    }
}
