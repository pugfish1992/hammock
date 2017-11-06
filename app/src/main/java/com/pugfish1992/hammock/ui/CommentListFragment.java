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
import com.pugfish1992.hammock.ui.binder.CommentPosterBinder;
import com.pugfish1992.hammock.ui.binder.CommentsViewerBinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentListFragment extends Fragment implements BottomSheet {

    public interface InteractionListener {
        void onCloseFragment();
    }

    private InteractionListener mInteractionListener;

    // In the case of using this fragment as a BottomSheet...
    @BottomSheetBehavior.State private static final int INVALID_SHEET_STATE = -5062;
    @BottomSheetBehavior.State  private int mPrevSheetState = INVALID_SHEET_STATE;
    private boolean mUseAsBottomSheet = true;
    @Nullable private CommentsViewerBinder mCommentsViewerBinder;
    @Nullable private CommentPosterBinder mCommentPosterBinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);

//        if (mUseAsBottomSheet) {
//            mCommentPosterBinder = new CommentPosterBinder(view);
//            mCommentsViewerBinder = new CommentsViewerBinder(view,
//                    new CommentsViewerBinder.ActionListener() {
//                        @Override
//                        public void onCloseButtonClick() {
//                            mInteractionListener.onCloseFragment();
//                        }
//                    });
//        }

        return view;
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
        if (mCommentsViewerBinder == null || mCommentPosterBinder == null) return;
        
        final View commentPosterRoot = mCommentPosterBinder.getRootView();
        final View commentsViewerRoot = mCommentsViewerBinder.getRootView();

        if (newState == STATE_EXPANDED &&
                (mPrevSheetState == STATE_SETTLING || mPrevSheetState == STATE_DRAGGING)) {
            // When expanded
            int cx = commentPosterRoot.getLeft() + commentPosterRoot.getWidth() / 2;
            int cy = commentPosterRoot.getTop() + commentPosterRoot.getHeight() / 2;
            int startRad = 0;
            int endRad = (int) Math.hypot(commentsViewerRoot.getWidth(), commentsViewerRoot.getHeight());
            Animator revealAnim = ViewAnimationUtils.createCircularReveal(commentsViewerRoot, cx, cy, startRad, endRad);
            revealAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    commentPosterRoot.setVisibility(View.GONE);
                }
            });
            commentsViewerRoot.setVisibility(View.VISIBLE);
            revealAnim.start();

        } else
        if (mPrevSheetState == STATE_EXPANDED &&
                (newState == STATE_DRAGGING || newState == STATE_SETTLING)) {
            // When began to collapse
            // When expanded
            int cx = commentPosterRoot.getLeft() + commentPosterRoot.getWidth() / 2;
            int cy = commentPosterRoot.getTop() + commentPosterRoot.getHeight() / 2;
            int startRad = (int) Math.hypot(commentsViewerRoot.getWidth(), commentsViewerRoot.getHeight());
            int endRad = 0;
            Animator closeAnim = ViewAnimationUtils.createCircularReveal(commentsViewerRoot, cx, cy, startRad, endRad);
            closeAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    commentsViewerRoot.setVisibility(View.GONE);
                }
            });
            commentPosterRoot.setVisibility(View.VISIBLE);
            closeAnim.start();
        }

        mPrevSheetState = newState;
    }
}
