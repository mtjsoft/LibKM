package cn.mtjsoft.libkotlinmvvm.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import cn.mtjsoft.libkotlinmvvm.R;
import cn.mtjsoft.libkotlinmvvm.model.ListTestModel;

import java.util.List;

/**
 * @author mtj
 * @Package cn.mtjsoft.libkotlinmvvm.adapter
 * @date 2020-09-01 10:10:12
 */

public class ListTestAdapter extends BaseQuickAdapter<ListTestModel, BaseViewHolder> {

    private Context context;

    public ListTestAdapter(Context context, List<ListTestModel> list) {
        super(R.layout.item_text, list);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, ListTestModel model) {
        int position = holder.getLayoutPosition();
        holder.setText(R.id.tv_itemText, model.getNumSrt());
    }
}