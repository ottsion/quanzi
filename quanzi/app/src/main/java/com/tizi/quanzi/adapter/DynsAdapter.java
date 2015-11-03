package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.Intent.StartGalleryActivity;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.GetThumbnailsUri;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by qixingchen on 15/8/19.
 * 动态
 */
public class DynsAdapter extends RecyclerView.Adapter<DynsAdapter.DynsViewHolder> {

    private static final String TAG = DynsAdapter.class.getSimpleName();
    private List<Dyns.DynsEntity> dynsList;
    private Context mContext;
    private NeedMore needMore;
    private Onclick onclick;

    /**
     * @param dynsList 动态List
     * @param context  上下文
     *
     * @see com.tizi.quanzi.gson.Dyns.DynsEntity
     */
    public DynsAdapter(List<Dyns.DynsEntity> dynsList, Context context) {
        this.dynsList = dynsList;
        this.mContext = context;
    }

    /**
     * @param parent   需要创建ViewHolder的 ViewGroup
     * @param viewType 样式类型
     *
     * @return ViewHolder
     *
     * @see com.tizi.quanzi.adapter.DynsAdapter.DynsViewHolder
     */
    @Override
    public DynsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dyns, parent, false);
        // set the view's size, margins, paddings and layout parameters
        DynsViewHolder vh = new DynsViewHolder(v);
        return vh;
    }


    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(DynsViewHolder holder, final int position) {
        final Dyns.DynsEntity dyns = dynsList.get(position);

        Picasso.with(holder.view.getContext()).load(dyns.icon)
                .resizeDimen(R.dimen.dyn_user_icon, R.dimen.dyn_user_icon)
                .into(holder.weibo_avatar_ImageView);
        holder.userNameTextView.setText(dyns.nickName);
        holder.contentTextView.setText(dyns.content);
        holder.dateTextView.setText(dyns.createTime);
        holder.attitudesTextView.setText(String.valueOf(dyns.zan));
        holder.commentsTextView.setText(String.valueOf(dyns.commentNum));
        int picsNum = dyns.pics.size();
        if (picsNum > 3) {
            picsNum = 3;
        }
        int hei = mContext.getResources().getDimensionPixelSize(R.dimen.weibo_pic_hei);
        int wei = mContext.getResources().getDimensionPixelSize(R.dimen.weibo_pic_wei);
        for (int i = 0; i < picsNum; i++) {
            String thumUri = GetThumbnailsUri.maxHeiAndWei(dyns.pics.get(i).url, hei, wei);
            Picasso.with(holder.view.getContext()).load(thumUri)
                    .resizeDimen(R.dimen.weibo_pic_hei, R.dimen.weibo_pic_wei)
                    .into(holder.weibo_pics_ImageView[i]);
            final int finalI = i;
            holder.weibo_pics_ImageView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartGalleryActivity.startByStringList(getPicsInfo(position), finalI, mContext);
                }
            });
        }
        holder.setPicVisbility(picsNum);

        //点击回调
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclick != null) {
                    onclick.click(dyns);
                } else {
                    Log.w(TAG, "Onclick 回调为空");
                }
            }
        });

        //加载更多
        if (position == dynsList.size() - 1) {
            if (needMore != null) {
                needMore.needMore();
            } else {
                Log.w(TAG, "needMore 回调为空");
            }
        }
    }

    /**
     * 获取图片uri List
     *
     * @param position Dyns位置
     *
     * @return pic uri List
     */
    private ArrayList<String> getPicsInfo(int position) {
        ArrayList<String> pics = new ArrayList<>();
        for (Dyns.DynsEntity.PicsEntity picsEntity : dynsList.get(position).pics) {
            pics.add(picsEntity.url);
        }
        return pics;
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return dynsList == null ? 0 : dynsList.size();
    }


    /**
     * 为 dynsList 添加内容
     * 如果 dynsList 为空则新建。
     *
     * @param dynsEntities 需要添加的内容List
     */
    public void addItems(List<Dyns.DynsEntity> dynsEntities) {
        if (dynsList == null) {
            dynsList = new ArrayList<>();
        }
        for (Dyns.DynsEntity dynsEntity : dynsEntities) {
            dynsList.add(dynsEntity);
        }
        notifyDataSetChanged();
    }

    public void setNeedMore(NeedMore needMore) {
        this.needMore = needMore;
    }

    public void setOnclick(Onclick onclick) {
        this.onclick = onclick;
    }

    public interface NeedMore {
        void needMore();
    }

    public interface Onclick {
        void click(Dyns.DynsEntity dyn);
    }

    /**
     * 动态界面的ViewHolder
     */
    public static class DynsViewHolder extends RecyclerView.ViewHolder {

        //界面元素
        private ImageView weibo_avatar_ImageView;
        private TextView userNameTextView, contentTextView, dateTextView,
                attitudesTextView, commentsTextView;
        private ImageView[] weibo_pics_ImageView = new ImageView[3];
        private LinearLayout weibo_pics_linearLayout;
        private View view;

        public DynsViewHolder(View v) {
            super(v);
            FindViewByID(v);
        }

        /**
         * 为界面元素赋值
         *
         * @param v 布局
         */
        private void FindViewByID(View v) {
            view = v;
            weibo_avatar_ImageView = (ImageView) v.findViewById(R.id.weibo_avatar);
            userNameTextView = (TextView) v.findViewById(R.id.weibo_name);
            contentTextView = (TextView) v.findViewById(R.id.weibo_content);
            dateTextView = (TextView) v.findViewById(R.id.weibo_date);
            attitudesTextView = (TextView) v.findViewById(R.id.weibo_attitudes);
            commentsTextView = (TextView) v.findViewById(R.id.weibo_comments);
            weibo_pics_ImageView[0] = (ImageView) v.findViewById(R.id.weibo_pic0);
            weibo_pics_ImageView[1] = (ImageView) v.findViewById(R.id.weibo_pic1);
            weibo_pics_ImageView[2] = (ImageView) v.findViewById(R.id.weibo_pic2);
            //            weibo_pics_ImageView[3] = (NetworkImageView) v.findViewById(R.id.weibo_pic3);
            //            weibo_pics_ImageView[4] = (NetworkImageView) v.findViewById(R.id.weibo_pic4);
            //            weibo_pics_ImageView[5] = (NetworkImageView) v.findViewById(R.id.weibo_pic5);
            //            weibo_pics_ImageView[6] = (NetworkImageView) v.findViewById(R.id.weibo_pic6);
            //            weibo_pics_ImageView[7] = (NetworkImageView) v.findViewById(R.id.weibo_pic7);
            //            weibo_pics_ImageView[8] = (NetworkImageView) v.findViewById(R.id.weibo_pic8);
            weibo_pics_linearLayout = (LinearLayout) v.findViewById(R.id.weibo_pics);
        }

        /**
         * 将需要的图片设置为可见
         * 将多余的图片设置成不可见
         * 如果没有图片，则将 weibo_pics_linearLayout 也设置为不可见
         *
         * @param picsNum 图片数量
         */
        public void setPicVisbility(int picsNum) {
            if (picsNum == 0) {
                weibo_pics_linearLayout.setVisibility(View.GONE);
                return;
            }
            weibo_pics_linearLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < picsNum; i++) {
                weibo_pics_ImageView[i].setVisibility(View.VISIBLE);
            }
            for (int i = picsNum; i < 3; i++) {
                weibo_pics_ImageView[i].setVisibility(View.GONE);
                weibo_pics_ImageView[i].setOnClickListener(null);
            }
        }
    }

}
