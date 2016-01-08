package com.xue.siu.common.view.datepicker;

public interface OnCalendarClickHandler {
    void setSuccessor(OnCalendarClickHandler handler);

    void handleCalendarClick(DateBean dateBean);
}
