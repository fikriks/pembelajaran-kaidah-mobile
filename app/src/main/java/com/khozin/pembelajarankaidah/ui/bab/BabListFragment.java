package com.khozin.pembelajarankaidah.ui.bab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.adapter.BabAdapter;
import com.khozin.pembelajarankaidah.data.model.Bab;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.data.repository.BabRepository;
import com.khozin.pembelajarankaidah.network.RetrofitClient;
import com.khozin.pembelajarankaidah.ui.kaidah.KaidahListFragment;

import java.util.List;
import java.util.ArrayList;

/**
 * Fragment untuk menampilkan daftar Bab/Chapter
 */
public class BabListFragment extends Fragment implements BabAdapter.OnChapterClickListener {

    private RecyclerView recyclerView;
    private BabAdapter babAdapter;
    private BabViewModel babViewModel;
    private BabRepository babRepository;
    private View emptyStateView;
    private View loadingView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bab_list, container, false);
        initViews(view);
        setupViewModel();
        setupObservers();
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_babs);
        emptyStateView = view.findViewById(R.id.empty_state);
        loadingView = view.findViewById(R.id.loading_view);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        babAdapter = new BabAdapter(getContext(), this);
        recyclerView.setAdapter(babAdapter);

        // Initially show loading
        showLoading(true);
    }

    private void setupViewModel() {
        // Get API service
        ApiService apiService = RetrofitClient.getInstance().getRetrofit().create(ApiService.class);

        // Setup ViewModel
        babViewModel = new ViewModelProvider(requireActivity()).get(BabViewModel.class);
        babViewModel.setApiService(apiService);

        // Setup Repository
        babRepository = new BabRepository(requireContext(), apiService);
    }

    private void setupObservers() {
        // Observe chapters
        babViewModel.getChaptersLiveData().observe(getViewLifecycleOwner(), chapters -> {
            if (chapters != null && !chapters.isEmpty()) {
                showChapters(chapters);
                showLoading(false);
            } else {
                showEmptyState();
                showLoading(false);
            }
        });

        // Observe loading state
        babViewModel.getLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                showLoading(isLoading);
            }
        });

        // Observe error messages
        babViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                showError(errorMessage);
                babViewModel.clearError();
            }
        });

        // Observe success messages
        babViewModel.getSuccessLiveData().observe(getViewLifecycleOwner(), successMessage -> {
            if (successMessage != null) {
                showSuccess(successMessage);
                babViewModel.clearSuccess();
            }
        });

        // Observe progress overview
        babViewModel.getProgressOverviewLiveData().observe(getViewLifecycleOwner(), progressResponse -> {
            if (progressResponse != null && progressResponse.getData() != null) {
                updateProgressData(progressResponse.getData().getChapters());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load chapters when fragment resumes
        loadChapters();
    }

    private void loadChapters() {
        // Try to load from local first
        // TODO: Replace with API call - babViewModel.loadChaptersFromAPI();

        // Then refresh from API
        babViewModel.refreshChapters();
    }

    private void refreshChapters() {
        babViewModel.refreshChapters();
    }

    private void showChapters(List<Bab> chapters) {
        if (babAdapter != null) {
            babAdapter.setBabList(chapters);
        }
        recyclerView.setVisibility(View.VISIBLE);
        emptyStateView.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        emptyStateView.setVisibility(View.VISIBLE);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            loadingView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.GONE);
        } else {
            loadingView.setVisibility(View.GONE);
        }
    }

    private void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    private void showSuccess(String successMessage) {
        Toast.makeText(getContext(), successMessage, Toast.LENGTH_SHORT).show();
    }

    private void updateProgressData(List<Bab> chaptersWithProgress) {
        if (chaptersWithProgress != null && babAdapter != null) {
            // Update existing chapters with progress data
            for (int i = 0; i < chaptersWithProgress.size(); i++) {
                Bab updatedChapter = chaptersWithProgress.get(i);
                // Find matching chapter in current list and update it
                List<Bab> currentChapters = babAdapter.getBabList();
                if (currentChapters != null) {
                    for (int j = 0; j < currentChapters.size(); j++) {
                        if (currentChapters.get(j).getIdBab() == updatedChapter.getIdBab()) {
                            babAdapter.updateItem(j, updatedChapter);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onChapterClick(Bab bab) {
        if (bab != null && bab.isAccessible()) {
            // Navigate to Kaidah list for this chapter
            navigateToKaidahList(bab);
        } else if (bab != null && !bab.isAccessible()) {
            // Show locked message
            showLockedMessage(bab);
        }
    }

    @Override
    public void onChapterLongClick(Bab bab) {
        if (bab != null) {
            // Show chapter details or options
            showChapterOptions(bab);
        }
    }

    private void navigateToKaidahList(Bab bab) {
        // Create new fragment for kaidah list filtered by chapter
        KaidahListFragment kaidahFragment = new KaidahListFragment();

        // Pass chapter information via arguments
        Bundle args = new Bundle();
        args.putInt("chapter_id", bab.getIdBab());
        args.putString("chapter_name", bab.getNamaBab());
        args.putString("chapter_code", bab.getChapterCode());
        kaidahFragment.setArguments(args);

        // Navigate to fragment
        if (getParentFragmentManager() != null) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, kaidahFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void showLockedMessage(Bab bab) {
        String message = "Bab " + bab.getNamaBabSaja() + " masih terkunci. " +
                "Selesaikan bab sebelumnya untuk membuka bab ini.";
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void showChapterOptions(Bab bab) {
        // TODO: Implement chapter options dialog
        // Options could include:
        // - View chapter details
        // - View chapter progress
        // - Reset chapter progress
        // - Mark chapter as complete (for testing)
    }

    /**
     * Clear observers and cleanup
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear LiveData observers
        if (babViewModel != null) {
            babViewModel.getChaptersLiveData().removeObservers(getViewLifecycleOwner());
            babViewModel.getLoadingLiveData().removeObservers(getViewLifecycleOwner());
            babViewModel.getErrorLiveData().removeObservers(getViewLifecycleOwner());
            babViewModel.getSuccessLiveData().removeObservers(getViewLifecycleOwner());
            babViewModel.getProgressOverviewLiveData().removeObservers(getViewLifecycleOwner());
        }
    }
}