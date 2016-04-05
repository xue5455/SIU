package com.xue.siu.module.calendar.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.avim.LeanConstants;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.TimeUtil;
import com.xue.siu.common.util.media.FrescoUtil;
import com.xue.siu.db.bean.Schedule;

/**
 * Created by XUE on 2016/3/27.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_schedule_list)
public class ScheduleItemViewHolder extends TRecycleViewHolder<Schedule> implements
        View.OnClickListener {
    private SimpleDraweeView avatarSdv;
    private TextView titleTv;
    private TextView dateTv;
    private TextView timeTv;
    private TextView locationTv;
    private View locationContainer;
    private View itemContainer;

    public ScheduleItemViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        avatarSdv = findViewById(R.id.schedule_item_avatar_sdv);
        titleTv = findViewById(R.id.schedule_item_title_tv);
        dateTv = findViewById(R.id.schedule_item_date_tv);
        timeTv = findViewById(R.id.schedule_item_time_tv);
        locationTv = findViewById(R.id.schedule_item_location_tv);
        locationContainer = findViewById(R.id.schedule_item_location_container);
        itemContainer = findViewById(R.id.schedule_item_container);
        itemContainer.setOnClickListener(this);
    }

    @Override
    public void refresh(TAdapterItem<Schedule> item) {
        int size = ResourcesUtil.getDimenPxSize(R.dimen.sf_item_avatar_size);
        FrescoUtil.setImageUri(avatarSdv, AVUser.getCurrentUser().
                get(LeanConstants.PORTRAIT).toString(), (float) size);
        titleTv.setText(item.getDataModel().getTitle());
        dateTv.setText(TimeUtil.getScheduleDate(item.getDataModel().getDate() * 1000));
        timeTv.setText(ResourcesUtil.stringFormat(R.string.sf_calendar_time_format,
                TimeUtil.convertSecondsToTime(item.getDataModel().getStart()),
                TimeUtil.convertSecondsToTime(item.getDataModel().getEnd())));
        if (!TextUtils.isEmpty(item.getDataModel().getLocation())) {
            locationContainer.setVisibility(View.VISIBLE);
            locationTv.setText(item.getDataModel().getLocation());
        } else {
            locationContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition());
    }
}
