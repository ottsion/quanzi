package com.tizi.quanzi.fragment.register;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.RequreForImage;

import java.io.IOException;

public class CompleteUesrInfo extends Fragment {

    private Activity mActivity;

    private TextInputLayout nickNameInputLayout;
    private NetworkImageView UserPhotoImageView;
    private RadioGroup sexGroup;

    /* 请求码 */
    private static final String IMAGE_FILE_NAME = "faceImage";
    private String photoTakenUri;
    private String photoOnlineUri;

    private RequreForImage requreForImage;

    private AllDone allDone;

    public void setAllDone(AllDone allDone) {
        this.allDone = allDone;
    }

    public CompleteUesrInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_uesr_info, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        nickNameInputLayout = (TextInputLayout) mActivity.findViewById(R.id.nickNameInputLayout);
        UserPhotoImageView = (NetworkImageView) mActivity.findViewById(R.id.UserPhotoImageView);
        UserPhotoImageView.setImageResource(R.drawable.face);
        sexGroup = (RadioGroup) mActivity.findViewById(R.id.sexGroup);
        Button submitButton = (Button) mActivity.findViewById(R.id.submit_button);
        requreForImage = new RequreForImage(mActivity);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allDone.CompUserInfoOK(nickNameInputLayout.getEditText().getText().toString(),
                        sexGroup.getCheckedRadioButtonId(), photoOnlineUri);
            }
        });
        UserPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requreForImage.showDialogAndCallIntent();
            }
        });

    }

    /**
     * 本界面完成回调借口
     */
    public interface AllDone {
        void CompUserInfoOK(String userName, int sex, String faceUri);
    }

    public void onIntentResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String filePath = requreForImage.FilePathFromIntent(data);
            if (filePath != null) {
                savePhoto(filePath);
            }
        }
    }


    private void savePhoto(String filepath) {
        AVFile file = null;
        try {
            file = AVFile.withAbsoluteLocalPath(App.getUserID() + "face.jpg",
                    filepath);
            final AVFile finalFile = file;
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e != null) {
                        //上传失败
                    } else {
                        String photoUri = finalFile.getThumbnailUrl(false, 200, 200);
                        UserPhotoImageView.setImageUrl(photoUri,
                                GetVolley.getmInstance(mActivity).getImageLoader());
                        photoOnlineUri = photoUri;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
