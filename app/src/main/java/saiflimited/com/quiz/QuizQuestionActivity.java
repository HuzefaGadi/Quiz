package saiflimited.com.quiz;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class QuizQuestionActivity extends AppCompatActivity {

    Button submit;
    RadioGroup radioGroup;
    TextView questionNumber;
    String username;
    String currentQuestionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        submit = (Button) findViewById(R.id.submit);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        questionNumber = (TextView) findViewById(R.id.questionNumber);
        username = getIntent().getStringExtra(Constants.USERNAME);
        callWebServiceUtilityForActiveQuestion();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRadioButton = radioGroup.getCheckedRadioButtonId();
                String selectedRadioButtonString = null;
                switch (selectedRadioButton) {
                    case R.id.radioButton1:
                        selectedRadioButtonString = "A";
                        break;
                    case R.id.radioButton2:
                        selectedRadioButtonString = "B";
                        break;
                    case R.id.radioButton3:
                        selectedRadioButtonString = "C";
                        break;
                    case R.id.radioButton4:
                        selectedRadioButtonString = "D";
                        break;
                    default:
                        selectedRadioButtonString = null;
                        break;
                }
                if (selectedRadioButtonString == null) {
                    Toast.makeText(QuizQuestionActivity.this, "Please select One Answer", Toast.LENGTH_LONG).show();
                } else {
                    callWebServiceUtility();
                }
            }
        });

    }


    public void callWebServiceUtility() {
        new CallWebService().execute();
    }

    public void callWebServiceUtilityForActiveQuestion() {
        new CallWebServiceForActiveQuestion().execute();
    }

    class CallWebService extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... voids) {

            URL url = null;
            try {
                String urlString = Constants.ANSWER_URL + "?username=" + username + "&ques_id=" + voids[0] + "&answer=" + voids[1];
                url = new URL(urlString);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(1000 * 60);
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String jsonResultLine = reader.readLine();
                return jsonResultLine;
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(QuizQuestionActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait..");
            progressDialog.setTitle("Initializing");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                if (progressDialog != null || progressDialog.isShowing()) {
                    progressDialog.hide();
                }

                if (result != null) {

                    if (result.equalsIgnoreCase("TRUE")) {
                        Toast.makeText(QuizQuestionActivity.this, "ANSWER SAVED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(QuizQuestionActivity.this, "ISSUE WITH SERVER, TRY AGAIN", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    class CallWebServiceForActiveQuestion extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Void... voids) {

            URL url = null;
            try {
                String urlString = Constants.ACTIVE_QUESTION_URL;
                url = new URL(urlString);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(1000 * 60);
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String jsonResultLine = reader.readLine();
                return jsonResultLine;
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(QuizQuestionActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait..");
            progressDialog.setTitle("Initializing");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {

                if (progressDialog != null || progressDialog.isShowing()) {
                    progressDialog.hide();
                }
                if (result != null) {
                    currentQuestionNumber = result;
                    questionNumber.setText(currentQuestionNumber);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
