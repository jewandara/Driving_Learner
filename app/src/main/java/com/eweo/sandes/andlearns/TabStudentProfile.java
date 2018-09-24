package com.eweo.sandes.andlearns;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabStudentProfile extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_student_profile, container, false);
        SharedPreferences prfs = this.getActivity().getSharedPreferences("com", Context.MODE_PRIVATE);

        TextView retextFullName = (TextView) rootView.findViewById(R.id.textFullName);
        retextFullName.setText(prfs.getString("Authentication_FullName","ERROR_SESION").toString() + "\n----------------------------");

        TextView reUserID = (TextView) rootView.findViewById(R.id.textUserID);
        reUserID.setText("Student ID : " + prfs.getString("Authentication_UserID","ERROR_SESION").toString());

        TextView retextPakage = (TextView) rootView.findViewById(R.id.textPakage);
        retextPakage.setText("Pakage \t\t: " + prfs.getString("Authentication_Pakage","ERROR_SESION").toString());

        TextView retextGendert = (TextView) rootView.findViewById(R.id.textGender);
        retextGendert.setText("Gender \t\t: " + prfs.getString("Authentication_Gender","ERROR_SESION").toString());

        TextView retextBithDay = (TextView) rootView.findViewById(R.id.textBithDay);
        retextBithDay.setText("Birth Day \t: " + prfs.getString("Authentication_BithDay","ERROR_SESION").toString());

        TextView retextNatiolPasPortID = (TextView) rootView.findViewById(R.id.textNatiolPasPortID);
        retextNatiolPasPortID.setText("NIC No \t\t: " + prfs.getString("Authentication_NatiolPasPortID","ERROR_SESION").toString());

        TextView retextContactNumber = (TextView) rootView.findViewById(R.id.textContactNumber);
        retextContactNumber.setText("Contact \t\t: " + prfs.getString("Authentication_ContactNumber","ERROR_SESION").toString());

        TextView retextOther = (TextView) rootView.findViewById(R.id.textOther);
        retextOther.setText("Other \t\t\t: " + prfs.getString("Authentication_Other","ERROR_SESION").toString());

        TextView retextInsertedDater = (TextView) rootView.findViewById(R.id.textInsertedDate);
        retextInsertedDater.setText("Inserted \t\t: " + prfs.getString("Authentication_InsertedDate","ERROR_SESION").toString());

        return rootView;
    }

}


