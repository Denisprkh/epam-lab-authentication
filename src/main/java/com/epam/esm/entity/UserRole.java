package com.epam.esm.entity;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited
@Table(name = "role")
public class UserRole {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_default")
    private Boolean isDefault;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole userRole = (UserRole) o;

        if (id != null ? !id.equals(userRole.id) : userRole.id != null) return false;
        if (name != null ? !name.equals(userRole.name) : userRole.name != null) return false;
        return isDefault != null ? isDefault.equals(userRole.isDefault) : userRole.isDefault == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isDefault != null ? isDefault.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserRole{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", isDefault=").append(isDefault);
        sb.append('}');
        return sb.toString();
    }
}
