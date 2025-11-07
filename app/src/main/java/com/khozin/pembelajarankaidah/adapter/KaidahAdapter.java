package com.khozin.pembelajarankaidah.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter untuk menampilkan daftar lengkap materi kaidah
 * Digunakan di KaidahListFragment
 */
public class KaidahAdapter extends RecyclerView.Adapter<KaidahAdapter.ViewHolder> {

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
                .inflate(R.layout.item_kaidah, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MateriKaidah kaidah = kaidahList.get(position);
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
        private MaterialCardView cardKaidah;
        private ImageView ivIcon;
        private TextView tvJudul;
        private TextView tvDeskripsi;
        private TextView tvProgress;
        private LinearProgressIndicator progressBar;
        private TextView tvStatus;
        private LinearLayout llActions;
        private View rootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardKaidah = itemView.findViewById(R.id.cardKaidah);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvProgress = itemView.findViewById(R.id.tvProgress);
            progressBar = itemView.findViewById(R.id.progressBar);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            llActions = itemView.findViewById(R.id.llActions);
            rootView = itemView;
        }

        public void bind(MateriKaidah kaidah) {
            // Set judul kaidah
            tvJudul.setText(kaidah.getJudulKaidah());

            // Set deskripsi
            String deskripsi = kaidah.getDeskripsi();
            if (deskripsi != null && !deskripsi.trim().isEmpty()) {
                tvDeskripsi.setText(deskripsi);
                tvDeskripsi.setVisibility(View.VISIBLE);
            } else {
                tvDeskripsi.setVisibility(View.GONE);
            }

            // Set progress
            float progress = kaidah.getPersentasePenguasaan();
            tvProgress.setText((int) progress + "%");
            progressBar.setProgress((int) progress);

            // Set status dan warna
            String status = kaidah.getStatus();
            if (status == null || status.equals("belum_dimulai")) {
                tvStatus.setText("Belum Dimulai");
                tvStatus.setTextColor(rootView.getContext().getResources().getColor(R.color.gray_600));
                progressBar.setIndicatorColor(rootView.getContext().getResources().getColor(R.color.gray_400));
            } else if (status.equals("sedang_belajar")) {
                tvStatus.setText("Sedang Belajar");
                tvStatus.setTextColor(rootView.getContext().getResources().getColor(R.color.orange_600));
                progressBar.setIndicatorColor(rootView.getContext().getResources().getColor(R.color.orange_500));
            } else if (status.equals("selesai")) {
                tvStatus.setText("Selesai");
                tvStatus.setTextColor(rootView.getContext().getResources().getColor(R.color.success));
                progressBar.setIndicatorColor(rootView.getContext().getResources().getColor(R.color.success));
            }

            // Set icon berdasarkan tingkat kesulitan
            switch (kaidah.getTingkatKesulitan()) {
                case "mudah":
                    ivIcon.setImageResource(R.drawable.ic_easy);
                    ivIcon.setBackgroundTintList(rootView.getContext().getResources().getColorStateList(R.color.success_light));
                    break;
                case "sedang":
                    ivIcon.setImageResource(R.drawable.ic_medium);
                    ivIcon.setBackgroundTintList(rootView.getContext().getResources().getColorStateList(R.color.orange_light));
                    break;
                case "sulit":
                    ivIcon.setImageResource(R.drawable.ic_hard);
                    ivIcon.setBackgroundTintList(rootView.getContext().getResources().getColorStateList(R.color.error_light));
                    break;
                default:
                    ivIcon.setImageResource(R.drawable.ic_book);
                    ivIcon.setBackgroundTintList(rootView.getContext().getResources().getColorStateList(R.color.primary_green_light));
                    break;
            }

            // Set card click listener
            cardKaidah.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onKaidahClick(kaidah);
                }
            });

            // Set background card stroke berdasarkan status
            if (status != null && status.equals("selesai")) {
                cardKaidah.setStrokeWidth(2);
                cardKaidah.setStrokeColor(rootView.getContext().getResources().getColor(R.color.success));
            } else {
                cardKaidah.setStrokeWidth(1);
                cardKaidah.setStrokeColor(rootView.getContext().getResources().getColor(R.color.gray_200));
            }
        }
    }
}