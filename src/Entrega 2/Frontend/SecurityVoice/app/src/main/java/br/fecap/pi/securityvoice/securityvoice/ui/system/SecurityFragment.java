package br.fecap.pi.securityvoice.securityvoice.ui.system;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.fecap.pi.securityvoice.R;

public class SecurityFragment extends Fragment {


    public SecurityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, //Carrega o fragment com as opções de segurança do usuário
                             Bundle savedInstanceState) { //Obs.: Só o primeiro botão funciona
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_security, container, false);
        return view;
    }
}