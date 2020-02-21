package edu.lawrence.androiddaycare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.sql.Date;

public class RegistrationActivity extends AppCompatActivity {
    String id, position, idchi;
    String sm, sd, sy, em, ed, ey;
    public final static String POSITION="com.example.samsung1.hw6.ChildrenProfile.POSITION";
    public final static String USER_ID ="com.example.samsung1.hw6.Login.USER_ID" ;
    public final static String PARENT_ID ="com.example.samsung1.hw6.ParentProfiles.PARENT_ID" ;
    private Provider[] p = null;
    private int selected_p = -1;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            id = savedInstanceState.getString(ChildrenDashboard.USER_ID);
            idchi=savedInstanceState.getString(ChildrenDashboard.CHILDREN_ID);
        } else {
            Intent intent = getIntent();
            id = intent.getStringExtra(LoginActivity.USER_ID);
            idchi=intent.getStringExtra(ChildrenDashboard.CHILDREN_ID);
        }
        setContentView(R.layout.activity_registration);
        gson = new Gson();
        //  new RegistrationTask().execute();
    }
    @Override
    protected void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);
        arg0.putString(LoginActivity.USER_ID, id);
    }

    public void search(View view) {
        EditText Start =  findViewById(R.id.start);
        String s = Start.getText().toString();
        EditText End =  findViewById(R.id.end);
        String e = End.getText().toString();
        String[] separated = s.split("/");
        sm=separated[0];
        sd=separated[1];
        sy=separated[2];
        String startdate=sy+"-"+sm+"-"+sd;

        String[] eee = e.split("/");
        em=eee[0];
        ed=eee[1];
        ey=eee[2];
        String enddate=ey+"-"+em+"-"+ed;

        ////////////get children info and do math to get minage and maxage after registrated///////////
    }
    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
    private void getchildren(String json)
    {   Child[] chi = gson.fromJson(json,Child[].class);//list[]--> server
        String[] childrenStrs = new String[chi.length];
        int idchil=Integer.parseInt(idchi);
        Date birth;
        for(int n = 0;n!= idchil;n++) {
            Child child = chi[n];
            birth= convertJavaDateToSqlDate(child.getBirthday());  }

    }}
