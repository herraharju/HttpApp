package com.example.ktkoiju.httpapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by MrKohvi on 3.10.2017.
 */

public class CustomAdapter extends BaseAdapter
{
    private Context _context;
    private ArrayList<Item> _items;
    private LayoutInflater _inflater;
    private String[] _urls;
    private String  REQUEST_TAG = "imageRequest";
    private int count;

    public CustomAdapter(Context context, ArrayList<Item> items, String[] urls){
        this._context = context;
        this._items = items;
        this._inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this._urls = urls;
        this.count=0;
    }
    @Override
    public int getCount()
    {
        return _items.size();
    }

    @Override
    public Object getItem(int position)
    {
        return _items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = _inflater.inflate(R.layout.custom_list_item, null);
        NetworkImageView imageView = (NetworkImageView)convertView.findViewById(R.id.imageView);
        ImageLoader loader = AppSingleton.getInstance(_context).getImageLoader();
        TextView tv1 = (TextView) convertView.findViewById(R.id.textView1);
        TextView tv2 = (TextView) convertView.findViewById(R.id.textView2);


        loader.get(_urls[count], ImageLoader.getImageListener(imageView,
                1, android.R.drawable
                        .ic_dialog_alert));
        imageView.setImageUrl(_urls[count], loader);
        if(count >= 2){
            count =0;
        }
        else{
            count++;
        }

        tv1.setText(_items.get(position).name);
        tv2.setText(_items.get(position).date);

        return convertView;
    }


}
