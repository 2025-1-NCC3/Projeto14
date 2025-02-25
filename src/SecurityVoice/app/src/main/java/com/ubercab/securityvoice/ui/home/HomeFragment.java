package com.ubercab.securityvoice.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.ubercab.securityvoice.R;
import com.ubercab.securityvoice.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
    private GoogleMap googleMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtém o fragmento do mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Adiciona um marcador em uma localização específica
        LatLng location = new LatLng(-23.5505, -46.6333); // São Paulo
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Marker in São Paulo"));

        // Move a câmera para a localização do marcador
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}