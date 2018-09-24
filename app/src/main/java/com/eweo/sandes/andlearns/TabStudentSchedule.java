package com.eweo.sandes.andlearns;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class TabStudentSchedule extends Fragment {

    //DEFINE GRID VIEW DATA
    GridView grid;
    String[] scheduleTextID;
    String[] scheduleTextSub;
    String[] scheduleTextMain;
    String[] scheduleData;
    int[] imageId;
    CustomGrid adapter;
    View rootView;
    String UsID;
    TextView viewLoadingText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_student_schedule, container, false);
        SharedPreferences prfs = this.getActivity().getSharedPreferences("com", Context.MODE_PRIVATE);
        UsID = prfs.getString("Authentication_UserID","ERROR_SESION").toString();
        grid = (GridView) rootView.findViewById(R.id.grid);
        viewLoadingText = (TextView) rootView.findViewById(R.id.textViewLoading);

        //START ASYNC TASK
        TabStudentSchedule.AsyncTaskRunner runner = new TabStudentSchedule.AsyncTaskRunner();
        runner.execute();

        return rootView;
    }

    public class AsyncTaskRunner extends AsyncTask<String, String, String>
    {
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
                final String SOAP_ACTION = "http://tempuri.org/viewScheduleDataForStudent";
                final String METHOD_NAME = "viewScheduleDataForStudent";
                final String NAMESPACE = "http://tempuri.org/";
                final String URL = "http://account.maxford.lk/Server/WebServiceSpecialistLearnerDrive.asmx";
                publishProgress("Connecting . . .");

                //SENDING SERVER DATA
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("STUDENTID", UsID);
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
                    adapter = new CustomGrid(getContext(), imageId, scheduleData, scheduleTextMain, scheduleTextSub);
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
                        Toast.makeText(getActivity(), "Schedule : " + scheduleTextID[+position]  + "\n" + scheduleTextMain[+position], Toast.LENGTH_SHORT).show();
                        viewLoadingText.setText("Schedule : " + scheduleTextID[+position]);
                    }
                });
            }
            else { viewLoadingText.setText(RESULT);}
        }

    }

}