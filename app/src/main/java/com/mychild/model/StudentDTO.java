package com.mychild.model;

import java.io.Serializable;

/**
 * Created by Sandeep on 21-03-2015.
 */
public class StudentDTO implements Serializable {

    private String stundentName = "";
    private int studentId = 0;
    private String dob;
    private String grade;
    private String presentGuadian;
    private String gender;
    private String studentPhoto;
    private String registerNumber;
    private String localGuardian;
    private String section;

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLocalGuardian() {
        return localGuardian;
    }

    public void setLocalGuardian(String localGuardian) {
        this.localGuardian = localGuardian;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(String studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    public String getPresentGuadian() {
        return presentGuadian;
    }

    public void setPresentGuadian(String presentGuadian) {
        this.presentGuadian = presentGuadian;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    private AddressModel addressModel;

    public String getStundentName() {
        return stundentName;
    }

    public void setStundentName(String stundentName) {
        this.stundentName = stundentName;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }


}
