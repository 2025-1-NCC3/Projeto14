package br.fecap.pi.securityvoice.securityvoice.ui.system;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.fecap.pi.securityvoice.R;
import br.fecap.pi.securityvoice.entities.SystemAtributes;


public class ProfileFragment extends Fragment {



    public ProfileFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, //Carrega o fragmento com as opções do perfil do usuário
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView nameProfile = view.findViewById(R.id.nameProfile);
        nameProfile.setText(SystemAtributes.user.getName());

        return view;
    }
}