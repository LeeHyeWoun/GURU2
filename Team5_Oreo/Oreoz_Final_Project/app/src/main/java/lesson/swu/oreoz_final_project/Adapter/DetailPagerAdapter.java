package lesson.swu.oreoz_final_project.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import lesson.swu.oreoz_final_project.Fragment.Detail1Fragment;
import lesson.swu.oreoz_final_project.Fragment.Detail2Fragment;

public class DetailPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTab;

    //생성자
    public DetailPagerAdapter(FragmentManager fm, int NumOfTab){
        super(fm);
        mNumOfTab = NumOfTab;
    }

    @Override
    public Fragment getItem(int position) {
        //BaseAdapter 에서 getView() 메서드에 해당하는 메서드로싸,
        //position 값이 곧 현재 선택 된 Tab 의 Index 번호를 나타낸다.
        switch (position){
            case 0:
                Detail1Fragment tab1 = new Detail1Fragment();
                return tab1;

            case 1:
                Detail2Fragment tab2 = new Detail2Fragment();
                return tab2;
        }
        return null;
    }

    @Override
    public int getCount() { return mNumOfTab;}

}
