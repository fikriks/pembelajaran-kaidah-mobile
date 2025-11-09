package com.khozin.pembelajarankaidah.ui.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.data.model.Bab;
import java.util.List;

/**
 * Adapter untuk menampilkan daftar quiz cards secara dinamis
 */
public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private Context context;
    private List<Bab> babList;
    private OnQuizClickListener onQuizClickListener;

    public interface OnQuizClickListener {
        void onQuizClick(Bab bab);
    }

    public QuizAdapter(Context context, List<Bab> babList, OnQuizClickListener onQuizClickListener) {
        this.context = context;
        this.babList = babList;
        this.onQuizClickListener = onQuizClickListener;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_card, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Bab bab = babList.get(position);
        holder.bind(bab);
    }

    @Override
    public int getItemCount() {
        return babList.size();
    }

    public void updateData(List<Bab> newBabList) {
        this.babList = newBabList;
        notifyDataSetChanged();
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {
        private TextView tvQuizTitle;
        private TextView tvQuizSubtitle;
        private ImageView ivQuizIcon;
        private View rootCard;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuizTitle = itemView.findViewById(R.id.tv_quiz_title);
            tvQuizSubtitle = itemView.findViewById(R.id.tv_quiz_subtitle);
            ivQuizIcon = itemView.findViewById(R.id.iv_quiz_icon);
            rootCard = itemView.findViewById(R.id.root_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Bab bab = babList.get(position);
                        if (onQuizClickListener != null) {
                            onQuizClickListener.onQuizClick(bab);
                        }
                    }
                }
            });
        }

        public void bind(Bab bab) {
            tvQuizTitle.setText("Quiz " + bab.getNamaBab());

            // Subtitle berdasarkan status unlock
            String subtitle = "";
            if (bab.isUnlocked()) {
                subtitle = "Semua materi " + bab.getNamaBab() + " telah selesai • Siap untuk quiz!";
            } else {
                subtitle = "Selesaikan semua materi " + bab.getNamaBab() + " terlebih dahulu";
            }
            tvQuizSubtitle.setText(subtitle);

            // Set background card selalu putih
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                rootCard.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    context.getResources().getColor(android.R.color.white)));
            } else {
                rootCard.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            }

            // Icon dan background icon berdasarkan status
            if (bab.isUnlocked()) {
                // Quiz tersedia - gunakan icon success
                ivQuizIcon.setImageResource(R.drawable.ic_quiz_success);
                ivQuizIcon.setBackgroundResource(R.drawable.bg_circle_success_light);
                tvQuizSubtitle.setTextColor(context.getResources().getColor(R.color.success_green));
            } else {
                // Quiz terkunci - gunakan icon locked
                ivQuizIcon.setImageResource(R.drawable.ic_lock);
                ivQuizIcon.setBackgroundResource(R.drawable.bg_circle_warning_light);
                tvQuizSubtitle.setTextColor(context.getResources().getColor(R.color.text_secondary));
            }

            // Enable/disable card berdasarkan status unlock
            rootCard.setEnabled(bab.isUnlocked());
            rootCard.setAlpha(bab.isUnlocked() ? 1.0f : 0.6f);
        }
    }
}