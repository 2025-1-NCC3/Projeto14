package br.fecap.pi.securityvoice.securityvoice.ui.system;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import br.fecap.pi.securityvoice.R;
import br.fecap.pi.securityvoice.entities.SystemAtributes;
import br.fecap.pi.securityvoice.entities.Travel;
import br.fecap.pi.securityvoice.resources.Adapter;
import retrofit2.Call;

public class TravelListFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppCompatButton refreshButton;
    private List<Travel> listTravel = new ArrayList<>();

    public TravelListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_list, container, false);

        recyclerView = view.findViewById(R.id.listTravel_Travel);
        refreshButton = view.findViewById(R.id.refreshButton);

        refresh();

        Adapter adapter = new Adapter(listTravel);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayout.VERTICAL));

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        return view;
    }

    public void refresh(){
        //Call<List<Travel>> call = new SystemAtributes.apiService.

    }
}