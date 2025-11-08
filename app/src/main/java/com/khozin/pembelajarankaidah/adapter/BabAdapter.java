package com.khozin.pembelajarankaidah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.data.model.Bab;

import java.util.List;
import java.util.Locale;

/**
 * RecyclerView adapter untuk Bab/Chapter items
 * Menampilkan chapter cards dengan progress information
 */
public class BabAdapter extends RecyclerView.Adapter<BabAdapter.BabViewHolder> {

    private List<Bab> babList;
    private Context context;
    private OnChapterClickListener listener;

    public interface OnChapterClickListener {
        void onChapterClick(Bab bab);
        void onChapterLongClick(Bab bab);
    }

    public BabAdapter(Context context, OnChapterClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bab, parent, false);
        return new BabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BabViewHolder holder, int position) {
        Bab bab = babList.get(position);
        holder.bind(bab);
    }

    @Override
    public int getItemCount() {
        return babList != null ? babList.size() : 0;
    }

    public void setBabList(List<Bab> babList) {
        this.babList = babList;
        notifyDataSetChanged();
    }

    public List<Bab> getBabList() {
        return babList;
    }

    public Bab getBabAt(int position) {
        return babList != null ? babList.get(position) : null;
    }

    class BabViewHolder extends RecyclerView.ViewHolder {

        // Main views
        private final TextView tvChapterNumber;
        private final TextView tvChapterName;
        private final TextView tvChapterDescription;
        private final TextView tvProgressText;
        private final TextView tvStatusText;
        private final ProgressBar progressBar;
        private final ImageView ivChapterIcon;
        private final ImageView ivLockIcon;
        private final View cardView;
        private final View progressContainer;

        public BabViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            cardView = itemView.findViewById(R.id.card_bab);
            tvChapterNumber = itemView.findViewById(R.id.tv_chapter_number);
            tvChapterName = itemView.findViewById(R.id.tv_chapter_name);
            tvChapterDescription = itemView.findViewById(R.id.tv_chapter_description);
            tvProgressText = itemView.findViewById(R.id.tv_progress_text);
            tvStatusText = itemView.findViewById(R.id.tv_status_text);
            progressBar = itemView.findViewById(R.id.progress_bar);
            ivChapterIcon = itemView.findViewById(R.id.iv_chapter_icon);
            ivLockIcon = itemView.findViewById(R.id.iv_lock_icon);
            progressContainer = itemView.findViewById(R.id.progress_container);
        }

        public void bind(Bab bab) {
            // Set chapter number
            String chapterNumber = bab.getNomorBab();
            if (chapterNumber != null && !chapterNumber.isEmpty()) {
                tvChapterNumber.setText(chapterNumber);
                tvChapterNumber.setVisibility(View.VISIBLE);
            } else {
                tvChapterNumber.setVisibility(View.GONE);
            }

            // Set chapter name
            tvChapterName.setText(bab.getNamaBabSaja());

            // Set description
            String description = bab.getDeskripsiSingkat();
            if (description != null && !description.isEmpty()) {
                tvChapterDescription.setText(description);
                tvChapterDescription.setVisibility(View.VISIBLE);
            } else {
                tvChapterDescription.setVisibility(View.GONE);
            }

            // Set progress
            int progress = bab.getProgressPercentage();
            progressBar.setMax(100);
            progressBar.setProgress(progress);

            // Set progress text
            String progressText = String.format(Locale.getDefault(), "%d%% (%d/%d)",
                    progress, bab.getCompletedMateri(), bab.getTotalMateri());
            tvProgressText.setText(progressText);

            // Set status text and colors
            String statusText = bab.getDisplayStatus();
            tvStatusText.setText(statusText);

            // Configure appearance based on unlock status
            if (bab.isAccessible()) {
                // Unlocked and active
                ivLockIcon.setVisibility(View.GONE);
                cardView.setAlpha(1.0f);
                progressContainer.setVisibility(View.VISIBLE);

                // Set status color
                int statusColor = getStatusColor(bab.getStatusColor());
                tvStatusText.setTextColor(statusColor);

                // Set icon based on progress
                if (progress >= 100) {
                    ivChapterIcon.setImageResource(R.drawable.ic_check_circle);
                    ivChapterIcon.setColorFilter(context.getResources().getColor(android.R.color.holo_green_dark));
                } else if (progress > 0) {
                    ivChapterIcon.setImageResource(R.drawable.ic_clock);
                    ivChapterIcon.setColorFilter(context.getResources().getColor(android.R.color.holo_orange_dark));
                } else {
                    ivChapterIcon.setImageResource(R.drawable.ic_play_circle);
                    ivChapterIcon.setColorFilter(context.getResources().getColor(android.R.color.holo_blue_dark));
                }
            } else {
                // Locked
                ivLockIcon.setVisibility(View.VISIBLE);
                cardView.setAlpha(0.6f);
                progressContainer.setVisibility(View.GONE);

                ivChapterIcon.setImageResource(R.drawable.ic_lock);
                ivChapterIcon.setColorFilter(context.getResources().getColor(android.R.color.darker_gray));

                tvStatusText.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            }

            // Set click listeners
            cardView.setOnClickListener(v -> {
                if (listener != null && bab.isAccessible()) {
                    listener.onChapterClick(bab);
                } else if (!bab.isAccessible()) {
                    // Show locked message
                    showLockedMessage();
                }
            });

            cardView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onChapterLongClick(bab);
                    return true;
                }
                return false;
            });

            // Animation for appearance
            animateCardAppearance();
        }

        private int getStatusColor(String statusColorName) {
            switch (statusColorName.toLowerCase()) {
                case "success":
                    return context.getResources().getColor(android.R.color.holo_green_dark);
                case "warning":
                    return context.getResources().getColor(android.R.color.holo_orange_dark);
                case "danger":
                    return context.getResources().getColor(android.R.color.holo_red_dark);
                case "info":
                    return context.getResources().getColor(android.R.color.holo_blue_dark);
                default:
                    return context.getResources().getColor(android.R.color.darker_gray);
            }
        }

        private void showLockedMessage() {
            // Show toast or snackbar with locked message
            // Implementation depends on your preferred way of showing messages
        }

        private void animateCardAppearance() {
            cardView.setScaleX(0.8f);
            cardView.setScaleY(0.8f);
            cardView.setAlpha(0.5f);

            cardView.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .alpha(1.0f)
                    .setDuration(300)
                    .start();
        }
    }

    /**
     * Update single item
     */
    public void updateItem(int position, Bab bab) {
        if (babList != null && position < babList.size()) {
            babList.set(position, bab);
            notifyItemChanged(position);
        }
    }

    /**
     * Add item at position
     */
    public void addItem(int position, Bab bab) {
        if (babList != null) {
            babList.add(position, bab);
            notifyItemInserted(position);
        }
    }

    /**
     * Remove item at position
     */
    public void removeItem(int position) {
        if (babList != null && position < babList.size()) {
            babList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Clear all items
     */
    public void clearItems() {
        if (babList != null) {
            int size = babList.size();
            babList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }
}