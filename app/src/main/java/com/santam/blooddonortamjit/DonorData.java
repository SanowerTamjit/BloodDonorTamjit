package com.santam.blooddonortamjit;


public class DonorData {

    private String name,cellno,bloodgrp,country,imgurl;

    public DonorData(String name, String cellno, String bloodgrp, String country, String imgurl) {
        this.name = name;
        this.cellno = cellno;
        this.bloodgrp = bloodgrp;
        this.country = country;
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellno() {
        return cellno;
    }

    public void setCellno(String cellno) {
        this.cellno = cellno;
    }

    public String getBloodgrp() {
        return bloodgrp;
    }

    public void setBloodgrp(String bloodgrp) {
        this.bloodgrp = bloodgrp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
