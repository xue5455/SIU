package com.xue.siu.common.view.datepicker;



class DateBean {
    public int y;
    public int m;
    public int d;

    public DateBean(int y, int m, int d) {
        this.y = y;
        this.m = m;
        this.d = d;
    }


    public void setDay(DateBean dateBean) {
        this.d = dateBean.d;
        this.y = dateBean.y;
        this.m = dateBean.m;
    }


    public boolean equals(DateBean dateBean) {
        if (this.d == dateBean.d && this.y == dateBean.y && this.m == dateBean.m) {
            return true;
        }
        return false;
    }
}
