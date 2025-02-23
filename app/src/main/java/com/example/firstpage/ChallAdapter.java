package com.example.firstpage;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ChallAdapter extends FragmentStateAdapter {

    public ChallAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new InProgressFragment(); // This should be your `fragment_in_progress.xml`
            case 1:
                return new DoneFragment(); // This should be your `fragment_done.xml`
            default:
                return new InProgressFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}
