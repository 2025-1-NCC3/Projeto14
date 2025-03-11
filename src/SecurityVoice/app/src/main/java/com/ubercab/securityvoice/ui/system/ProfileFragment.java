package com.ubercab.securityvoice.ui.system;

import android.accounts.Account;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubercab.securityvoice.MainActivity;
import com.ubercab.securityvoice.R;


public class ProfileFragment extends Fragment {

    public AppCompatButton profileActivityButton;
    public AppCompatButton profileInformationButton;
    public AppCompatButton profileConfigurationButton;
    public AppCompatButton profileExitButton;

    public ProfileFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }
}