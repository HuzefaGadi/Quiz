package saiflimited.com.quiz;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by huzefaasger on 20-01-2016.
 */
public class WebServiceUtility {

    public WebServiceUtility()
    {
        new CallWebService().execute();
    }

    class CallWebService extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            URL url = null;
            try {
                url = new URL("http://statigsoft.com/QuizBuzz/login.php?username=user1&password=password1");

                HttpURLConnection conn =(HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(1000*60);
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String jsonResultLine = reader.readLine();
                System.out.println("RESPONSE FOR QUIZ "+jsonResultLine);
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
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
