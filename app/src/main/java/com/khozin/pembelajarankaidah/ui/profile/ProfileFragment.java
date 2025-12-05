package com.khozin.pembelajarankaidah.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.khozin.pembelajarankaidah.LoginActivity;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.utils.SessionManager;

/**
 * Profile Fragment - Halaman profil siswa sederhana
 */
public class ProfileFragment extends Fragment {

    private TextView tvStudentName;
    private TextView tvStudentClass;
    private MaterialButton btnLogout;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupProfileData();
        setupClickListeners();
    }

    private void initViews(View rootView) {
        tvStudentName = rootView.findViewById(R.id.tvStudentName);
        tvStudentClass = rootView.findViewById(R.id.tvStudentClass);
        btnLogout = rootView.findViewById(R.id.btnLogout);
        sessionManager = new SessionManager(requireContext());
    }

    private void setupProfileData() {
        if (sessionManager != null) {
            // Get student data from session
            String studentName = sessionManager.getUserName();
            String studentClass = sessionManager.getUserClass();

            // Set data to views
            if (tvStudentName != null) {
                tvStudentName.setText(studentName != null && !studentName.isEmpty() ? studentName : "Nama Siswa");
            }

            if (tvStudentClass != null) {
                tvStudentClass.setText(studentClass != null && !studentClass.isEmpty() ? studentClass : "Kelas");
            }
        }
    }

    private void setupClickListeners() {
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
        }
    }

    private void showLogoutConfirmationDialog() {
        if (getContext() == null) return;

        new AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setPositiveButton("Ya, Keluar", (dialog, which) -> performLogout())
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    private void performLogout() {
        if (sessionManager != null) {
            // Clear session data
            sessionManager.logout();

            // Navigate to LoginActivity
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            // Finish the activity to prevent back navigation
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }
}