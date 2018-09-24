package com.eweo.sandes.andlearns;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    //DEFINE MAIN
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //DEFINE LOGIN BUTTON
        Button bt = (Button) findViewById(R.id.buttonLogin);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
            }
        });

    }

    public class AsyncTaskRunner extends AsyncTask<String, String, String>
    {
        private TextView viewLoadingText = (TextView) findViewById(R.id.textViewLoading);
        private EditText userNameLearner = (EditText) findViewById(R.id.editTextUserName);
        private EditText passWordLearner = (EditText) findViewById(R.id.editTextPassWord);
        private String textUserNameLearner = userNameLearner.getText().toString();
        private String textPassWordLearner = passWordLearner.getText().toString();

        @Override
        protected void onPreExecute() { }

        @Override
        protected void onProgressUpdate(String... text)  { viewLoadingText.setText(text[0]); }

        @Override
        protected String doInBackground(String... params)
        {
            publishProgress("Connecting . . .");
            try {

                //CALLING SERVER FUNCTION
                final String SOAP_ACTION = "http://tempuri.org/loginUserProfile";
                final String METHOD_NAME = "loginUserProfile";
                final String NAMESPACE = "http://tempuri.org/";
                final String URL = "http://account.maxford.lk/Server/WebServiceSpecialistLearnerDrive.asmx";
                publishProgress("Calling . . .");

                //SENDING SERVER DATA
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("USERNAME", textUserNameLearner);
                request.addProperty("PASSWORD", textPassWordLearner);
                publishProgress("Sending . . .");

                //CREATE ENVELOPE
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                publishProgress("Creating . . .");

                //CATCH EXCEPTIONS
                try { androidHttpTransport.call(SOAP_ACTION, envelope); }
                catch (IOException e) { e.printStackTrace(); }
                catch (XmlPullParserException e) { e.printStackTrace(); }
                publishProgress("Catching . . .");

                if (envelope.bodyIn != null)
                {
                    SoapObject result = (SoapObject) envelope.getResponse();
                    publishProgress("Updating . . .");
                    SharedPreferences shared_preferences = getSharedPreferences("com", Context.MODE_PRIVATE);
                    SharedPreferences.Editor shared_editor = shared_preferences.edit();
                    shared_editor.putString("Authentication_UserType",result.getProperty("UserType").toString());
                    shared_editor.putString("Authentication_UserID", result.getProperty("UserID").toString());
                    shared_editor.putString("Authentication_FullName", result.getProperty("FullName").toString());
                    shared_editor.putString("Authentication_Pakage", result.getProperty("Pakage").toString());
                    shared_editor.putString("Authentication_Gender",result.getProperty("Gender").toString());
                    shared_editor.putString("Authentication_BithDay", result.getProperty("BithDay").toString());
                    shared_editor.putString("Authentication_NatiolPasPortID", result.getProperty("NatiolPasPortID").toString());
                    shared_editor.putString("Authentication_ContactNumber", result.getProperty("ContactNumber").toString());
                    shared_editor.putString("Authentication_Other", result.getProperty("Other").toString());
                    shared_editor.putString("Authentication_InsertedDate", result.getProperty("InsertedDate").toString());
                    shared_editor.apply();
                    return result.getProperty("UserType").toString();
                }
                else { return "BODY"; }
            }
            catch (Exception e) { return  e.toString(); }
        }

        @Override
        protected void onPostExecute(String RESULT) {
            if(RESULT.equals("student"))
            {
                Intent nt = new Intent(getBaseContext(), MainActivityStudent.class);
                finish();
                startActivity(nt);
            }
            else if(RESULT.equals("tuter"))
            {
                Intent nt = new Intent(getBaseContext(), MainActivityTuter.class);
                finish();
                startActivity(nt);
            }
            else if(RESULT.equals("BODY")) { viewLoadingText.setText("Invalid Username and Password"); }
            else if(RESULT.equals("NET")) { viewLoadingText.setText("Connection Error Found"); }
            else { viewLoadingText.setText(RESULT); }
        }

    }

}
