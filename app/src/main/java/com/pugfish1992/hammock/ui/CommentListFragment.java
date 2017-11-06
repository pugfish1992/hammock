package com.pugfish1992.hammock.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;

import static android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING;
import static android.support.design.widget.BottomSheetBehavior.STATE_SETTLING;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pugfish1992.hammock.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentListFragment extends Fragment implements BottomSheet {

    public interface InteractionListener {
        void onCloseFragment();
    }

    private boolean mIsViewCreated;
    private InteractionListener mInteractionListener;

    // UI
    private ImageView mCloseIconButton;

    // In the case of using this fragment as a BottomSheet...
    @BottomSheetBehavior.State private static final int INVALID_SHEET_STATE = -5062;
    @BottomSheetBehavior.State  private int mPrevSheetState = INVALID_SHEET_STATE;
    private boolean mUseAsBottomSheet = true;
    @Nullable private ViewGroup mCommentListViewRoot;
    @Nullable private ViewGroup mCommentViewRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);

        mCloseIconButton = view.findViewById(R.id.img_down_arrow);
        mCloseIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInteractionListener.onCloseFragment();
            }
        });

        if (mUseAsBottomSheet) {
            mCommentListViewRoot = view.findViewById(R.id.fl_comment_list_view_root);
            mCommentViewRoot = view.findViewById(R.id.rl_comment_view_root);
        }

        mIsViewCreated = true;
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsViewCreated = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mInteractionListener = (InteractionListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.getClass().getSimpleName()
                    + " must implements " + InteractionListener.class.getName());
        }
    }

    /**
     * INTERFACE IMPL -> BottomSheet
     * ---------- */

    @Override
    public void onBottomSheetStateChanged(int newState) {
        if (mCommentListViewRoot == null || mCommentViewRoot == null) return;

        if (newState == STATE_EXPANDED &&
                (mPrevSheetState == STATE_SETTLING || mPrevSheetState == STATE_DRAGGING)) {
            // When expanded
            int cx = mCommentViewRoot.getLeft() + mCommentViewRoot.getWidth() / 2;
            int cy = mCommentViewRoot.getTop() + mCommentViewRoot.getHeight() / 2;
            int startRad = 0;
            int endRad = (int) Math.hypot(mCommentListViewRoot.getWidth(), mCommentListViewRoot.getHeight());
            Animator revealAnim = ViewAnimationUtils.createCircularReveal(mCommentListViewRoot, cx, cy, startRad, endRad);
            revealAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCommentViewRoot.setVisibility(View.GONE);
                }
            });
            mCommentListViewRoot.setVisibility(View.VISIBLE);
            revealAnim.start();

        } else
        if (mPrevSheetState == STATE_EXPANDED &&
                (newState == STATE_DRAGGING || newState == STATE_SETTLING)) {
            // When began to collapse
            // When expanded
            int cx = mCommentViewRoot.getLeft() + mCommentViewRoot.getWidth() / 2;
            int cy = mCommentViewRoot.getTop() + mCommentViewRoot.getHeight() / 2;
            int startRad = (int) Math.hypot(mCommentListViewRoot.getWidth(), mCommentListViewRoot.getHeight());
            int endRad = 0;
            Animator closeAnim = ViewAnimationUtils.createCircularReveal(mCommentListViewRoot, cx, cy, startRad, endRad);
            closeAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCommentListViewRoot.setVisibility(View.GONE);
                }
            });
            mCommentViewRoot.setVisibility(View.VISIBLE);
            closeAnim.start();
        }

        mPrevSheetState = newState;
    }
}
