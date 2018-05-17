package com.zhangke.shizhong.page.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * RecyclerAdapter 简化基类，支持 item 点击事件
 * Created by ZhangKe on 2018/4/18.
 */

public abstract class BaseRecyclerAdapter<T extends BaseRecyclerAdapter.ViewHolder, P> extends RecyclerView.Adapter<T> {

    protected Context context;
    protected List<P> listData;
    protected LayoutInflater inflater;
    protected OnRecyclerItemClickListener onItemClickListener;

    public BaseRecyclerAdapter(Context context, List<P> listData) {
        this.context = context;
        this.listData = listData;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                if (BaseRecyclerAdapter.this.onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnRecyclerItemClickListener {
        /**
         * @param position 点击 position
         */
        void onItemClick(int position);
    }
}
