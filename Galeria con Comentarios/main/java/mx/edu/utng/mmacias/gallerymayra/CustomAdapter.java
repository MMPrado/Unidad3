package mx.edu.utng.mmacias.gallerymayra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Dell Latitude on 12/02/2018.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    int pics[];
    LayoutInflater inflter;
    public CustomAdapter(Context applicationContext, int[] logos) {
        this.context = applicationContext;
        this.pics = logos;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return pics.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_gridview, null); // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.icon); // get the reference of ImageView
        icon.setImageResource(pics[i]); // set logo images
        return view;
    }
}