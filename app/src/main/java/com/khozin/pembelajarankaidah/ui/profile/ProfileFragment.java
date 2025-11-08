package com.khozin.pembelajarankaidah.ui.profile;

import android.content.Intent;
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
 * Profile Fragment - Halaman profil siswa dengan empty state universal
 */
public class ProfileFragment extends Fragment {

    private View emptyStateLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_empty, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupEmptyState(view);
    }

    private void setupEmptyState(View rootView) {
        emptyStateLayout = rootView.findViewById(R.id.llEmptyState);

        // Setup empty state for Profile
        if (emptyStateLayout != null) {
            ImageView ivIcon = emptyStateLayout.findViewById(R.id.ivEmptyIcon);
            TextView tvTitle = emptyStateLayout.findViewById(R.id.tvEmptyTitle);
            TextView tvDescription = emptyStateLayout.findViewById(R.id.tvEmptyDescription);
            Button btnAction = emptyStateLayout.findViewById(R.id.btnAction);

            ivIcon.setImageResource(R.drawable.empty_state_universal);
            tvTitle.setText("Profil Belum Lengkap");
            tvDescription.setText("Lengkapi profil Anda untuk mendapatkan pengalaman belajar yang lebih personal.");
            btnAction.setText("Edit Profil");
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setOnClickListener(v -> {
                // Navigate to profile edit or login
                if (getActivity() != null) {
                    // TODO: Implement profile edit
                    // For now, show a toast message
                    android.widget.Toast.makeText(getContext(), "Edit Profil", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}