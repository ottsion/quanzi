package com.tizi.quanzi.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.tool.FriendTime;

import java.util.List;

/**
 * Created by qixingchen on 15/9/3.
 * 私信、系统消息列表
 */
public class PrivateMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PrivateMessageAdapter.class.getSimpleName();

    private List<PrivateMessPair> privateMessPairs;
    private Context mContext;
    private Onclick onclick;


    /**
     * @param privateMessPairs 私信列表
     * @param mContext         上下文
     * @param onclick          被点击时的回调
     */
    public PrivateMessageAdapter(List<PrivateMessPair> privateMessPairs, Context mContext, Onclick onclick) {
        this.privateMessPairs = privateMessPairs;
        this.mContext = mContext;
        this.onclick = onclick;
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onChanged(PrivateMessPairList list) {
        notifyDataSetChanged();
    }

    /**
     * 创建 ViewHolder
     *
     * @param parent   需要创建ViewHolder的 ViewGroup
     * @param viewType 记录类型
     *
     * @return ViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        RecyclerView.ViewHolder vh;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_private_mess, parent, false);
        vh = new PrivateViewHolder(v);

        return vh;
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {



        /*私信*/
        if (PrivateViewHolder.class.isInstance(holder)) {
            final PrivateViewHolder privateVH = (PrivateViewHolder) holder;
            final PrivateMessPair privateMessPair = privateMessPairs.get(position);
            Picasso.with(mContext).load(privateMessPair.getFace()).fit().into(privateVH.mUserFaceImage);
            privateVH.mUserNameText.setText(privateMessPair.getName());
            privateVH.mMessTextView.setText(privateMessPair.getLastMess());
            privateVH.lastMessTimeTextView.setText(FriendTime.FriendlyDate(privateMessPair.getLastMessTime()));
            if (privateMessPair.getUnreadCount() != 0) {
                privateVH.mMessTextView.setTypeface(Typeface.DEFAULT_BOLD);
                privateVH.mMessTextView.setTextColor(mContext.getResources().getColor(R.color.md_grey_1000));
                privateVH.mUserNameText.setTypeface(Typeface.DEFAULT_BOLD);
                privateVH.mUserNameText.setTextColor(mContext.getResources().getColor(R.color.md_grey_1000));
            } else {
                privateVH.mMessTextView.setTypeface(Typeface.DEFAULT);
                privateVH.mMessTextView.setTextColor(mContext.getResources().getColor(R.color.md_grey_700));
                privateVH.mUserNameText.setTypeface(Typeface.DEFAULT);
                privateVH.mUserNameText.setTextColor(mContext.getResources().getColor(R.color.md_grey_700));
            }
            privateVH.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onclick != null) {
                        onclick.priMessClick(privateMessPair);
                    } else {
                        Log.w(TAG, "私聊消息被点击,但是没有 onclick 回调");
                    }
                }
            });
            privateVH.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("删除列表项").setMessage("确认删除这个聊天项目么?")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBAct.getInstance().deletePriMessPair(privateMessPair.getID());
                                    privateMessPairs.remove(privateMessPair);
                                    notifyItemRemoved(position);
                                }
                            }).setNegativeButton("取消", null).show();

                    return false;
                }
            });
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return privateMessPairs == null ? 0 : privateMessPairs.size();
    }

    /**
     * 点击接口
     */
    public interface Onclick {

        /**
         * 项目被点击
         *
         * @param privateMessPair 被点击的私聊消息组
         */
        void priMessClick(PrivateMessPair privateMessPair);
    }

    public static class PrivateViewHolder extends RecyclerView.ViewHolder {

        private ImageView mUserFaceImage;
        private TextView mUserNameText;
        private TextView mMessTextView;
        private TextView lastMessTimeTextView;
        private View itemView;

        public PrivateViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mUserFaceImage = (ImageView) itemView.findViewById(R.id.user_face_image);
            mUserNameText = (TextView) itemView.findViewById(R.id.user_name_text_view);
            mMessTextView = (TextView) itemView.findViewById(R.id.mess_text_view);
            lastMessTimeTextView = (TextView) itemView.findViewById(R.id.last_mess_time_text_view);
        }
    }

}
