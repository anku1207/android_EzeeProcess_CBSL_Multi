package in.cbslgroup.ezeepeafinal.model;

public class City {

    String cityName, cityId;

    public City(String cityName, String cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

}
