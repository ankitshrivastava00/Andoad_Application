package rapidora.co.myapplication.model;

/**
 * Created by azmat.ali.khan on 05/05/16.
 */
public class UpdateLocationRequestModel {



    private String latitude;
    private String longtitude;
    private String address;
    private String receivetime;
    private String id;



   // private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }  public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longtitude;
    }

    public void setLongitude(String longitude) {
        this.longtitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(String time) {
        this.receivetime = time;
    }
}
