package com.eweo.sandes.andlearns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter{

    private Context mContext;
    private final int[] imgid;
    private final String[] id;
    private final String[] web;
    private final String[] webSub;

    public CustomGrid(Context c,int[] IMGID, String[] ID, String[] WEB, String[] WEBSUB ) {
        mContext = c;
        this.imgid = IMGID;
        this.id = ID;
        this.web = WEB;
        this.webSub = WEBSUB;
    }

    @Override
    public int getCount() { return web.length; }

    @Override
    public Object getItem(int position) { return null;}

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);

            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            imageView.setImageResource(imgid[position]);

            TextView textViewID = (TextView) grid.findViewById(R.id.grid_id_text);
            textViewID.setText(id[position]);

            TextView textViewMain = (TextView) grid.findViewById(R.id.grid_text);
            textViewMain.setText(web[position]);

            TextView textViewSub = (TextView) grid.findViewById(R.id.grid_sub_text);
            textViewSub.setText(webSub[position]);
        }
        else {  grid = (View) convertView;}
        return grid;
    }
}