package com.example.irhabi_ecsboard.sendbird.anggota;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.irhabi_ecsboard.sendbird.R;
import com.example.irhabi_ecsboard.sendbird.model.User;

import java.util.ArrayList;
import java.util.List;

public class AnggotaAdapter extends  RecyclerView.Adapter<AnggotaAdapter.MyViewHolder>
        implements Filterable{

    private List<User>anggotaFiltered;
    private List<User>Listdata;
    private AnggotaListener listener;
    private Context contex;

    public AnggotaAdapter(Context context, ArrayList<User> anggota, AnggotaListener listener) {
        this.contex = context;
        this.Listdata =anggota;
        this.listener = listener;
        this.anggotaFiltered = anggota;
    }

    @Override
    public AnggotaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_anggota_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AnggotaAdapter.MyViewHolder holder, int position) {
        final User data = anggotaFiltered.get(position);
        data.getId();
        holder.nama.setText(data.getName());
        holder.phone.setText(data.getUsername());
        if (data.getStatus().equals("aktif")){
            holder.aktif.setVisibility(View.VISIBLE);
            holder.aktif.setText(data.getStatus());
        } else if (data.getStatus().equals("pending")){
            holder.pending.setVisibility(View.VISIBLE);
            holder.pending.setText(data.getStatus());
        }
    }

    @Override
    public int getItemCount() {

        if(anggotaFiltered == null){
            return 0;
        }else{
            return anggotaFiltered.size();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if(charString.isEmpty()){
                    anggotaFiltered = Listdata;
                }else{
                    List<User> filteredList =new ArrayList<>();
                    for (User row : Listdata){
                        //menemukan kata pencarian
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    anggotaFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = anggotaFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                anggotaFiltered = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nama, phone, aktif, pending;

        public MyViewHolder(View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phonenumber);
            aktif = itemView.findViewById(R.id.statusaktif);
            pending = itemView.findViewById(R.id.nonaktif);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAnggotaSelected(anggotaFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface AnggotaListener{
        void onAnggotaSelected(User user);
    }
}
