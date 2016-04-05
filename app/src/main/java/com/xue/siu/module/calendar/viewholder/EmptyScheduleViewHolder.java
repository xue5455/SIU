package com.xue.siu.module.calendar.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;

/**
 * Created by XUE on 2016/3/27.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_empty_schedule_list)
public class EmptyScheduleViewHolder extends TRecycleViewHolder implements
        View.OnClickListener {
    private Button btnNewSchedule;

    public EmptyScheduleViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        btnNewSchedule = (Button) findViewById(R.id.schedule_item_new_btn);
        btnNewSchedule.setOnClickListener(this);
    }

    @Override
    public void refresh(TAdapterItem item) {

    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition());
        }
    }
}
