package com.eweo.sandes.andlearns;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class PageTuterProgress extends AppCompatActivity {

    String value;
    String[] stringRatingOfData = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuter_progress_page);

        addListenerOnRatingBar();

        Button btBack = (Button) findViewById(R.id.buttonGoBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView viewLoadingText = (TextView) findViewById(R.id.textViewSubTitle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("PROGRESS_ID");
            viewLoadingText.setText(value);
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute();
        }

    }

    public class AsyncTaskRunner extends AsyncTask<String, String, String>
    {
        final TextView viewLoadingText = (TextView) findViewById(R.id.textViewSubTitle);
        final RatingBar ratingBarTime = (RatingBar) findViewById(R.id.ratingBarTime);
        final RatingBar ratingBarStart = (RatingBar) findViewById(R.id.ratingBarStart);
        final RatingBar ratingBarGass = (RatingBar) findViewById(R.id.ratingBarGass);
        final RatingBar ratingBarBrake = (RatingBar) findViewById(R.id.ratingBarBrake);
        final RatingBar ratingBarNuter = (RatingBar) findViewById(R.id.ratingBarNuter);
        final RatingBar ratingBarGier = (RatingBar) findViewById(R.id.ratingBarGier);
        final RatingBar ratingBarSteringWheel = (RatingBar) findViewById(R.id.ratingBarSteringWheel);
        final RatingBar ratingBarSpeed = (RatingBar) findViewById(R.id.ratingBarSpeed);
        final RatingBar ratingBarStop = (RatingBar) findViewById(R.id.ratingBarStop);
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
                final String SOAP_ACTION = "http://tempuri.org/viewProgressDataForTuter";
                final String METHOD_NAME = "viewProgressDataForTuter";
                final String NAMESPACE = "http://tempuri.org/";
                final String URL = "http://account.maxford.lk/Server/WebServiceSpecialistLearnerDrive.asmx";
                publishProgress("Connecting . . .");

                //SENDING SERVER DATA
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("PROGRESID", value);
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
                viewLoadingText.setText(pType);
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

    public class AsyncTaskRunnerUpdate extends AsyncTask<String, String, String>
    {
        final TextView viewLoadingText = (TextView) findViewById(R.id.textViewSubTitle);

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
                final String SOAP_ACTION = "http://tempuri.org/updateProgressInTuter";
                final String METHOD_NAME = "updateProgressInTuter";
                final String NAMESPACE = "http://tempuri.org/";
                final String URL = "http://account.maxford.lk/Server/WebServiceSpecialistLearnerDrive.asmx";
                publishProgress("Connecting . . .");

                //SENDING SERVER DATA
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("PROGRESID", value);
                request.addProperty("PTYPE", "UPDATED");
                request.addProperty("PTIME", stringRatingOfData[0]);
                request.addProperty("PSTART", stringRatingOfData[1]);
                request.addProperty("PGUESS", stringRatingOfData[2]);
                request.addProperty("PBRAKE", stringRatingOfData[3]);
                request.addProperty("PNUTER", stringRatingOfData[4]);
                request.addProperty("PGIER", stringRatingOfData[5]);
                request.addProperty("PStWheel", stringRatingOfData[6]);
                request.addProperty("PSPEED", stringRatingOfData[7]);
                request.addProperty("PSTOP", stringRatingOfData[8]);
                request.addProperty("PUPDATE", "8/8/2016");
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
                    return result.toString();
                }
                else { return "BODY"; }
            }
            catch (Exception e) { return  e.toString(); }
        }

        @Override
        protected void onPostExecute(String RESULT) {
            if(RESULT.equals(value)) {
                viewLoadingText.setText("New Data Updated");
            }
            else if(RESULT.equals("BODY")) { viewLoadingText.setText("Invalid Username and Password"); }
            else if(RESULT.equals("NET")) { viewLoadingText.setText("Connection Error Found"); }
            else { viewLoadingText.setText("New Data Updated");}
        }

    }

    public void addListenerOnRatingBar() {

        Button btSave = (Button) findViewById(R.id.buttonSave);
        final RatingBar ratingBarTime = (RatingBar) findViewById(R.id.ratingBarTime);
        final RatingBar ratingBarStart = (RatingBar) findViewById(R.id.ratingBarStart);
        final RatingBar ratingBarGass = (RatingBar) findViewById(R.id.ratingBarGass);
        final RatingBar ratingBarBrake = (RatingBar) findViewById(R.id.ratingBarBrake);
        final RatingBar ratingBarNuter = (RatingBar) findViewById(R.id.ratingBarNuter);
        final RatingBar ratingBarGier = (RatingBar) findViewById(R.id.ratingBarGier);
        final RatingBar ratingBarSteringWheel = (RatingBar) findViewById(R.id.ratingBarSteringWheel);
        final RatingBar ratingBarSpeed = (RatingBar) findViewById(R.id.ratingBarSpeed);
        final RatingBar ratingBarStop = (RatingBar) findViewById(R.id.ratingBarStop);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringRatingOfData[0] = String.valueOf(ratingBarTime.getRating()*20);
                stringRatingOfData[1] = String.valueOf(ratingBarStart.getRating()*20);
                stringRatingOfData[2] = String.valueOf(ratingBarGass.getRating()*20);
                stringRatingOfData[3] = String.valueOf(ratingBarBrake.getRating()*20);
                stringRatingOfData[4] = String.valueOf(ratingBarNuter.getRating()*20);
                stringRatingOfData[5] = String.valueOf(ratingBarGier.getRating()*20);
                stringRatingOfData[6] = String.valueOf(ratingBarSteringWheel.getRating()*20);
                stringRatingOfData[7] = String.valueOf(ratingBarSpeed.getRating()*20);
                stringRatingOfData[8] = String.valueOf(ratingBarStop.getRating()*20);
                AsyncTaskRunnerUpdate runnerUp = new AsyncTaskRunnerUpdate();
                runnerUp.execute();
            }
        });

    }

}