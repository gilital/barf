package com.bezeq.locator.bl;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.bezeq.locator.draw.Marker;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

public abstract class LocalDataSource {
	
	protected Dictionary<String,Bitmap> icons = new Hashtable<String,Bitmap>();
    
    public LocalDataSource(Resources res) {
    }
    
    protected abstract void createIcons(Resources res);
    
    public abstract List<Marker> getMarkers(Context context);

}
