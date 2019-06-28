package Data;


import org.json.JSONException;
import org.json.JSONObject;
import static com.controlla.controlla.Main2Activity.*;
import static com.controlla.controlla.MainActivity.*;
public class Weather {

    private String temperature;
    private String city;
    private String iconName;

    private String wind;
    private String humidity;
    private String pressure;
    private String description;
    private String country;

    private int condition;

    public static Weather fromJson(JSONObject jsonObject) throws JSONException {

        try {

            Weather weather = new Weather();

            weather.city = jsonObject.getString("name");
            weather.country = jsonObject.getJSONObject("sys").getString("country");
            weather.condition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weather.iconName = determineWeatherIcon(weather.condition);

            weather.description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

            double pressure = jsonObject.getJSONObject("main").getDouble("pressure");
            weather.pressure = Double.toString(pressure);

            double humidity = jsonObject.getJSONObject("main").getDouble("humidity");
            weather.humidity = Double.toString(humidity);

            double wind = jsonObject.getJSONObject("wind").getDouble("speed");
            weather.wind = Double.toString(wind);

            double temperatureInKelvin = jsonObject.getJSONObject("main").getDouble("temp");
            double temperatureInCelsius = temperatureInKelvin - 273.15;

            int approximateTemperature = (int) Math.rint(temperatureInCelsius);
            weather.temperature = Integer.toString(approximateTemperature);
            currentWeather = weather;
            return weather;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String determineWeatherIcon(int condition) {

        if (condition >= 200 && condition <= 233) {
            return "thunderstorm";
        } else if (condition >=300 && condition <= 321) {
            return "drizzle";
        } else if (condition >= 500 && condition <= 531) {
            return "rain";
        } else if (condition >= 600 && condition <= 622) {
            return "snow";
        } else if (condition >= 701 && condition <= 781) {
            return "mist";
        } else if (condition == 800) {
            return "clear_sky";
        } else if (condition >= 801 && condition <= 804) {
            return "clouds";
        }

        return "unknown";

    }

    public String getTemperature() {
        return temperature + "°C";
    }

    public String getCity() {
        return city;
    }

    public String getIconName() {
        return iconName;
    }

    public String getWind() {
        return wind + "m/s";
    }

    public String getHumidity() {
        return humidity + "%";
    }

    public String getPressure() {
        return pressure + "hpa";
    }

    public String getDescription() {
        return description;
    }

    public String getCountry() {
        return country;
    }
}
