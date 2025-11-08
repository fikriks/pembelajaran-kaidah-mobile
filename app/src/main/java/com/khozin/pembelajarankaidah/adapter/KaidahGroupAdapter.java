package com.khozin.pembelajarankaidah.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import android.widget.ProgressBar;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.data.model.KaidahGroup;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter untuk menampilkan daftar kaidah yang dikelompokkan per bab
 * Menggunakan Expandable/Collapsible pattern untuk setiap bab
 */
public class KaidahGroupAdapter extends RecyclerView.Adapter<KaidahGroupAdapter.GroupViewHolder> {

    private List<KaidahGroup> groupList = new ArrayList<>();
    private OnKaidahGroupClickListener listener;
    private OnKaidahClickListener kaidahClickListener;

    public interface OnKaidahGroupClickListener {
        void onGroupClick(KaidahGroup group);
        void onGroupExpand(KaidahGroup group, boolean expand);
    }

    public interface OnKaidahClickListener {
        void onKaidahClick(MateriKaidah kaidah);
    }

    public void setOnKaidahGroupClickListener(OnKaidahGroupClickListener listener) {
        this.listener = listener;
    }

    public void setOnKaidahClickListener(OnKaidahClickListener listener) {
        this.kaidahClickListener = listener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kaidah_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        KaidahGroup group = groupList.get(position);
        holder.bind(group, listener, kaidahClickListener);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void updateData(List<KaidahGroup> newGroups) {
        this.groupList.clear();
        this.groupList.addAll(newGroups);
        notifyDataSetChanged();
    }

    public void addData(List<KaidahGroup> newGroups) {
        int startPos = this.groupList.size();
        this.groupList.addAll(newGroups);
        notifyItemRangeInserted(startPos, newGroups.size());
    }

    public KaidahGroup getItemAt(int position) {
        return groupList.get(position);
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardGroup;
        private TextView tvNomorBab;
        private TextView tvJudulBab;
        private TextView tvDeskripsiBab;
        private TextView tvProgress;
        private ProgressBar progressBar;
        private TextView tvStatus;
        private TextView tvKaidahCount;
        private RecyclerView rvKaidahList;
        private View rootView;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            cardGroup = itemView.findViewById(R.id.cardGroup);
            tvNomorBab = itemView.findViewById(R.id.tvNomorBab);
            tvJudulBab = itemView.findViewById(R.id.tvJudulBab);
            tvDeskripsiBab = itemView.findViewById(R.id.tvDeskripsiBab);
            tvProgress = itemView.findViewById(R.id.tvProgress);
            progressBar = itemView.findViewById(R.id.progressBar);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvKaidahCount = itemView.findViewById(R.id.tvKaidahCount);
            rvKaidahList = itemView.findViewById(R.id.rvKaidahList);
            rootView = itemView;
        }

        public void bind(KaidahGroup group, OnKaidahGroupClickListener groupListener,
                        OnKaidahClickListener kaidahListener) {
            // Set bab information
            tvNomorBab.setText(group.getNomorBab());
            tvJudulBab.setText(group.getNamaBabSaja());

            // Set deskripsi
            String deskripsi = group.getDeskripsiBab();
            if (deskripsi != null && !deskripsi.trim().isEmpty()) {
                tvDeskripsiBab.setText(deskripsi);
                tvDeskripsiBab.setVisibility(View.VISIBLE);
            } else {
                tvDeskripsiBab.setVisibility(View.GONE);
            }

            // Set progress
            int progress = group.getProgressPercentage();
            tvProgress.setText(progress + "%");
            progressBar.setProgress(progress);

            // Set status dan warna
            String statusText = group.getStatusText();
            tvStatus.setText(statusText);

            if (progress >= 100) {
                tvStatus.setTextColor(rootView.getContext().getResources().getColor(R.color.success));
            } else if (progress > 0) {
                tvStatus.setTextColor(rootView.getContext().getResources().getColor(R.color.orange_600));
            } else {
                tvStatus.setTextColor(rootView.getContext().getResources().getColor(R.color.gray_600));
            }

            // Set kaidah count
            tvKaidahCount.setText(group.getTotalKaidah() + " kaidah");

            // Setup kaidah list (always visible now)
            setupKaidahList(group, kaidahListener);

            // Set group click listener
            cardGroup.setOnClickListener(v -> {
                if (groupListener != null) {
                    groupListener.onGroupClick(group);
                }
            });

            // Set card stroke based on progress
            if (progress >= 100) {
                cardGroup.setStrokeWidth(2);
                cardGroup.setStrokeColor(rootView.getContext().getResources().getColor(R.color.success));
            } else {
                cardGroup.setStrokeWidth(1);
                cardGroup.setStrokeColor(rootView.getContext().getResources().getColor(R.color.gray_200));
            }
        }

        private void setupKaidahList(KaidahGroup group, OnKaidahClickListener kaidahListener) {
            if (group.isEmpty()) {
                rvKaidahList.setVisibility(View.GONE);
                return;
            }

            // Create adapter for inner kaidah list
            KaidahListItemAdapter adapter = new KaidahListItemAdapter(group.getKaidahList());
            adapter.setOnKaidahClickListener(new KaidahListItemAdapter.OnKaidahClickListener() {
                @Override
                public void onKaidahClick(MateriKaidah kaidah) {
                    if (kaidahClickListener != null) {
                        kaidahClickListener.onKaidahClick(kaidah);
                    }
                }
            });

            // Setup RecyclerView
            rvKaidahList.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
            rvKaidahList.setAdapter(adapter);
            rvKaidahList.setNestedScrollingEnabled(false);
            rvKaidahList.setVisibility(View.VISIBLE); // Always visible
        }
    }

    /**
     * Inner adapter untuk menampilkan kaidah di dalam setiap group
     */
    private static class KaidahListItemAdapter extends RecyclerView.Adapter<KaidahListItemAdapter.KaidahViewHolder> {

        private List<MateriKaidah> kaidahList;
        private OnKaidahClickListener listener;

        public interface OnKaidahClickListener {
            void onKaidahClick(MateriKaidah kaidah);
        }

        public KaidahListItemAdapter(List<MateriKaidah> kaidahList) {
            this.kaidahList = kaidahList;
        }

        public void setOnKaidahClickListener(OnKaidahClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public KaidahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_kaidah_small, parent, false);
            return new KaidahViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull KaidahViewHolder holder, int position) {
            MateriKaidah kaidah = kaidahList.get(position);
            holder.bind(kaidah, listener);
        }

        @Override
        public int getItemCount() {
            return kaidahList != null ? kaidahList.size() : 0;
        }

        static class KaidahViewHolder extends RecyclerView.ViewHolder {
            private TextView tvJudulKaidah;
            private TextView tvDeskripsi;
            private TextView tvStatus;
            private ImageView ivStatus;
            private View rootView;

            public KaidahViewHolder(@NonNull View itemView) {
                super(itemView);
                tvJudulKaidah = itemView.findViewById(R.id.tvKaidahTitle);
                tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                ivStatus = itemView.findViewById(R.id.ivKaidahIcon);
                rootView = itemView;
            }

            public void bind(MateriKaidah kaidah, OnKaidahClickListener listener) {
                // Set judul
                tvJudulKaidah.setText(kaidah.getJudulKaidah());

                // Set deskripsi
                String deskripsi = kaidah.getDeskripsi();
                if (deskripsi != null && !deskripsi.trim().isEmpty()) {
                    tvDeskripsi.setText(deskripsi);
                    tvDeskripsi.setVisibility(View.VISIBLE);
                } else {
                    tvDeskripsi.setVisibility(View.GONE);
                }

                // Set status
                String status = kaidah.getStatus();
                if (status == null || status.equals("belum_dimulai")) {
                    tvStatus.setText("Belum Dimulai");
                    ivStatus.setImageResource(R.drawable.ic_circle);
                    ivStatus.setColorFilter(rootView.getContext().getResources().getColor(R.color.gray_400));
                } else if (status.equals("sedang_belajar")) {
                    tvStatus.setText("Sedang Belajar");
                    ivStatus.setImageResource(R.drawable.ic_clock);
                    ivStatus.setColorFilter(rootView.getContext().getResources().getColor(R.color.orange_500));
                } else if (status.equals("selesai")) {
                    tvStatus.setText("Selesai");
                    ivStatus.setImageResource(R.drawable.ic_check_circle);
                    ivStatus.setColorFilter(rootView.getContext().getResources().getColor(R.color.success));
                }

                // Set click listener
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onKaidahClick(kaidah);
                    }
                });
            }
        }
    }
}