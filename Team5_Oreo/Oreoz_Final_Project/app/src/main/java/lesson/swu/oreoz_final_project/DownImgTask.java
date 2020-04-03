package lesson.swu.oreoz_final_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import lesson.swu.oreoz_final_project.util.PrefUtil;

/***
 * 비동기로 이미지를 다운로드 한다.
 */

public class DownImgTask extends AsyncTask<URL,Void,Bitmap> {

    //이미지뷰를 저장하는 임시 멤버면수 (메모리 관리 imageView)
    private final WeakReference<ImageView> mImageView;

    public DownImgTask(ImageView imageView){
        mImageView = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(URL... urls) {
        URL imageURL = urls[0];
        Bitmap downroadedBirmap=null;
        try{
            //인터넷을 통해서 이미지를 다운로드 받는다.
            InputStream inputStream = imageURL.openStream();
            downroadedBirmap = BitmapFactory.decodeStream(inputStream);
        }catch (Exception e){e.printStackTrace();}
        return downroadedBirmap;
    }// end doInBackground

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap!=null&&mImageView.get()!=null){
            //화면상에 이미지를 대입한다.
            mImageView.get().setImageBitmap(bitmap);
        }
    }//onPostExecute

}//end DownImgTask
