package com.example.manuel.sudo;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealLinearLayout;
import com.example.manuel.sudo.C;
import com.example.manuel.sudo.R;
import com.example.manuel.sudo.SudokuAdapter;
import com.example.manuel.sudo.SudokuCell;
import com.example.manuel.sudo.SudokuGenerator;
import com.example.manuel.sudo.LogUtil;

import static android.content.Context.MODE_PRIVATE;


public class GameFragment extends BaseFragment implements AdapterView.OnItemClickListener, OnSudokuChangedListener {
    private static final String ARG_LEVEL = "level";

    private String mLv;
    long startTime = System.currentTimeMillis();
    private GridView sudoku;
    private GridView number;

    private LinearLayout frame;

    private View mCurrSelectedView;
    private SudokuCell mCurrCell;
    SudokuAdapter sudokuAdapter;
    NumberAdapter numberAdapter;

    private Stack<Integer> mCurrPos;
    private int pos;

    SudokuGenerator generator;

    public GameFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lv Game level
     * @return A new instance of fragment GameFragment.
     */
    public static GameFragment newInstance(String lv) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LEVEL, lv);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mLv = getArguments().getString(ARG_LEVEL);
            mCurrPos = new Stack<>();
            generator = new SudokuGenerator();
            generator.setBlockSize(3);

                    generator.generateProblems(SudokuGenerator.Level.MID);




        } else {
            throw new IllegalArgumentException("You must have a level args first!");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (C.DEBUG) {
            LogUtil.log(mLv);
        }

        final View view;
        if (Build.VERSION_CODES.LOLLIPOP > Build.VERSION.SDK_INT) {
            view = inflater.inflate(R.layout.fragment_game, container, false);
            frame = (RevealLinearLayout) view.findViewById(R.id.frame);
        } else {
            view = inflater.inflate(R.layout.fragment_game, container, false);
            frame = (LinearLayout) view.findViewById(R.id.frame);
        }

        sudoku = (GridView) view.findViewById(R.id.gv_sudoku);
        number = (GridView) view.findViewById(R.id.gv_number);

        sudokuAdapter = new SudokuAdapter(getActivity(), generator.asList(), generator);


        numberAdapter = new NumberAdapter(getActivity(),
                Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
        initSudoku();
        number.setAdapter(numberAdapter);
        number.setOnItemClickListener(this);
        return view;
    }



    private void initSudoku() {
        sudoku.setOnItemClickListener(this);
        sudokuAdapter.setOnChangedListener(this);
        sudoku.setAdapter(sudokuAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.gv_sudoku) {
            pos = position;
            view.setBackground(getActivity().getResources().getDrawable(R.drawable.item_border_selected));
            if (view != mCurrSelectedView && mCurrSelectedView != null) {
                mCurrSelectedView.setBackground(getActivity().getResources().getDrawable(R.drawable.item_border));

            }
            mCurrSelectedView = view;
            mCurrCell = (SudokuCell) sudokuAdapter.getItem(position);
        } else if (parent.getId() == R.id.gv_number) {
            mCurrPos.push(pos);
            if (mCurrSelectedView != null) {
                if (mCurrSelectedView instanceof TextView) {
                    if (((TextView) mCurrSelectedView).getCurrentTextColor()
                            != getResources().getColor(R.color.colorAccent)
                            && mCurrCell.isShown()) {
                        return;
                    }
                    TextView tv = (TextView) mCurrSelectedView;
                    tv.setText(numberAdapter.getItem(position) + "");
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    mCurrCell.setValue(Integer.parseInt(parent.getAdapter().getItem(position) + ""));
                    sudokuAdapter.notifyDataSetChanged(mCurrPos.peek());
                    if (C.DEBUG) {
                        LogUtil.log("input after \n" + generator.toString());
                    }

                }
            }
        }
    }


    @Override
    public void onSuccess() {
        long difference = System.currentTimeMillis() - startTime;
        float time=difference/1000;
        time =time/60;

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int gamesWon=pref.getInt("GAMES_WON", 0);
        gamesWon++;
        editor.putInt("GAMES_WON", gamesWon);


       float time1=pref.getFloat("GAME_TIME", 0 );
        if (time1==0 || time<=time1){
            editor.putFloat("GAME_TIME", time);
        }
        editor.apply();

        Toast.makeText(getActivity(), String.valueOf(time)+" Minuti", Toast.LENGTH_SHORT).show();
        Snackbar.make(frame, getResources().getString(R.string.success), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.another_game), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        generator = new SudokuGenerator();
                        generator.setBlockSize(3);

                                generator.generateProblems(SudokuGenerator.Level.MID);



                        sudokuAdapter = new SudokuAdapter(getActivity(), generator.asList(), generator);
                        sudokuAdapter.setOnChangedListener(GameFragment.this);
                        sudoku.setAdapter(sudokuAdapter);
                        sudoku.invalidateViews();
                        mCurrPos.clear();
                        mCurrCell = null;
                        mCurrSelectedView = null;
                    }
                }).show();
    }


    @Override
    public void onRowError(int row) {
        @ColorInt int red = getResources().getColor(R.color.android_red_dark);
        for (int i = 0; i < generator.getFieldSize(); i++) {
            View view = sudoku.getChildAt(row * generator.getFieldSize() + i);
            if (view instanceof TextView) {
                animateFlashText((TextView) view, ((TextView) view).getCurrentTextColor(), red, false);
            }
        }
    }


    @Override
    public void onColError(int col) {
        @ColorInt int red = getResources().getColor(R.color.android_red_dark);
        for (int i = 0; i <= col; i++) {
            View view = sudoku.getChildAt(col + i * generator.getFieldSize());
            if (view instanceof TextView) {
                animateFlashText((TextView) view, ((TextView) view).getCurrentTextColor(), red, false);
            }
        }
    }


    @Override
    public void onBlockError(int row, int col) {
        @ColorInt int red = getResources().getColor(R.color.android_red_dark);
        int left = col / generator.getBlockSize() * 3;
        int bottom = row / generator.getBlockSize() * 3;
        for (int i = bottom; i < bottom + 3; i++) {
            for (int j = left; j < left + 3; j++) {
                View view = sudoku.getChildAt(j + i * generator.getFieldSize());
                if (view instanceof TextView) {
                    animateFlashText((TextView) view, ((TextView) view).getCurrentTextColor(), red, false);
                }
            }
        }

    }

    private static void animateFlashText(
            final TextView textView, @ColorInt final int color1, @ColorInt int color2, boolean staySecondColor) {
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setTextColor((Integer) animation.getAnimatedValue());
            }

        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                textView.setTextColor(color1);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(staySecondColor ? 4 : 5);
        anim.setDuration(400);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
    }

    class NumberAdapter extends BaseAdapter {
        List<String> datas;
        LayoutInflater inflater;
        public NumberAdapter(Context ctx, List<String> datas) {
            inflater = LayoutInflater.from(ctx);
            this.datas = datas;
        }
        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.number_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tv = (TextView) convertView.findViewById(R.id.number);
                convertView.setTag(viewHolder);
                
            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }
            String text = datas.get(position);
            viewHolder.tv.setText(text);

            return convertView;
        }

        class ViewHolder {
            TextView tv;
            
        }
    }
}
