package com.withpet.health;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import java.util.ArrayList;
import com.withpet.*;

public class diary_detail_Adapter extends PagerAdapter {
    private Context context;
    private ArrayList<Integer> imagelist;

    public diary_detail_Adapter(Context context , ArrayList<Integer> imagelist){
        this.context = context;
        this.imagelist = imagelist;
    }

    @NonNull
    @Override
    //데이터 리스트에서 인자로 넘어온 position에 해당하는 아이템 항목에 대한 페이지를 생성한다
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_view, null);

        ImageView imageView = view.findViewById(R.id.imageView2);
        imageView.setImageResource(imagelist.get(position));

        container.addView(view);

        return view;
    }

    @Override
    //adapter가 관리하는 데이터 리스트의 총 개수
    public int getCount() {
        return imagelist.size();
    }

    @Override
    //adapter가 관리하는 데이터 리스트에서 인자로 넘어온 position에 해당하는 데이터 항목을 생서된
    //페이지를 제거
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    //페이지가 특정 키와 연관 되는지 체크
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view ==(View) object);
    }
}
