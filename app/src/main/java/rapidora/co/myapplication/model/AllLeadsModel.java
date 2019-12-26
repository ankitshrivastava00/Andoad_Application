package rapidora.co.myapplication.model;

/**
 * Created by ANDROID on 06-Nov-17.
 */

public class AllLeadsModel {

    String startTime,endTime,duration,mobileReciever,callType,availableInPhoneBook,natureOfCall,filePath,name;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public AllLeadsModel(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AllLeadsModel(String name, String filePath, String startTime, String endTime, String duration, String mobileReciever, String callType, String availableInPhoneBook, String natureOfCall) {
        this.filePath = filePath;
        this.name = name;
        this.startTime = startTime;

        this.endTime = endTime;
        this.duration = duration;

        this.mobileReciever = mobileReciever;
        this.callType = callType;
        this.availableInPhoneBook = availableInPhoneBook;
        this.natureOfCall = natureOfCall;
    }

    public String getStartTime() {

        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMobileReciever() {
        return mobileReciever;
    }

    public void setMobileReciever(String mobileReciever) {
        this.mobileReciever = mobileReciever;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getAvailableInPhoneBook() {
        return availableInPhoneBook;
    }

    public void setAvailableInPhoneBook(String availableInPhoneBook) {
        this.availableInPhoneBook = availableInPhoneBook;
    }

    public String getNatureOfCall() {
        return natureOfCall;
    }

    public void setNatureOfCall(String natureOfCall) {
        this.natureOfCall = natureOfCall;
    }
}
