package com.tizi.quanzi.tool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.tizi.quanzi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/8/28.
 * 获取系统可分享的应用列表
 */
public class GetShareIntent {

    public static List<LabeledIntent> onShareClick(Context context) {

        PackageManager pm = context.getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;

            Intent intent = new Intent();
            intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.share_the_app));
            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));

        }
        return intentList;
    }

    public static void startShare(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.share_the_app));
        context.startActivity(intent);
    }

}