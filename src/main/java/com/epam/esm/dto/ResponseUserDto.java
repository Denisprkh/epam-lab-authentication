package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

public class ResponseUserDto extends RepresentationModel<ResponseUserDto> {

    private int id;

    private String login;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResponseUserDto that = (ResponseUserDto) o;

        if (id != that.id) return false;
        return login != null ? login.equals(that.login) : that.login == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResponseUserDto{");
        sb.append("id=").append(id);
        sb.append(", login='").append(login).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
