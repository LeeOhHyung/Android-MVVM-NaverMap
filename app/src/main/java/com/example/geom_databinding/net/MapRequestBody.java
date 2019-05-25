package com.example.geom_databinding.net;

public class MapRequestBody {

    private double lng, lat;

    private double swlng, swlat;

    private double selng, selat;

    private double nelng, nelat;

    private double nwlng, nwlat;

    public MapRequestBody(
            double lng, double lat,
            double swlng, double swlat,
            double selng, double selat,
            double nelng, double nelat,
            double nwlng, double nwlat) {

        this.lng = lng;
        this.lat = lat;
        this.swlng = swlng;
        this.swlat = swlat;
        this.selng = selng;
        this.selat = selat;
        this.nelng = nelng;
        this.nelat = nelat;
        this.nwlng = nwlng;
        this.nwlat = nwlat;
    }
}
