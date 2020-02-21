package edu.lawrence.androiddaycare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChildrenActivity extends AppCompatActivity {
    public final static String USER_ID ="edu.lawrence.AndroidDaycare.LoginActivity.USER_ID" ;
    public final static String PARENT_ID ="edu.lawrence.AndroidDaycare.ParentActivity.PARENT_ID" ;
    public final static String CHILDREN_ID="edu.lawrence.AndroidDaycare.ChildrenActivity.CHILDREN_ID" ;
    private Gson gson;
    private String userID,parentID, name, parent, birthday, idchi, year, month, date;
    private Date bir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        Intent intent = getIntent();
        idchi=intent.getStringExtra(ChildrenDashboard.CHILDREN_ID);
        userID = intent.getStringExtra(USER_ID);
        parentID=intent.getStringExtra(PARENT_ID);

        if(idchi == null)
        {name="";birthday="MM/DD/YYYY";}
        else
        {new ChildTask().execute();}

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }
    protected void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);
        arg0.putString(USER_ID, userID);
        arg0.putString(CHILDREN_ID, idchi);
    }


    private class ChildTask extends AsyncTask<Void, Void, String> {
        private String uri;
        ChildTask(){uri = "http://" + URIHandler.hostName + "/children="+idchi;    }/////////////////////////////???????

        @Override
        protected String doInBackground(Void... ignored) {
            return URIHandler.doGet(uri, "") ;
        }
        @Override
        public void onPostExecute(String result)
        {loadChildren(result);}}


    public void loadChildren(String json) {
        Child c=gson.fromJson(json, Child.class);
        // idparent= c.getidparent();
        userID= String.valueOf(c.getId());
        EditText Name =  findViewById(R.id.N);
        Name.setText(c.getName());
        EditText Birthday =  findViewById(R.id.B);
        Birthday.setText("?");
    }


    public void doneChildren(View view) {
        EditText Name =  findViewById(R.id.N);
        String Name1 = Name.getText().toString();
        EditText Birth =  findViewById(R.id.B);
        String birth = Birth.getText().toString();
        String[] separated = birth.split("/");
        month=separated[0];
        date=separated[1];
        year=separated[2];
        String string=year+"-"+month+"-"+date;
        bir=Date.valueOf(string);//converting string into sql date.

        Child chi = new Child();
        chi.setParentID(Integer.parseInt(parentID));
        chi.setName(Name1);
        chi.setBirthday(bir);         }

    private class NewChildTask extends AsyncTask<Void, Void, String> {
        private String uri;
        private String toSend;

        NewChildTask(Child c) {
            uri = "http://" + URIHandler.hostName + "/children";////////////////////////////////
            this.toSend = gson.toJson(c);
        }
        @Override
        protected String doInBackground(Void... ignored) {
            return URIHandler.doPost(uri, toSend);
        }
        @Override
        public void onPostExecute(String result) {
            if ("0".equals(result)) {
                userMessage("failed to login");
            } else {
                GoBack(result);
            }
        }
    }

    private class EditChildTask extends AsyncTask<Void, Void, String> {
        private String uri;
        private String toSend;
        EditChildTask(Child c) {
            uri = "http://" + URIHandler.hostName + "/children?user="+userID;//////////////////////////
            this.toSend = gson.toJson(c);       }
        @Override
        protected String doInBackground(Void... ignored) {
            return URIHandler.doPost(uri, toSend);
        }
        @Override
        public void onPostExecute(String result) {
            if ("0".equals(result)) {
                userMessage("failed to login");
            } else {
                GoBack(result);   }
        }
    }

    private void userMessage(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void GoBack(String userId) {
        Intent intent = new Intent(ChildrenActivity.this, ChildrenActivity.class);
        intent.putExtra(USER_ID, userId);
        startActivity(intent);
    }
}
