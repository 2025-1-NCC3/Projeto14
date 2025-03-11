package com.ubercab.securityvoice.ui.system;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mapbox.maps.MapView;

import com.mapbox.maps.Style;
import com.ubercab.securityvoice.R;


public class TravelFragment extends Fragment {
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travel, container, false);

        // Inicializando o MapView
        mapView = view.findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Libere o MapView se estiver utilizando o Mapbox
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

}