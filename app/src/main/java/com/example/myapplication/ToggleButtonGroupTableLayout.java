//package com.example.myapplication;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.RadioButton;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//
///**
// * @author diego
// */
//public class ToggleButtonGroupTableLayout extends TableLayout implements OnClickListener {
//
//    private static final String TAG = "ToggleButtonGroupTableLayout";
//    private RadioButton activeRadioButton;
//
//    /**
//     * @param context
//     */
//    public ToggleButtonGroupTableLayout(Context context) {
//        super(context);
//        // TODO Auto-generated constructor stub
//    }
//
//    /**
//     * @param context
//     * @param attrs
//     */
//    public ToggleButtonGroupTableLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        // TODO Auto-generated constructor stub
//    }
//
//    @Override
//    public void onClick(View v) {
//        final RadioButton rb = (RadioButton) v;
//        if (activeRadioButton != null) {
//            activeRadioButton.setChecked(false);
//        }
//        rb.setChecked(true);
//        activeRadioButton = rb;
//    }
//
//    /* (non-Javadoc)
//     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
//     */
//    @Override
//    public void addView(View child, int index,
//                        android.view.ViewGroup.LayoutParams params) {
//        super.addView(child, index, params);
//        setChildrenOnClickListener((TableRow) child);
//    }
//
//
//    /* (non-Javadoc)
//     * @see android.widget.TableLayout#addView(android.view.View, android.view.ViewGroup.LayoutParams)
//     */
//    @Override
//    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
//        super.addView(child, params);
//        setChildrenOnClickListener((TableRow) child);
//    }
//
//
//    private void setChildrenOnClickListener(TableRow tr) {
//        final int c = tr.getChildCount();
//        for (int i = 0; i < c; i++) {
//            final View v = tr.getChildAt(i);
//            if (v instanceof RadioButton) {
//                v.setOnClickListener(this);
//            }
//        }
//    }
//
//    public int getCheckedRadioButtonId() {
//        if (activeRadioButton != null) {
//            return activeRadioButton.getId();
//        }
//
//        return -1;
//    }
//}

//package com.example.myapplication;
//import android.content.Context;
//import android.os.Parcel;
//import android.os.Parcelable;
////import android.support.annotation.IdRes;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.RadioButton;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//
//import androidx.annotation.IdRes;
//
//public class ToggleButtonGroupTableLayout extends TableLayout implements View.OnClickListener {
//
//    private static final String TAG = "ToggleButtonGroupTableLayout";
//    private int checkedButtonID = -1;
//
//    /**
//     * @param context
//     */
//    public ToggleButtonGroupTableLayout(Context context) {
//        super(context);
//        // TODO Auto-generated constructor stub
//    }
//
//    /**
//     * @param context
//     * @param attrs
//     */
//    public ToggleButtonGroupTableLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        // TODO Auto-generated constructor stub
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v instanceof RadioButton) {
//            int id = v.getId();
//            check(id);
//        }
//    }
//
//    private void setCheckedStateForView(int viewId, boolean checked) {
//        View checkedView = findViewById(viewId);
//        if (checkedView != null && checkedView instanceof RadioButton) {
//            ((RadioButton) checkedView).setChecked(checked);
//        }
//    }
//
//    /* (non-Javadoc)
//     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
//     */
//    @Override
//    public void addView(View child, int index,
//                        android.view.ViewGroup.LayoutParams params) {
//        super.addView(child, index, params);
//        setChildrenOnClickListener((TableRow) child);
//    }
//
//
//    /* (non-Javadoc)
//     * @see android.widget.TableLayout#addView(android.view.View, android.view.ViewGroup.LayoutParams)
//     */
//    @Override
//    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
//        super.addView(child, params);
//        setChildrenOnClickListener((TableRow) child);
//    }
//
//
//    private void setChildrenOnClickListener(TableRow tr) {
//        final int c = tr.getChildCount();
//        for (int i = 0; i < c; i++) {
//            final View v = tr.getChildAt(i);
//            if (v instanceof RadioButton) {
//                v.setOnClickListener(this);
//            }
//        }
//    }
//
//
//    /**
//     * @return the checked button Id
//     */
//    public int getCheckedRadioButtonId() {
//        return checkedButtonID;
//    }
//
//
//    /**
//     * Check the id
//     *
//     * @param id
//     */
//    public void check(@IdRes int id) {
//        // don't even bother
//        if (id != -1 && (id == checkedButtonID)) {
//            return;
//        }
//        if (checkedButtonID != -1) {
//            setCheckedStateForView(checkedButtonID, false);
//        }
//        if (id != -1) {
//            setCheckedStateForView(id, true);
//        }
//        setCheckedId(id);
//    }
//
//    /**
//     * set the checked button Id
//     *
//     * @param id
//     */
//    private void setCheckedId(int id) {
//        this.checkedButtonID = id;
//    }
//
//    public void clearCheck() {
//        check(-1);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        if (!(state instanceof SavedState)) {
//            super.onRestoreInstanceState(state);
//            return;
//        }
//
//        SavedState ss = (SavedState) state;
//        super.onRestoreInstanceState(ss.getSuperState());
//
//        this.checkedButtonID = ss.buttonId;
//        setCheckedStateForView(checkedButtonID, true);
//    }
//
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState savedState = new SavedState(superState);
//        savedState.buttonId = checkedButtonID;
//        return savedState;
//    }
//
//    static class SavedState extends BaseSavedState {
//        int buttonId;
//
//        /**
//         * Constructor used when reading from a parcel. Reads the state of the superclass.
//         *
//         * @param source
//         */
//        public SavedState(Parcel source) {
//            super(source);
//            buttonId = source.readInt();
//        }
//
//        /**
//         * Constructor called by derived classes when creating their SavedState objects
//         *
//         * @param superState The state of the superclass of this view
//         */
//        public SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags) {
//            super.writeToParcel(out, flags);
//            out.writeInt(buttonId);
//        }
//
//        public static final Parcelable.Creator<SavedState> CREATOR =
//                new Parcelable.Creator<SavedState>() {
//                    public SavedState createFromParcel(Parcel in) {
//                        return new SavedState(in);
//                    }
//
//                    public SavedState[] newArray(int size) {
//                        return new SavedState[size];
//                    }
//                };
//    }
//}

package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;

public class ToggleButtonGroupTableLayout extends GridLayout {

    private int mCheckedId = -1;
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;
    private static final String TAG = "ToggleButtonGroupTableLayout";

    private void setCheckedId(@IdRes int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
        AutofillManager afm = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            afm = getContext().getSystemService(AutofillManager.class);
        }
        if (afm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                afm.notifyValueChanged(this);
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(ToggleButtonGroupTableLayout group, @IdRes int checkedId);
    }

    private int mInitialCheckedId = View.NO_ID;

    public ToggleButtonGroupTableLayout(Context context) {
        super(context);
        setOrientation(VERTICAL);
        init();
    }

    public ToggleButtonGroupTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getImportantForAutofill() == IMPORTANT_FOR_AUTOFILL_AUTO) {
                setImportantForAutofill(IMPORTANT_FOR_AUTOFILL_YES);
            }
        }

        TypedArray attributes = context.obtainStyledAttributes(
                attrs,
                R.styleable.RadioGridLayout,
                R.attr.radioButtonStyle, 0);

        int value = attributes.getResourceId(R.styleable.RadioGridLayout_checked, View.NO_ID);
        if (value != View.NO_ID) {
            mCheckedId = value;
            mInitialCheckedId = value;
        }

        attributes.recycle();
        init();
    }

    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof RadioButton) {
            final RadioButton button = (RadioButton) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        }

        super.addView(child, index, params);
    }

    public void check(@IdRes int id) {
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    @IdRes
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }

    public void clearCheck() {
        check(-1);
    }

    @Override
    public GridLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new GridLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof RadioGroup.LayoutParams;
    }

    @Override
    protected GridLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return RadioGroup.class.getName();
    }

    public static class LayoutParams extends GridLayout.LayoutParams {

        public LayoutParams(Spec rowSpec, Spec columnSpec) {
            super(rowSpec, columnSpec);
        }

        public LayoutParams() {
            super();
        }

        public LayoutParams(ViewGroup.LayoutParams params) {
            super(params);
        }

        public LayoutParams(MarginLayoutParams params) {
            super(params);
        }

        public LayoutParams(GridLayout.LayoutParams source) {
            super(source);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void setBaseAttributes(TypedArray a,
                                         int widthAttr, int heightAttr) {

            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }

            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == ToggleButtonGroupTableLayout.this && child instanceof RadioButton) {
                int id = child.getId();
                if (id == View.NO_ID) {
                    id = View.generateViewId();
                    child.setId(id);
                }
                ((RadioButton) child).setOnCheckedChangeListener(
                        mChildOnCheckedChangeListener);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == ToggleButtonGroupTableLayout.this && child instanceof RadioButton) {
                ((RadioButton) child).setOnCheckedChangeListener(null);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    @Override
    public void onProvideAutofillStructure(ViewStructure structure, int flags) {
        super.onProvideAutofillStructure(structure, flags);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            structure.setDataIsSensitive(mCheckedId != mInitialCheckedId);
        }
    }

    @Override
    public void autofill(AutofillValue value) {
        if (!isEnabled()) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!value.isList()) {
                Log.d(TAG, "autofill:" + value + " could not be autofilled into " + this);
                return;
            }
        }

        int index = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            index = value.getListValue();
        }
        final View child = getChildAt(index);
        if (child == null) {
            Log.d(TAG, "autofill: RadioGroup.autoFill(): no child with index" + index);
            return;
        }

        check(child.getId());
    }

    @Override
    public int getAutofillType() {
        return isEnabled() ? AUTOFILL_TYPE_LIST : AUTOFILL_TYPE_NONE;
    }

    @Override
    public AutofillValue getAutofillValue() {
        if (!isEnabled()) return null;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getId() == mCheckedId) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return AutofillValue.forList(i);
                }
            }
        }
        return null;
    }
}