package br.fecap.pi.securityvoice.securityvoice.ui.system;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.fecap.pi.securityvoice.R;
import br.fecap.pi.securityvoice.criptography.Criptography;
import br.fecap.pi.securityvoice.entities.SystemAtributes;
import br.fecap.pi.securityvoice.entities.Travel;
import br.fecap.pi.securityvoice.resources.Adapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelListFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppCompatButton refreshButton;
    private List<Travel> listTravel = new ArrayList<>();

    private Adapter adapter;

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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayout.VERTICAL));

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View itemView = rv.findChildViewUnder(e.getX(), e.getY());

                if(itemView != null){
                    int position = rv.getChildAdapterPosition(itemView);
                    Travel travel = Adapter.listTravel.get(position);
                    acceptingTravel(travel);

                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        return view;
    }

    public void refresh(){
        Call<List<Travel>> call = SystemAtributes.apiService.refreshDriverTravel();

        call.enqueue(new Callback<List<Travel>>() {
            @Override
            public void onResponse(Call<List<Travel>> call, Response<List<Travel>> response) {
                if(response.isSuccessful()) {
                    listTravel = Criptography.listTravelDecrypt(response.body());

                    for(Travel t : listTravel){
                        t.setDriverName("NaN");
                    }
                    adapter = new Adapter(listTravel);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(getActivity().getApplicationContext(), "Lista de viagens atualizada com sucesso!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Falha ao carregar solicitações de viagem!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Travel>> call, Throwable throwable) {
                Toast.makeText(getActivity().getApplicationContext(), "Servidor não responde!", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void acceptingTravel(Travel travel){
        SystemAtributes.travel = travel;


        SystemAtributes.travel.setDriverId(SystemAtributes.user.getId());
        System.out.println(travel);
        Travel travelCrypt = Criptography.travelCriptography(SystemAtributes.travel);
        System.out.println(travelCrypt);
        Call<Travel> call = SystemAtributes.apiService.acceptingTravel(travelCrypt);

        call.enqueue(new Callback<Travel>() {
            @Override
            public void onResponse(Call<Travel> call, Response<Travel> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Viagem selecionada com sucesso!", Toast.LENGTH_LONG).show();
                    SystemAtributes.travel = Criptography.travelDecrypt(response.body());
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction(); //Instânciando um objeto de transação de fragments

                    MapFragment mapFragment = new MapFragment();

                    mapFragment.startAcceptingTravel = true;

                    transaction.replace(R.id.nav_host_fragment_activity_main,mapFragment); //Definindo quando layout receberá o fragmento e sobrescrevendo o layout atual
                    transaction.commit();//Efetuando alterações da transação

                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Falha ao aceitar viagem!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Travel> call, Throwable throwable) {
                Toast.makeText(getActivity().getApplicationContext(), "Servidor não responde!", Toast.LENGTH_LONG).show();
            }
        });
    }
}