package edu.lawrence.androiddaycare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

public class ChildrenDashboard extends AppCompatActivity {
    public final static String USER_ID = "com.example.samsung1.hw6.Login.USER_ID";
    public final static String CHILDREN_ID = "com.example.samsung1.hw6.ChildrenProfile.CHILDREN_ID";
    public final static String PARENT_ID = "com.example.samsung1.hw6.ParentProfiles.PARENT_ID";
    private Child[] chi = null;
    private int selected_children = -1;
    private Gson gson;
    private String userID, parentID;

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
        new ChildTask().execute();
    }
    @Override
    protected void onResume() {
        super.onResume();
        new ChildTask().execute();
    }
    @Override
    protected void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);
        arg0.putString(LoginActivity.USER_ID, userID);
    }
    private class ChildTask extends AsyncTask<Void, Void, String> {
        private String uri;

        ChildTask() {
            uri = "http://" + URIHandler.hostName + "/children?parent=" + parentID;
        }///////////////////////////////do i need this children task?

        @Override
        protected String doInBackground(Void... ignored) {
            return URIHandler.doGet(uri, "");
        }

        @Override
        protected void onPostExecute(String result) { loadChildrens(result);   }
    }

    private void loadChildrens(String json) {
        chi = null;
        String[] childrenStrs = null;

        ListView List = (ListView) findViewById(R.id.L);

        chi = gson.fromJson(json,Child[].class);//list[]--> server
        if(chi!=null)
        {
            childrenStrs = new String[chi.length];
            for(int n = 0;n < childrenStrs.length;n++) {
                Child child = chi[n];
                childrenStrs[n] = child.getName();      }//make the list

            //listening for click
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, childrenStrs);
            List.setAdapter(adapter);

            List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selected_children = i;
                }
            });}
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

    public void GoRegistry(View view) {
        if (selected_children != -1) {
            Child c = chi[selected_children];
            Intent intent = new Intent(this, registration.class);
            intent.putExtra(USER_ID, userID);
            intent.putExtra(CHILDREN_ID, c.getId());
            startActivity(intent);
        }
    }
}