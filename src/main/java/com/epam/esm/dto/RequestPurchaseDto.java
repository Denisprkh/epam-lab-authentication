package com.epam.esm.dto;

import com.epam.esm.util.ResourceBundleErrorMessage;

import javax.validation.constraints.NotNull;
import java.util.List;

public class RequestPurchaseDto {

    @NotNull(message = ResourceBundleErrorMessage.PURCHASE_USER_ID_REQUIRED)
    private Integer userId;

    @NotNull(message = ResourceBundleErrorMessage.PURCHASE_CERTIFICATES_ID_REQUIRED)
    private List<Integer> certificatesId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getCertificatesId() {
        return certificatesId;
    }

    public void setCertificatesId(List<Integer> certificatesId) {
        this.certificatesId = certificatesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestPurchaseDto that = (RequestPurchaseDto) o;

        if (userId != that.userId) return false;
        return certificatesId != null ? certificatesId.equals(that.certificatesId) : that.certificatesId == null;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (certificatesId != null ? certificatesId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RequestOrderDto{");
        sb.append("userId=").append(userId);
        sb.append(", certificatesId=").append(certificatesId);
        sb.append('}');
        return sb.toString();
    }
}
