package com.tizi.quanzi.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/10/29.
 */
public class AllTags extends OnlySuccess {


    /**
     * id : HTDM0048773c059f042340eeafe4261bf92b2a51
     * tagName : 政治
     * parentTagName : 哪个是你相对擅长的领域?
     */

    @SerializedName("tags")
    public List<TagsEntity> tags;

    public static String getTagString(List<TagsEntity> tags) {
        String tagsString = "";
        for (AllTags.TagsEntity tag : tags) {
            tagsString += tag.tagName + ",";
        }
        return tagsString;
    }

    public String getTagServerString(List<TagsEntity> oldTags) {
        ArrayList<TAG> tags = new ArrayList<>();
        for (AllTags.TagsEntity oldtag : oldTags) {
            tags.add(new TAG(oldtag.id, oldtag.tagName));
        }
        return new Gson().toJson(tags);
    }

    public static class TagsEntity implements Parcelable {
        public static final Parcelable.Creator<TagsEntity> CREATOR = new Parcelable.Creator<TagsEntity>() {
            public TagsEntity createFromParcel(Parcel source) {
                return new TagsEntity(source);
            }

            public TagsEntity[] newArray(int size) {
                return new TagsEntity[size];
            }
        };
        @SerializedName("id")
        public String id;
        @SerializedName("tagName")
        public String tagName;
        @SerializedName("parentTagName")
        public String parentTagName;

        public TagsEntity() {
        }

        protected TagsEntity(Parcel in) {
            this.id = in.readString();
            this.tagName = in.readString();
            this.parentTagName = in.readString();
        }

        public static TagsEntity getNewTag(String tagName) {
            TagsEntity tag = new TagsEntity();
            tag.tagName = tagName;
            return tag;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.tagName);
            dest.writeString(this.parentTagName);
        }
    }

    private class TAG {
        public String tagid;
        public String tagname;

        public TAG(String tagid, String tagName) {
            this.tagid = tagid;
            this.tagname = tagName;
        }
    }
}
