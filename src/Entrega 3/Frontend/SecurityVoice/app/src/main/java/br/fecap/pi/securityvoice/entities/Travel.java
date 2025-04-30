package br.fecap.pi.securityvoice.entities;

public class Travel {
    private Integer id;
    private Integer driverId;
    private Integer passengerId;
    private String destination;
    private String origin;

    private String date;
    private String cust;
    private String duration;
    private String driverName;
    private String passengerName;
    private String state;

    public Travel(){
        
    }

    public Travel(Integer id, Integer driverId, Integer passengerId, String destination, String origin, String date, String cust, String duration, String driverName, String passengerName, String state) {
        this.id = id;
        this.driverId = driverId;
        this.passengerId = passengerId;
        this.destination = destination;
        this.origin = origin;
        this.date = date;
        this.cust = cust;
        this.duration = duration;
        this.driverName = driverName;
        this.passengerName = passengerName;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCust() {
        return cust;
    }

    public void setCust(String cust) {
        this.cust = cust;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
