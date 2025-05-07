package br.fecap.pi.securityvoice.resources;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.fecap.pi.securityvoice.R;
import br.fecap.pi.securityvoice.criptography.Criptography;
import br.fecap.pi.securityvoice.entities.SystemAtributes;
import br.fecap.pi.securityvoice.entities.Travel;

import br.fecap.pi.securityvoice.securityvoice.ui.system.TravelListFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter extends RecyclerView.Adapter<Adapter.TravelViewHolder>{

    public static List<Travel> listTravel;

    public Adapter(List<Travel> listTravel){
        this.listTravel = listTravel;
    }

    public class TravelViewHolder extends RecyclerView.ViewHolder{
        TextView destinationTextView,
                originTextView,
                dateTextView,
                custTextView,
                durationTextView,
                nameTextView
        ;

        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);

            destinationTextView = itemView.findViewById(R.id.destination_TextView);
            originTextView = itemView.findViewById(R.id.origin_TextView);
            dateTextView = itemView.findViewById(R.id.date_TextView);
            custTextView = itemView.findViewById(R.id.cust_TextView);
            durationTextView = itemView.findViewById(R.id.duration_TextView);
            nameTextView = itemView.findViewById(R.id.name_TextView);
        }
    }

    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_travel,parent, false);
        return new TravelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        Travel travel = listTravel.get(position);

        holder.destinationTextView.setText(travel.getDestination());
        holder.originTextView.setText(travel.getOrigin());
        holder.dateTextView.setText(travel.getDate());
        holder.custTextView.setText(travel.getCust());
        holder.durationTextView.setText(travel.getDuration());

        if(!SystemAtributes.user.getCpf().equals("NaN")){
            holder.nameTextView.setText(travel.getPassengerName());
        }else{
            holder.nameTextView.setText(travel.getDriverName());
        }

    }

    @Override
    public int getItemCount() {
        return listTravel.size();
    }

    public List<Travel> getListTravel() {
        return listTravel;
    }

    public void setListTravel(List<Travel> listTravel) {
        this.listTravel = listTravel;
    }
}
