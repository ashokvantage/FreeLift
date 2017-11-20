package com.freelift;

/**
 * Created by ADMIN on 02-Feb-17.
 */

public class Holder {
    String src_address, dest_address, src_lat, src_long, dest_lat, dest_long, date, firstname, lastname, mobile, email,bookingId,taxi_type,status,C_Reason,Date_Time,CancelBy;
String Drivername,Driveremail,Drivermobile;
String country_code,country_name;

    public String getCancelBy() {
        return CancelBy;
    }

    public void setCancelBy(String cancelBy) {
        CancelBy = cancelBy;
    }

    public String getDate_Time() {
        return Date_Time;
    }

    public void setDate_Time(String date_Time) {
        Date_Time = date_Time;
    }

    public String getC_Reason() {
        return C_Reason;
    }

    public void setC_Reason(String c_Reason) {
        C_Reason = c_Reason;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getDrivername() {
        return Drivername;
    }

    public void setDrivername(String drivername) {
        Drivername = drivername;
    }

    public String getDriveremail() {
        return Driveremail;
    }

    public void setDriveremail(String driveremail) {
        Driveremail = driveremail;
    }

    public String getDrivermobile() {
        return Drivermobile;
    }

    public void setDrivermobile(String drivermobile) {
        Drivermobile = drivermobile;
    }

    public String getTaxi_type() {
        return taxi_type;
    }

    public void setTaxi_type(String taxi_type) {
        this.taxi_type = taxi_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getSrc_address() {
        return src_address;
    }

    public void setSrc_address(String src_address) {
        this.src_address = src_address;
    }

    public String getDest_address() {
        return dest_address;
    }

    public void setDest_address(String dest_address) {
        this.dest_address = dest_address;
    }

    public String getSrc_lat() {
        return src_lat;
    }

    public void setSrc_lat(String src_lat) {
        this.src_lat = src_lat;
    }

    public String getSrc_long() {
        return src_long;
    }

    public void setSrc_long(String src_long) {
        this.src_long = src_long;
    }

    public String getDest_lat() {
        return dest_lat;
    }

    public void setDest_lat(String dest_lat) {
        this.dest_lat = dest_lat;
    }

    public String getDest_long() {
        return dest_long;
    }

    public void setDest_long(String dest_long) {
        this.dest_long = dest_long;
    }

//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Holder() {

    }
}
