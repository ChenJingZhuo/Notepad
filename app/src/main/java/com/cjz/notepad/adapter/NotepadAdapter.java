package com.cjz.notepad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cjz.notepad.R;
import com.cjz.notepad.bean.NotepadBean;

import java.util.List;

public class NotepadAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<NotepadBean> list;
    public boolean flag = false;

    public NotepadAdapter(Context context, List<NotepadBean> list){
        this.layoutInflater=LayoutInflater.from(context);
        this.list=list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.notepad_item_layout,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        NotepadBean notepadInfo= (NotepadBean) getItem(position);
        if (notepadInfo != null) {
            viewHolder.tvNotepadContent.setText(notepadInfo.getNotepadContent());
            viewHolder.tvNotepadTime.setText(notepadInfo.getNotepadTime());
            viewHolder.checkBox.setChecked(notepadInfo.isCheck);

            // 根据isSelected来设置checkbox的显示状况
            if (flag) {
                viewHolder.checkBox.setVisibility(View.VISIBLE);
            } else {
                viewHolder.checkBox.setVisibility(View.GONE);
            }
            //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (notepadInfo.isCheck) {
                        notepadInfo.isCheck = false;
                    } else {
                        notepadInfo.isCheck = true;
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder{
        TextView tvNotepadContent;
        TextView tvNotepadTime;
        CheckBox checkBox;
        public ViewHolder(View view){
            tvNotepadContent=view.findViewById(R.id.item_content);
            tvNotepadTime=view.findViewById(R.id.item_time);
            checkBox=view.findViewById(R.id.checkbox_operate_data);
        }
    }
}
