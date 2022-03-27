package com.tierscooters.app.ui.main.vehicles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.tierscooters.app.R;
import com.tierscooters.app.models.Vehicle;

public class TierClusterRenderer extends DefaultClusterRenderer<Vehicle> {
    private Context context = null;
    public TierClusterRenderer(Context context, GoogleMap map, ClusterManager<Vehicle> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }
    @Override
    protected void onBeforeClusterItemRendered(Vehicle item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.scooter);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);

        markerOptions.title("").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
    }

    @Override
    protected void onClusterItemRendered(final Vehicle clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);

    }
}
