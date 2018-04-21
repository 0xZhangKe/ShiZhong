package com.zhangke.shizhong.page.poster.inputname;

import java.util.List;

/**
 * 豆瓣接口获取到的数据实体
 * <p>
 * Created by ZhangKe on 2018/4/18.
 */

public class DoubanSearchResultUserBean {
    private int total;
    private int limit;
    private boolean more;
    private List<String> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
