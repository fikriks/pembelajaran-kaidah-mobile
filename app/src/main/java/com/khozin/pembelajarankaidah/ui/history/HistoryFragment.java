package com.khozin.pembelajarankaidah.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.khozin.pembelajarankaidah.R;

/**
 * History Fragment - Halaman riwayat pembelajaran dengan empty state universal
 */
public class HistoryFragment extends Fragment {

    private View emptyStateLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_empty, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupEmptyState(view);
    }

    private void setupEmptyState(View rootView) {
        emptyStateLayout = rootView.findViewById(R.id.llEmptyState);

        // Setup empty state for History
        if (emptyStateLayout != null) {
            ImageView ivIcon = emptyStateLayout.findViewById(R.id.ivEmptyIcon);
            TextView tvTitle = emptyStateLayout.findViewById(R.id.tvEmptyTitle);
            TextView tvDescription = emptyStateLayout.findViewById(R.id.tvEmptyDescription);
            Button btnAction = emptyStateLayout.findViewById(R.id.btnAction);

            ivIcon.setImageResource(R.drawable.empty_state_universal);
            tvTitle.setText("Belum Ada Riwayat");
            tvDescription.setText("Riwayat pembelajaran Anda akan tampil di sini setelah Anda mulai berlatih.");
            btnAction.setText("Mulai Latihan");
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setOnClickListener(v -> {
                // Navigate to Kaidah list
                if (getActivity() != null) {
                    // TODO: Implement navigation to Kaidah list
                    // For now, show a toast message
                    android.widget.Toast.makeText(getContext(), "Navigasi ke Kaidah List", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}