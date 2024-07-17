package com.college.managerpengeluaran;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomBottomNavigationView extends BottomNavigationView {

    public CustomBottomNavigationView(@NonNull Context context) {
        super(context);
    }

    public CustomBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        centerMenuItems();
    }

    private void centerMenuItems() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            View itemView = menuView.getChildAt(i);
            View icon = itemView.findViewById(com.google.android.material.R.id.icon);
            ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();

            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = dpToPx(getContext(), 6);
                ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = dpToPx(getContext(), 6);
                icon.setLayoutParams(layoutParams);
            }
        }
    }

    private int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}