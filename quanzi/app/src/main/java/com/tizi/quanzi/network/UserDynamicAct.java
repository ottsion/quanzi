package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.AddComment;
import com.tizi.quanzi.gson.AddZan;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.gson.IsZan;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yulan on 2015/11/29.
 */
public class UserDynamicAct extends RetrofitNetworkAbs {

    private RetrofitAPI.UserDyns dynsSer = RetrofitNetwork.retrofit.create(RetrofitAPI.UserDyns.class);

    public static UserDynamicAct getNewInstance() {
        return new UserDynamicAct();
    }

    /**
     * 查询圈子内对于某事件的动态
     */
    public void getDynamic(int start) {

        dynsSer.findDyns(start, StaticField.Limit.DynamicLimit)
                .enqueue(new Callback<Dyns>() {
                    @Override
                    public void onResponse(Call<Dyns> call, Response<Dyns> response) {
                        myOnResponse(response);
                    }

                    @Override
                    public void onFailure(Call<Dyns> call, Throwable t) {
                        myOnFailure(t);
                    }
                });
    }

    /**
     * 获取动态
     *
     * @param userID 想要获取动态的用户的ID
     * @param start  开始的序号
     */
    public void getDynamic(String userID, int start) {

        dynsSer.findDyns(userID, start, StaticField.Limit.DynamicLimit).enqueue(new Callback<com.tizi.quanzi.gson.Dyns>() {
            @Override
            public void onResponse(Call<Dyns> call, Response<Dyns> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<Dyns> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /**
     * 获取动态
     *
     * @param dynID 动态ID
     */
    public void getDynamicByID(String dynID) {
        dynsSer.findDynByID(dynID).enqueue(new Callback<Dyns>() {
            @Override
            public void onResponse(Call<Dyns> call, Response<Dyns> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<Dyns> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /*点赞*/
    public void addZan(String dynID, boolean isZan) {
        dynsSer.zan(dynID, isZan ? 1 : -1).enqueue(new Callback<AddZan>() {
            @Override
            public void onResponse(Call<AddZan> call, Response<AddZan> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<AddZan> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    public void isZan(String dynID) {
        dynsSer.isZan(dynID).enqueue(new Callback<IsZan>() {
            @Override
            public void onResponse(Call<IsZan> call, Response<IsZan> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<IsZan> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }


    /*获取评论*/
    public void getComment(String dynID, int start, int limit) {
        dynsSer.findComment(dynID, start, limit).enqueue(new Callback<Comments>() {
            @Override
            public void onResponse(Call<Comments> call, Response<Comments> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<Comments> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /*添加评论*/
    public void addComment(String dynID, String comment) {
        dynsSer.addComent(dynID, Tool.getUTF_8String(comment)).enqueue(new Callback<AddComment>() {
            @Override
            public void onResponse(Call<AddComment> call, Response<AddComment> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<AddComment> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }


    /*添加评论*/
    public void addComment(String dynID, String comment, String replyID, String atUserID) {
        dynsSer.addComent(dynID, Tool.getUTF_8String(comment), replyID, atUserID).enqueue(new Callback<AddComment>() {
            @Override
            public void onResponse(Call<AddComment> call, Response<AddComment> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<AddComment> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /*发表动态*/
    public void addDYn(String comment, String pic) {

        Call<OnlySuccess> addDynCall;
        comment = Tool.getUTF_8String(comment);
        if (pic == null) {
            addDynCall = dynsSer.addDyn(comment);
        } else {
            Log.i(TAG, pic);
            addDynCall = dynsSer.addDyn(comment, Tool.getUTF_8String(pic));
        }

        addDynCall.enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(Call<OnlySuccess> call, Response<OnlySuccess> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<OnlySuccess> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    public void addDYn(String comment) {
        addDYn(comment, null);
    }

    /*删除评论*/
    public void deleteComment(String commentID) {
        dynsSer.deleteComment(commentID).enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(Call<OnlySuccess> call, Response<OnlySuccess> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<OnlySuccess> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /*删除动态*/
    public void deleteDyn(String dynID) {
        dynsSer.deleteDyn(dynID).enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(Call<OnlySuccess> call, Response<OnlySuccess> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<OnlySuccess> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserDynamicAct setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }

}
