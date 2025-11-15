package com.khozin.pembelajarankaidah.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.utils.SessionManager;

/**
 * Profile Fragment - Halaman profil siswa sederhana
 */
public class ProfileFragment extends Fragment {

    private TextView tvStudentName;
    private TextView tvStudentClass;
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
    }

    private void initViews(View rootView) {
        tvStudentName = rootView.findViewById(R.id.tvStudentName);
        tvStudentClass = rootView.findViewById(R.id.tvStudentClass);
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
}