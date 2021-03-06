package edu.lawrence.androiddaycare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ParentActivity extends AppCompatActivity {
    private Gson gson;
    private String userID;
    private String makeNew;
    private int parentID;
    public final static String PARENT_ID ="edu.lawrence.AndroidDaycare.USER_ID.PARENT_ID" ;
    public final static String USER_ID = "edu.lawrence.AndroidDaycare.USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            userID = savedInstanceState.getString(LoginActivity.USER_ID);
            makeNew = savedInstanceState.getString("makeNew");
        } else {
            Intent intent = getIntent();
            userID = intent.getStringExtra(LoginActivity.USER_ID);
            makeNew = intent.getStringExtra("makeNew");
        }
        setContentView(R.layout.activity_parent);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        if (makeNew.equals("NO")) {
            new LoadTask().execute();
        }
    }

    public void create(View view) {
        EditText nameField = (EditText) findViewById(R.id.nameField);
        String name = nameField.getText().toString();
        EditText phoneField = (EditText) findViewById(R.id.phoneField);
        String phone = phoneField.getText().toString();
        EditText addressField = (EditText) findViewById(R.id.addressField);
        String address = addressField.getText().toString();
        EditText cityField = (EditText) findViewById(R.id.cityField);
        String city = cityField.getText().toString();
        EditText emailField = (EditText) findViewById(R.id.emailField);
        String email = emailField.getText().toString();
        Parent newParent = new Parent();
        newParent.setName(name);
        newParent.setPhone(phone);
        newParent.setAddress(address);
        newParent.setCity(city);
        newParent.setEmail(email);
        newParent.setUser(Integer.parseInt(userID));
        if (makeNew.equals("YES")) {
            new NewParentTask(newParent).execute();
        }
        if ((makeNew.equals("NO"))) {
            new EditParentTask(newParent).execute();
        }
    }

    private class NewParentTask extends AsyncTask<Void, Void, String> {
        private String uri;
        private String toSend;

        NewParentTask(Parent m) {
            uri = "http://" + URIHandler.hostName + "/parent/";
            this.toSend = gson.toJson(m);
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

    private class EditParentTask extends AsyncTask<Void, Void, String> {
        private String uri;
        private String toSend;

        EditParentTask(Parent m) {
            uri = "http://" + URIHandler.hostName + "/parent/";
            m.setId(parentID);
            this.toSend = gson.toJson(m);
        }

        @Override
        protected String doInBackground(Void... voids) { return URIHandler.doPut(uri, toSend);
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

    private class LoadTask extends AsyncTask<Void, Void, String> {
        private String uri;

        LoadTask() {
            uri = "http://" + URIHandler.hostName + "/parent?user=" + userID;
        }

        @Override
        protected String doInBackground(Void... ignored) {
            return URIHandler.doGet(uri, "");
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            loadParent(result);
        }
    }

    public void loadParent(String json) {
        Parent p = gson.fromJson(json, Parent.class);
        EditText nameField = (EditText) findViewById(R.id.nameField);
        nameField.setText(p.getName());
        EditText phoneField = (EditText) findViewById(R.id.phoneField);
        phoneField.setText(p.getPhone());
        EditText addressField = (EditText) findViewById(R.id.addressField);
        addressField.setText(p.getAddress());
        EditText cityField = (EditText) findViewById(R.id.cityField);
        cityField.setText(p.getCity());
        EditText emailField = (EditText) findViewById(R.id.emailField);
        emailField.setText(p.getEmail());
        parentID = p.getId();
    }
    private void GoDashboard(String parentID) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(USER_ID, userID);
        intent.putExtra(PARENT_ID, parentID);
        startActivity(intent);
    }
    private void userMessage(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}


