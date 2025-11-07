package com.khozin.pembelajarankaidah.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter untuk menampilkan daftar kaidah dalam ukuran kecil
 * Digunakan di HomeFragment untuk "Recent Kaidah"
 */
public class KaidahSmallAdapter extends RecyclerView.Adapter<KaidahSmallAdapter.ViewHolder> {

    private List<MateriKaidah> kaidahList = new ArrayList<>();
    private OnKaidahClickListener listener;

    public interface OnKaidahClickListener {
        void onKaidahClick(MateriKaidah kaidah);
    }

    public void setOnKaidahClickListener(OnKaidahClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kaidah_small, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MateriKaidah kaidah = kaidahList.get(position);
        holder.setOnKaidahClickListener(listener);
        holder.bind(kaidah);
    }

    @Override
    public int getItemCount() {
        return kaidahList.size();
    }

    public void updateData(List<MateriKaidah> newList) {
        this.kaidahList.clear();
        this.kaidahList.addAll(newList);
        notifyDataSetChanged();
    }

    public void addData(List<MateriKaidah> newData) {
        int startPos = this.kaidahList.size();
        this.kaidahList.addAll(newData);
        notifyItemRangeInserted(startPos, newData.size());
    }

    public MateriKaidah getItemAt(int position) {
        return kaidahList.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivKaidahIcon;
        private TextView tvKaidahTitle;
        private TextView tvProgress;
        private TextView tvStatus;
        private View rootView;
        private OnKaidahClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivKaidahIcon = itemView.findViewById(R.id.ivKaidahIcon);
            tvKaidahTitle = itemView.findViewById(R.id.tvKaidahTitle);
            tvProgress = itemView.findViewById(R.id.tvProgress);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            rootView = itemView;
        }

        public void bind(MateriKaidah kaidah) {
            // Set judul kaidah
            tvKaidahTitle.setText(kaidah.getJudulKaidah());

            // Set progress text
            tvProgress.setText(kaidah.getPersentasePenguasaan() + "%");

            // Set status text
            String status = kaidah.getStatus();
            if (status == null || status.equals("belum_dimulai")) {
                tvStatus.setText("Belum dimulai");
            } else if (status.equals("sedang_belajar")) {
                tvStatus.setText("Sedang belajar");
            } else if (status.equals("selesai")) {
                tvStatus.setText("Selesai");
            }

            // Set icon berdasarkan tingkat kesulitan
            switch (kaidah.getTingkatKesulitan()) {
                case "mudah":
                    ivKaidahIcon.setImageResource(R.drawable.ic_easy);
                    break;
                case "sedang":
                    ivKaidahIcon.setImageResource(R.drawable.ic_medium);
                    break;
                case "sulit":
                    ivKaidahIcon.setImageResource(R.drawable.ic_hard);
                    break;
                default:
                    ivKaidahIcon.setImageResource(R.drawable.ic_book);
                    break;
            }

            // Set click listener
            rootView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onKaidahClick(kaidah);
                }
            });
        }

        public void setOnKaidahClickListener(OnKaidahClickListener listener) {
            this.listener = listener;
        }
    }
}