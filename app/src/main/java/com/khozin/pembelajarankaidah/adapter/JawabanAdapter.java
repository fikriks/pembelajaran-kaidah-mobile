package com.khozin.pembelajarankaidah.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.data.model.Jawaban;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter untuk menampilkan pilihan jawaban
 * Digunakan di QuizActivity
 */
public class JawabanAdapter extends RecyclerView.Adapter<JawabanAdapter.ViewHolder> {

    private List<Jawaban> jawabanList = new ArrayList<>();
    private OnJawabanClickListener listener;
    private int selectedJawabanId = -1;

    public interface OnJawabanClickListener {
        void onJawabanClick(Jawaban jawaban);
    }

    public void setOnJawabanClickListener(OnJawabanClickListener listener) {
        this.listener = listener;
    }

    public void setSelectedJawaban(int jawabanId) {
        this.selectedJawabanId = jawabanId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jawaban, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Jawaban jawaban = jawabanList.get(position);
        holder.bind(jawaban, selectedJawabanId);
    }

    @Override
    public int getItemCount() {
        return jawabanList.size();
    }

    public void updateData(List<Jawaban> newList) {
        this.jawabanList.clear();
        this.jawabanList.addAll(newList);
        notifyDataSetChanged();
    }

    public Jawaban getItemAt(int position) {
        return jawabanList.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardJawaban;
        private TextView tvOptionLetter;
        private TextView tvJawaban;
        private ImageView ivCheck;
        private View rootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardJawaban = itemView.findViewById(R.id.cardJawaban);
            tvOptionLetter = itemView.findViewById(R.id.tvOptionLetter);
            tvJawaban = itemView.findViewById(R.id.tvJawaban);
            ivCheck = itemView.findViewById(R.id.ivCheck);
            rootView = itemView;
        }

        public void bind(Jawaban jawaban, int selectedId) {
            // Set option letter based on urutan
            String optionLetter = getOptionLetter(jawaban.getUrutan());
            tvOptionLetter.setText(optionLetter);

            // Set text jawaban
            tvJawaban.setText(jawaban.getJawaban());

            // Check if this is selected
            boolean isSelected = (jawaban.getIdPilihan() == selectedId);

            // Update UI based on selection
            if (isSelected) {
                // Selected state
                cardJawaban.setStrokeWidth(3);
                cardJawaban.setStrokeColor(rootView.getContext().getResources().getColor(R.color.primary_green));
                cardJawaban.setCardBackgroundColor(rootView.getContext().getResources().getColor(R.color.primary_green_light));
                tvOptionLetter.setTextColor(rootView.getContext().getResources().getColor(R.color.primary_green));
                tvJawaban.setTextColor(rootView.getContext().getResources().getColor(R.color.primary_green_dark));
                ivCheck.setVisibility(View.VISIBLE);
                ivCheck.setImageResource(R.drawable.ic_check_circle);
                ivCheck.setTint(rootView.getContext().getResources().getColor(R.color.primary_green));
            } else {
                // Normal state
                cardJawaban.setStrokeWidth(1);
                cardJawaban.setStrokeColor(rootView.getContext().getResources().getColor(R.color.gray_300));
                cardJawaban.setCardBackgroundColor(rootView.getContext().getResources().getColor(R.color.background_card));
                tvOptionLetter.setTextColor(rootView.getContext().getResources().getColor(R.color.text_primary));
                tvJawaban.setTextColor(rootView.getContext().getResources().getColor(R.color.text_primary));
                ivCheck.setVisibility(View.GONE);
            }

            // Set click listener
            cardJawaban.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onJawabanClick(jawaban);
                }
            });

            // Add ripple effect
            cardJawaban.setRippleColor(rootView.getContext().getResources().getColorStateList(R.color.primary_green_light));
        }

        /**
         * Get option letter from urutan (1=A, 2=B, 3=C, 4=D)
         */
        private String getOptionLetter(int urutan) {
            switch (urutan) {
                case 1: return "A";
                case 2: return "B";
                case 3: return "C";
                case 4: return "D";
                default: return String.valueOf((char) ('A' + urutan - 1));
            }
        }
    }
}