package com.eweo.sandes.andlearns;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class TabStudentProgress  extends Fragment {

    String value;
    TextView viewLoadingText;
    RatingBar ratingBarTime, ratingBarStart, ratingBarGass, ratingBarBrake, ratingBarNuter, ratingBarGier, ratingBarSteringWheel, ratingBarSpeed, ratingBarStop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_student_progress, container, false);

        SharedPreferences prfs = this.getActivity().getSharedPreferences("com", Context.MODE_PRIVATE);
        value = prfs.getString("Authentication_UserID","ERROR_SESION").toString();

        viewLoadingText = (TextView) rootView.findViewById(R.id.textViewLoading);
        ratingBarTime = (RatingBar) rootView.findViewById(R.id.ratingBarTime);
        ratingBarStart = (RatingBar) rootView.findViewById(R.id.ratingBarStart);
        ratingBarGass = (RatingBar) rootView.findViewById(R.id.ratingBarGass);
        ratingBarBrake = (RatingBar) rootView.findViewById(R.id.ratingBarBrake);
        ratingBarNuter = (RatingBar) rootView.findViewById(R.id.ratingBarNuter);
        ratingBarGier = (RatingBar) rootView.findViewById(R.id.ratingBarGier);
        ratingBarSteringWheel = (RatingBar) rootView.findViewById(R.id.ratingBarSteringWheel);
        ratingBarSpeed = (RatingBar) rootView.findViewById(R.id.ratingBarSpeed);
        ratingBarStop = (RatingBar) rootView.findViewById(R.id.ratingBarStop);

        //START ASYNC TASK
        TabStudentProgress.AsyncTaskRunner runner = new TabStudentProgress.AsyncTaskRunner();
        runner.execute();

        return rootView;
    }

    public class AsyncTaskRunner extends AsyncTask<String, String, String>
    {

        String pType = "";
        float[] numberOfData = new float[10];

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
                final String SOAP_ACTION = "http://tempuri.org/viewProgressDataForStudent";
                final String METHOD_NAME = "viewProgressDataForStudent";
                final String NAMESPACE = "http://tempuri.org/";
                final String URL = "http://account.maxford.lk/Server/WebServiceSpecialistLearnerDrive.asmx";
                publishProgress("Connecting . . .");

                //SENDING SERVER DATA
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("PROFILEID", value);
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
                    numberOfData[0] = Float.parseFloat(result.getProperty("pTime").toString());
                    numberOfData[1] = Float.parseFloat(result.getProperty("pStart").toString());
                    numberOfData[2] = Float.parseFloat(result.getProperty("pGuess").toString());
                    numberOfData[3] = Float.parseFloat(result.getProperty("pBrake").toString());
                    numberOfData[4] = Float.parseFloat(result.getProperty("pNuter").toString());
                    numberOfData[5] = Float.parseFloat(result.getProperty("pGier").toString());
                    numberOfData[6] = Float.parseFloat(result.getProperty("pSteringWheel").toString());
                    numberOfData[7] = Float.parseFloat(result.getProperty("pSpeed").toString());
                    numberOfData[8] = Float.parseFloat(result.getProperty("pStop").toString());
                    pType = result.getProperty("ProgressType").toString();
                    return "OK";
                }
                else { return "BODY"; }
            }
            catch (Exception e) { return  e.toString(); }
        }

        @Override
        protected void onPostExecute(String RESULT) {
            if(RESULT.equals("OK")) {
                viewLoadingText.setText("Absent Schedules : " + pType);
                ratingBarTime.setRating(numberOfData[0]/20);
                ratingBarStart.setRating(numberOfData[1]/20);
                ratingBarGass.setRating(numberOfData[2]/20);
                ratingBarBrake.setRating(numberOfData[3]/20);
                ratingBarNuter.setRating(numberOfData[4]/20);
                ratingBarGier.setRating(numberOfData[5]/20);
                ratingBarSteringWheel.setRating(numberOfData[6]/20);
                ratingBarSpeed.setRating(numberOfData[7]/20);
                ratingBarStop.setRating(numberOfData[8]/20);
            }
            else if(RESULT.equals("BODY")) { viewLoadingText.setText("Invalid Username and Password"); }
            else if(RESULT.equals("NET")) { viewLoadingText.setText("Connection Error Found"); }
            else { viewLoadingText.setText(RESULT);}
        }

    }

}


