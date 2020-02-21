package edu.lawrence.androiddaycare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

public class ChildrenDashboard extends AppCompatActivity {
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
        setContentView(R.layout.activity_children_dashboard);
        gson = new Gson();
    }

    public void newChild(View view) {
        Intent intent = new Intent(this, ChildrenActivity.class);
        intent.putExtra(LoginActivity.USER_ID, userID);
        intent.putExtra("makeNew", "YES");
        intent.putExtra(ParentActivity.PARENT_ID, parentID);
        startActivity(intent);
    }

    public void editChild(View view) {
        Intent intent = new Intent(this, ChildrenActivity.class);
        intent.putExtra(LoginActivity.USER_ID, userID);
        intent.putExtra(ParentActivity.PARENT_ID, parentID);
        intent.putExtra("makeNew", "NO");
        startActivity(intent);

    }
}