package com.eweo.sandes.andlearns;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class PageTuterStudent extends AppCompatActivity {

    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuter_student_page);

        Button btBack = (Button) findViewById(R.id.buttonGoBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("SCH_ID");
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute();
        }
    }

    public class AsyncTaskRunner extends AsyncTask<String, String, String>
    {
        final TextView viewLoadingText = (TextView) findViewById(R.id.textViewSubTitle);
        final GridView grid = (GridView) findViewById(R.id.grid);
        String[] progressTextID;
        String[] progressTextSub;
        String[] progressTextMain;
        String[] progressData;
        int[] imageId;
        CustomGrid adapter;

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
                final String SOAP_ACTION = "http://tempuri.org/viewProgressForTuter";
                final String METHOD_NAME = "viewProgressForTuter";
                final String NAMESPACE = "http://tempuri.org/";
                final String URL = "http://account.maxford.lk/Server/WebServiceSpecialistLearnerDrive.asmx";
                publishProgress("Connecting . . .");

                //SENDING SERVER DATA
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("SCHEDULEID", value);
                publishProgress("Sending . . .");

                //CREATE ENVELOP
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

                //LOADING ENVELOPE
                if (envelope.bodyIn != null)
                {
                    SoapObject result = (SoapObject) envelope.getResponse();
                    progressTextID = new String[result.getPropertyCount()];
                    progressTextSub = new String[result.getPropertyCount()];
                    progressTextMain = new String[result.getPropertyCount()];
                    progressData = new String[result.getPropertyCount()];
                    imageId = new int[result.getPropertyCount()];

                    for(int i=0;i<result.getPropertyCount();i++)
                    {
                        SoapObject objectResult =(SoapObject) result.getProperty(i);
                        imageId[i] = R.drawable.student;
                        progressTextID[i] = objectResult.getProperty("ProgressID").toString();
                        progressTextMain[i] = objectResult.getProperty("pFullName").toString() + "\n" + objectResult.getProperty("pNatiolPasPortID").toString();
                        progressTextSub[i] = objectResult.getProperty("pPakage").toString();
                        progressData[i] = objectResult.getProperty("ProgressID").toString();
                    }
                    adapter = new CustomGrid(PageTuterStudent.this, imageId, progressTextID, progressTextMain, progressTextSub);
                    return "OK";
                }
                else { return "BODY"; }
            }
            catch (Exception e) { return  e.toString(); }
        }

        @Override
        protected void onPostExecute(String RESULT) {
            if(RESULT.equals("OK")) {
                grid.setAdapter(adapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(PageTuterStudent.this, "Opening Student Number : " + progressTextID[+position]  + "\n" + progressTextMain[+position], Toast.LENGTH_SHORT).show();
                        Intent nt = new Intent(getBaseContext(), PageTuterProgress.class);
                        nt.putExtra("PROGRESS_ID",progressTextID[+position].toString());
                        startActivity(nt);
                    }
                });
                viewLoadingText.setText("Students In Schedule : " + value);
            }
            else if(RESULT.equals("BODY")) { viewLoadingText.setText("Invalid Username and Password"); }
            else if(RESULT.equals("NET")) { viewLoadingText.setText("Connection Error Found"); }
            else { viewLoadingText.setText(RESULT);}
        }

    }

}