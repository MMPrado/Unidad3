package mx.edu.utng.mmacias.gallerymayra;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Comment extends AppCompatActivity implements View.OnClickListener  {
    private Button btnSave;
    private Button btnShow;

    private EditText edtcomments;
    private ListView lsvComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnShow = (Button) findViewById(R.id.btn_show);
        edtcomments = (EditText) findViewById(R.id.edt_comments);

        lsvComments = findViewById(R.id.lsv_comments);
        btnSave.setOnClickListener(this);
        btnShow.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                SaveWSTask saveWSTask = new SaveWSTask();
                saveWSTask.execute(edtcomments.getText().toString());
                break;
            case R.id.btn_show:
                ShowWSTask showWSTask = new ShowWSTask();
                showWSTask.execute(edtcomments.getText().toString());
                break;

            default:
                break;
        }
    }

    private class ShowWSTask extends AsyncTask<String, Integer, Boolean> {
        private String[] comments;

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean result = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://192.168.0.16:3000/apps.json");
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse response = httpClient.execute(get);
                String strResponse = EntityUtils.toString(response.getEntity());
                JSONArray jsonResponse = new JSONArray(strResponse);
                comments = new String[jsonResponse.length()];
                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject jsonObject = jsonResponse.getJSONObject(i);
                    int idcommets = jsonObject.getInt("id");
                    String data = jsonObject.getString("data");

                    comments[i] = "" + idcommets + "-" + data;
                }
            } catch (Exception e) {
                Log.e("Rest Service Error", "Error!!", e);
                result = false;
            }
            return result;
        }

        @Override
        public void onPostExecute(Boolean result) {
            if (result) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Comment.this,
                        android.R.layout.simple_list_item_1, comments);
                lsvComments.setAdapter(arrayAdapter);
            }
        }
    }
    private class SaveWSTask extends AsyncTask<String, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://192.168.0.16:3000/apps.json");
            post.setHeader("content-type", "application/json");
            try {
                // create the client object format json
                JSONObject datas = new JSONObject();
                datas.put("data", params[0]);
                StringEntity entity = new StringEntity(datas.toString());
                post.setEntity(entity);

                HttpResponse response = httpClient.execute(post);
                String strResponse = EntityUtils.toString(response.getEntity());
                Log.i("Response", strResponse);
                //JSONArray jsonResponse = new JSONArray(strResponse);


            } catch (Exception e) {
                Log.e("Rest Service Error", "Error!!", e);
                result = false;
            }
            return result;
        }

        @Override
        public void onPostExecute(Boolean result) {
            String message = result ? "sucessful" : "Failed";
            Toast.makeText(Comment.this, "Comment " + message, Toast.LENGTH_SHORT).show();

        }
    }
}
