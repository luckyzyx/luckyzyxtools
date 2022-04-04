package com.luckyzyx.tools.utils.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.bean.LogBean;

import java.util.List;

public class UpdateLogAdapter extends BaseQuickAdapter<LogBean, BaseViewHolder> {
    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public UpdateLogAdapter(List<LogBean> list) {
        super(R.layout.update_log_items, list);
    }
    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, LogBean item) {
        baseViewHolder.setText(R.id.title, item.getTitle());
        baseViewHolder.setText(R.id.desc, item.getDesc());
    }

}