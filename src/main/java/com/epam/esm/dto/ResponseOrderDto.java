package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;

public class ResponseOrderDto extends RepresentationModel<ResponseOrderDto> {

    private int id;

    private BigDecimal cost;

    private String purchaseDate;

    private int userId;

    private List<Integer> certificatesId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getCertificatesId() {
        return certificatesId;
    }

    public void setCertificatesId(List<Integer> certificatesId) {
        this.certificatesId = certificatesId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResponseOrderDto that = (ResponseOrderDto) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (cost != null ? !cost.equals(that.cost) : that.cost != null) return false;
        if (purchaseDate != null ? !purchaseDate.equals(that.purchaseDate) : that.purchaseDate != null) return false;
        return certificatesId != null ? certificatesId.equals(that.certificatesId) : that.certificatesId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (purchaseDate != null ? purchaseDate.hashCode() : 0);
        result = 31 * result + userId;
        result = 31 * result + (certificatesId != null ? certificatesId.hashCode() : 0);
        return result;
    }

}

