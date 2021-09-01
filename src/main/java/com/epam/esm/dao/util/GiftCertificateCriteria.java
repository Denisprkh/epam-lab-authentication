package com.epam.esm.dao.util;

import java.util.Set;

public class GiftCertificateCriteria {

    private String giftCertDesc;
    private String giftCertName;
    private String order;
    private Set<String> tagName;

    public String getGiftCertDesc() {
        return giftCertDesc;
    }

    public void setGiftCertDesc(String giftCertDesc) {
        this.giftCertDesc = giftCertDesc;
    }

    public String getGiftCertName() {
        return giftCertName;
    }

    public void setGiftCertName(String giftCertName) {
        this.giftCertName = giftCertName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Set<String> getTagName() {
        return tagName;
    }

    public void setTagName(Set<String> tagName) {
        this.tagName = tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftCertificateCriteria that = (GiftCertificateCriteria) o;

        if (giftCertDesc != null ? !giftCertDesc.equals(that.giftCertDesc) : that.giftCertDesc != null) return false;
        if (giftCertName != null ? !giftCertName.equals(that.giftCertName) : that.giftCertName != null) return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;
        return tagName != null ? tagName.equals(that.tagName) : that.tagName == null;
    }

    @Override
    public int hashCode() {
        int result = giftCertDesc != null ? giftCertDesc.hashCode() : 0;
        result = 31 * result + (giftCertName != null ? giftCertName.hashCode() : 0);
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (tagName != null ? tagName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GiftCertificateCriteria{");
        sb.append("giftCertDesc='").append(giftCertDesc).append('\'');
        sb.append(", giftCertName='").append(giftCertName).append('\'');
        sb.append(", order='").append(order).append('\'');
        sb.append(", tagName=").append(tagName);
        sb.append('}');
        return sb.toString();
    }
}
