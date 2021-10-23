package com.donews.mine.views.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.classic.common.MultipleStatusView;
import com.donews.mine.R;
import com.donews.mine.views.refresh.adapters.BaesLoadMoreAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

/**
 * @author lcl
 * Date on 2021/10/19
 * Description:
 * 下拉刷新的容器，采用下拉刷新容器 + RecyclerView
 * 因为可能存在[CoordinatorLayout]联动。所以需要将设置相关属性和关联才能正常使用可能
 */
public class RefeshLayout extends LinearLayout {

    private int inclRootViewRes = R.layout.incl_refesh_layout;
    private SmartRefreshLayout refeshLayout;
    private MultipleStatusView stateLayout;
    private ClassicsHeader refeshHead;
    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;
    //列表滚动的总距离
    private int scrollToaPx = 0;
    //关联APPBarLayout的滚动距离
    private int appBarVerticalOffsetPx = 0;

    public RefeshLayout(Context context) {
        super(context);
        init();
    }

    public RefeshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefeshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RefeshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 联动的AppBarLayout,存在 CoordinatorLayout 联动时候关联
     * 注意：只有第一次调用有效
     *
     * @param appBarLayout
     */
    public void attchAppBarLayout(AppBarLayout appBarLayout) {
        if (this.appBarLayout != null) {
            return;
        }
        this.appBarLayout = appBarLayout;
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            appBarVerticalOffsetPx = verticalOffset;
            if (appBarVerticalOffsetPx == 0) {
                setIsAllowRefesh(true);
            } else {
                setIsAllowRefesh(false);
            }
        });
    }

    /**
     * 滚动AppBar指定的位置
     *
     * @param offsetY 指定偏移量，0：表示到最顶部(复原)，负数表示向上滚动距离
     */
    public void scrollAppBarOffset(int offsetY) {
        if (appBarLayout == null) {
            return;
        }
        if (offsetY > 0) {
            return;//无效滚动。因为最多还原。无法继续向下滚动了
        }
        //拿到 appbar 的 behavior,让 appbar 滚动
        ViewGroup.LayoutParams layoutParams = appBarLayout.getLayoutParams();
        CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {
            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
            appBarLayoutBehavior.setTopAndBottomOffset(offsetY);
        }
    }

    /**
     * 获取刷新的容器
     *
     * @return
     */
    public SmartRefreshLayout getRefeshLayout() {
        return refeshLayout;
    }

    /**
     * 获取多状态视图
     *
     * @return
     */
    public MultipleStatusView getStateLayout() {
        return stateLayout;
    }

    /**
     * 获取刷新的顶部视图
     *
     * @return
     */
    public ClassicsHeader getRefeshHead() {
        return refeshHead;
    }

    /**
     * 获取列表
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 设置是否允许下拉刷新
     *
     * @param status T:允许，F:不允许
     */
    public void setIsAllowRefesh(Boolean status) {
        refeshLayout.setEnableRefresh(status);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置布局管理器
     *
     * @param layoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 设置下拉刷新的监听
     *
     * @param listener
     */
    public void setRefeshOnListener(OnRefreshListener listener) {
        refeshLayout.setEnableLoadMore(false);
        refeshLayout.setOnRefreshListener(refreshLayout -> {
            if (recyclerView.getAdapter() instanceof BaesLoadMoreAdapter) {
                //恢复可加载更多状态
                ((BaesLoadMoreAdapter<?, ?>) recyclerView.getAdapter()).getLoadMoreModule().loadMoreComplete();
            }
            listener.onRefresh(refreshLayout);
        });
    }

    /**
     * 设置为下拉加载完成状态
     */
    public void setRefeshComplete() {
        refeshLayout.finishRefresh();
    }

    private void init() {
        View view = View.inflate(getContext(), inclRootViewRes, null);
        refeshLayout = view.findViewById(R.id.incl_refesh_layout);
        stateLayout = view.findViewById(R.id.incl_refesh_state);
        refeshHead = view.findViewById(R.id.incl_refesh_head);
        recyclerView = view.findViewById(R.id.incl_refesh_list);
        addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        //处理滚动
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                scrollToaPx = recyclerView.computeVerticalScrollOffset();
                setIsAllowRefesh(scrollToaPx <= 0);
            }
        });
    }
}
