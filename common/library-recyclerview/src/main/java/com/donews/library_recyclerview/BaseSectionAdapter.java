package com.donews.library_recyclerview;

import android.view.ViewGroup;


import com.donews.library_recyclerview.entity.SectionEntity;

import java.util.List;

/**
 * 分组的Adapter
 *
 * 具体用法请参考：https://github.com/pengzhenjin/BaseRecyclerViewAdapter
 */
public abstract class BaseSectionAdapter<T extends SectionEntity, VH extends DataBindBaseViewHolder> extends BaseRecyclerViewAdapter<T, VH> {

    protected static final int TYPE_SECTION_HEADER_VIEW = 0x10000005;
    protected int mSectionHeadResId;

    public BaseSectionAdapter(int sectionHeadResId, int layoutResId, List<T> dataList) {
        super(layoutResId, dataList);
        this.mSectionHeadResId = sectionHeadResId;
    }

    @Override
    protected int getDefaultItemViewType(int position) {
        T item = super.mDataList.get(position);
        if (item != null && item.isSection) {
            return TYPE_SECTION_HEADER_VIEW;
        }
        return super.getDefaultItemViewType(position);
    }

    @Override
    protected VH createDefaultViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SECTION_HEADER_VIEW) {
            return createBaseViewHolder(this.getItemView(this.mSectionHeadResId, parent));
        }
        return super.createDefaultViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_SECTION_HEADER_VIEW:
                super.setFullSpan(holder);
                this.convertHead(holder, super.mDataList.get(holder.getLayoutPosition() - super.getHeaderViewCount()));
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    protected abstract void convertHead(DataBindBaseViewHolder viewHolder, T item);
}
