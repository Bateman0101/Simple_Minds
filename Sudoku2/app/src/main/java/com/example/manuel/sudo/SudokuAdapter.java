package com.example.manuel.sudo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.manuel.sudo.C;
import com.example.manuel.sudo.R;
import com.example.manuel.sudo.SudokuCell;
import com.example.manuel.sudo.SudokuGenerator;
import com.example.manuel.sudo.OnSudokuChangedListener;
import com.example.manuel.sudo.LogUtil;


public class SudokuAdapter extends BaseAdapter {
    List<SudokuCell> datas;
    LayoutInflater inflater;
    OnSudokuChangedListener listener;
    SudokuGenerator generator;


    public SudokuAdapter(Context ctx, List<SudokuCell> list, SudokuGenerator generator) {
        inflater = LayoutInflater.from(ctx);
        datas = list;
        this.generator = generator;
    }



    public void setOnChangedListener(OnSudokuChangedListener listener) {
        this.listener = listener;
    }



    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    public void clear(int position) {
        datas.get(position).clear();
    }

    private int getCol(int position) {
        return position % generator.getSudoku().getFieldSize() + 1;
    }

    private int getRow(int position) {
        return position / generator.getSudoku().getFieldSize() + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item, parent, false);
            holder = new ViewHolder();
            holder.mTv = (TextView) convertView.findViewById(R.id.number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position<=2 || (position >=6 && position<=11) || (position>=15 && position<=20) || (position >=24 && position<=26) || (position>=30 && position<=32) || (position >=39 && position<=41) || (position>=48 && position<=50) || (position>=54 && position<=56) || (position>=60 && position <= 65) || (position >=69 && position <= 74) || position>= 78) {
            holder.mTv.setBackgroundColor(Color.argb(100, 43, 51, 47));
        }
        SudokuCell cell = datas.get(position);
        if (cell.isShown()) {
            holder.mTv.setText(cell.getValue()+"");
        }
        return convertView;
    }



    static class ViewHolder {
        TextView mTv;
    }


    public void notifyDataSetChanged(int pos) {
        super.notifyDataSetChanged();
        if (pos < 0) return;
        SudokuGenerator.State row = generator.checkRow(getRow(pos) - 1);
        SudokuGenerator.State column = generator.checkColumn(getCol(pos) - 1);
        SudokuGenerator.State block = generator.checkBlock(getRow(pos) - 1, getCol(pos) - 1);
        boolean allFill = generator.getSudoku().allCellsFilled();
        if (listener != null) {
            if (block == SudokuGenerator.State.BLOCK_ERROR) {
                if (C.DEBUG) {
                    LogUtil.log("block error");
                }
                listener.onBlockError(getRow(pos) - 1, getCol(pos) - 1);
            } else if (column == SudokuGenerator.State.COL_ERROR) {
                if (C.DEBUG) {
                    LogUtil.log("col error");
                }
                listener.onColError(getCol(pos) - 1);
            } else if (row == SudokuGenerator.State.ROW_ERROR) {
                if (C.DEBUG) {
                    LogUtil.log("row error");
                }
                listener.onRowError(getRow(pos) - 1);
            } else if (allFill) {
                listener.onSuccess();
            }
        }


    }
}
