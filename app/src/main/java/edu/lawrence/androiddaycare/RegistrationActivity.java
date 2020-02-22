package edu.lawrence.androiddaycare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity {
    String position, idchi;
    String sm, sd, sy, em, ed, ey;
    private int selected_provider = -1;
    private Provider[] p = null;
    public final static String POSITION = "com.example.samsung1.hw6.ChildrenDashboard.POSITION";
    public final static String USER_ID = "edu.lawrence.AndroidDaycare.LoginActivity.USER_ID";
    public final static String PARENT_ID = "edu.lawrence.AndroidDaycare.ParentActivity.PARENT_ID";
    private int selected_p = -1;
    private String userID;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            userID = savedInstanceState.getString(LoginActivity.USER_ID);
            idchi = savedInstanceState.getString(ChildrenDashboard.CHILDREN_ID);
        } else {
            Intent intent = getIntent();
            userID = intent.getStringExtra(LoginActivity.USER_ID);
            idchi = intent.getStringExtra(ChildrenDashboard.CHILDREN_ID);
        }
        setContentView(R.layout.activity_registration);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        //  new RegistrationTask().execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);
        arg0.putString(LoginActivity.USER_ID, userID);
    }

    protected void register(View view) throws ParseException {
        Provider selected = p[selected_provider];
        EditText Start = findViewById(R.id.start);
        String s = Start.getText().toString();
        EditText End = findViewById(R.id.end);
        String e = End.getText().toString();
        String[] separated = s.split("/");
        sm = separated[0];
        sd = separated[1];
        sy = separated[2];
        String startdate = sy + "-" + sm + "-" + sd;
        Date start=new SimpleDateFormat("yyyy-MM-dd").parse(startdate);

        String[] eee = e.split("/");
        em = eee[0];
        ed = eee[1];
        ey = eee[2];
        String enddate = ey + "-" + em + "-" + ed;
        Date end=new SimpleDateFormat("yyyy-MM-dd").parse(enddate);
        int status = 1;
        registration newRegis = new registration();
        newRegis.setChildID(Integer.parseInt(idchi));
        newRegis.setProviderID(selected.getId());
        newRegis.setStatus(status);
        newRegis.setStart(start);
        newRegis.setEnd(end);
        new NewRegistrationTask(newRegis).execute();
        }
    private class NewRegistrationTask extends AsyncTask<Void, Void, String> {
        private String uri;
        private String toSend;

        NewRegistrationTask(registration r) {
            uri = "http://" + URIHandler.hostName + "/registration/";
            this.toSend = gson.toJson(r);
            finish();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return URIHandler.doPost(uri, toSend);
        }
        @Override
        public void onPostExecute(String result) {
            if ("0".equals(result)) {
                userMessage("fail");
            } else {
                GoDashboard(result);
            }
        }
    }



    public void search(View view) {
        EditText Start = findViewById(R.id.start);
        String s = Start.getText().toString();
        EditText End = findViewById(R.id.end);
        String e = End.getText().toString();
        String[] separated = s.split("/");
        sm = separated[0];
        sd = separated[1];
        sy = separated[2];
        String startdate = sy + "-" + sm + "-" + sd;

        String[] eee = e.split("/");
        em = eee[0];
        ed = eee[1];
        ey = eee[2];
        String enddate = ey + "-" + em + "-" + ed;
        new SearchTask(startdate, enddate).execute();

    }

    private class SearchTask extends AsyncTask<Void, Void, String> {
        private String uri;
        private String toSend;

        SearchTask(String start, String end) {
            uri = "http://" + URIHandler.hostName + "/provider?start=" + start + "&end=" + end + "&id=" + idchi;
        }

        @Override
        protected String doInBackground(Void... ignored) {
            return URIHandler.doGet(uri, "");
        }

        @Override
        public void onPostExecute(String result) {
            loadProviders(result);
        }}

        private void loadProviders(String json) {
            p = null;
            String[] providersStrs = null;

            ListView List = (ListView) findViewById(R.id.L2);

            p = gson.fromJson(json, Provider[].class);//list[]--> server
            if (p != null) {
                providersStrs = new String[p.length];
                for (int n = 0; n < providersStrs.length; n++) {
                    Provider provid = p[n];
                    providersStrs[n] = provid.getName();
                }//make the list

                //listening for click
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, providersStrs);
                List.setAdapter(adapter);

                List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int i, long l) {
                        // remember the selection
                        selected_provider = i;
                    }
                });
            }
        }



    ////////////get children info and do math to get minage and maxage after registrated///////////




    private void userMessage(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void GoDashboard(String parentID) {
        Intent intent = new Intent(this, ChildrenDashboard.class);
        intent.putExtra(LoginActivity.USER_ID, userID);
        intent.putExtra(ParentActivity.PARENT_ID, parentID);
        intent.putExtra(ChildrenActivity.CHILDREN_ID, idchi);
        startActivity(intent);
    }
}