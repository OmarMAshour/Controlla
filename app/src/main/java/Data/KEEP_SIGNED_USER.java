package Data;

public class KEEP_SIGNED_USER {
    private String email;
    private String deviceSerial;
    private boolean keepSigned;

    public KEEP_SIGNED_USER(){

    }
    public KEEP_SIGNED_USER(String email, String deviceSerial, boolean keepSigned){
        this.email = email;
        this.deviceSerial = deviceSerial;
        this.keepSigned = keepSigned;
    }


    public String getEmail() {
        return email;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public boolean isKeepSigned() {
        return keepSigned;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public void setKeepSigned(boolean keepSigned) {
        this.keepSigned = keepSigned;
    }
}
