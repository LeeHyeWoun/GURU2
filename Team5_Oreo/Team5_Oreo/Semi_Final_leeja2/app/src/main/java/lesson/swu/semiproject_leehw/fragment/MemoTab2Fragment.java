package lesson.swu.semiproject_leehw.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import lesson.swu.semiproject_leehw.Bean.MemoBean;
import lesson.swu.semiproject_leehw.R;
import lesson.swu.semiproject_leehw.util.PrefUtil;

public class MemoTab2Fragment extends Fragment {
    //카메라 실행시 요청코드
    public static final int REQ_GO_CAMERA = 2222;
    private ImageView imgMemo;
    private TextView txtReTake;
    public String mCurrentPhotoPath;
    private Uri mImageUri;
    private MemoBean memoBean;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo_tab2,null);

        //코드상으로 명시적으로 퍼미션을 요청하지 않으면 롤리팝 이상 버젼에서 실행되지 않는다.
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                }, 1);

        imgMemo = view.findViewById(R.id.imgMemo);
        txtReTake = view.findViewById(R.id.txtReTake);

        //넘겨준 데이터를 받는다.-------------------------------------------------------------------
        memoBean = (MemoBean) getActivity().getIntent().getSerializableExtra(MemoBean.class.getName());

        if(memoBean!=null){
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(memoBean.getMemo_img());
                imgMemo.setImageBitmap(bitmap);
            }catch(OutOfMemoryError oom){
                oom.printStackTrace();
            }

            //데어터 세팅
            imgMemo.setImageBitmap(bitmap);
        }

        imgMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureCamera(); //메서드 호출
                txtReTake.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }//end OnCreate()

    private void captureCamera() {

        String state = Environment.getExternalStorageState();
        //외장 메모리 검사
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if( takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null ) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (Exception e) {
                    Log.e("SWU", e.toString());
                }

                if(photoFile != null) {
                    //getUriForFile의 두번째 인자는 Manifest provider의 authorities 와 일치해야 함.
                    Uri providerURI =
                            FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), photoFile);
                    mImageUri = providerURI;

                    //인텐트에 전달할 때는 FileProvider의 Return값이 content://로만,
                    //providerURI의 값에 카메라 데이터를 넣어 보냄.
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                    //롤리팝 이상버젼에서만 카메라가 기동된다.
                    startActivityForResult(takePictureIntent, REQ_GO_CAMERA);
                }
            }
        }//end if

    }//end captureCamera()


    //카메라를 찍기전에 카메라로 찍은 이미지가 저장될 파일을 생성-반환 한다.
    private File createImageFile() throws Exception {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPGE_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir =
                new File(Environment.getExternalStorageDirectory() + "/Pictures/", "gyeom");

        if(!storageDir.exists()) {
            Log.d("SWU", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQ_GO_CAMERA:
                if(resultCode == Activity.RESULT_OK) {
                    imgMemo.setImageURI(mImageUri);
                } else {
                    mCurrentPhotoPath = null;
                }
                break;
        }
    }

}
