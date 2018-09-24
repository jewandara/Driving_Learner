package com.eweo.sandes.andlearns;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivityTuter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuter);

        //START ASYNC TASK
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }



    public class AsyncTaskRunner extends AsyncTask<String, String, String>
    {
        //DEFINE APPLICATION SHARED PREFERENCES DATA
        final private TextView viewLoadingText = (TextView) findViewById(R.id.textViewTuter);
        SharedPreferences shared_preferences = getSharedPreferences("com", Context.MODE_PRIVATE);
        String UsID = shared_preferences.getString("Authentication_UserID","ERROR_SESION").toString();
        String FilNam = shared_preferences.getString("Authentication_FullName","ERROR_SESION").toString();

        //DEFINE GRID VIEW DATA
        final GridView grid = (GridView) findViewById(R.id.grid);
        String[] scheduleTextID;
        String[] scheduleTextSub;
        String[] scheduleTextMain;
        String[] scheduleData;
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
                final String SOAP_ACTION = "http://tempuri.org/viewScheduleDataForTuter";
                final String METHOD_NAME = "viewScheduleDataForTuter";
                final String NAMESPACE = "http://tempuri.org/";
                final String URL = "http://account.maxford.lk/Server/WebServiceSpecialistLearnerDrive.asmx";
                publishProgress("Connecting . . .");

                //SENDING SERVER DATA
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("TUTERID", UsID);
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
                    scheduleTextID = new String[result.getPropertyCount()];
                    scheduleTextSub = new String[result.getPropertyCount()];
                    scheduleTextMain = new String[result.getPropertyCount()];
                    scheduleData = new String[result.getPropertyCount()];
                    imageId = new int[result.getPropertyCount()];

                    for(int i=0;i<result.getPropertyCount();i++)
                    {
                        SoapObject objectResult =(SoapObject) result.getProperty(i);
                        SoapObject objectResultSub =(SoapObject) objectResult.getProperty("VehicleName");
                        imageId[i] = R.drawable.schedulicon;
                        scheduleTextID[i] = objectResult.getProperty("ScheduleID").toString();
                        scheduleTextMain[i] = objectResult.getProperty("SchDate").toString() + "  at  " + objectResult.getProperty("SchStartTime").toString();// + " in " + objectResult.getProperty("SchLocation").toString();
                        scheduleTextSub[i] = objectResult.getProperty("SchLocation").toString();
                        scheduleData[i] = objectResultSub.getProperty("VehicleNumber").toString() + " / " + objectResultSub.getProperty("VehicleName").toString();
                    }
                    adapter = new CustomGrid(MainActivityTuter.this, imageId, scheduleData, scheduleTextMain, scheduleTextSub);
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
                        Toast.makeText(MainActivityTuter.this, "Opening Schedule : " + scheduleTextID[+position]  + "\n" + scheduleTextMain[+position], Toast.LENGTH_SHORT).show();
                        Intent nt = new Intent(getBaseContext(), PageTuterStudent.class);
                        nt.putExtra("SCH_ID",scheduleTextID[+position].toString());
                        startActivity(nt);
                    }
                });
                viewLoadingText.setText(FilNam);
            }
            else if(RESULT.equals("BODY")) { viewLoadingText.setText("Invalid Username and Password"); }
            else if(RESULT.equals("NET")) { viewLoadingText.setText("Connection Error Found"); }
            else { viewLoadingText.setText(RESULT);}
        }

    }


}