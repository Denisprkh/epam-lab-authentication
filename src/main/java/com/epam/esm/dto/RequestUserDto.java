package com.epam.esm.dto;

import com.epam.esm.util.ResourceBundleErrorMessage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RequestUserDto {

    @NotBlank(message = ResourceBundleErrorMessage.LOGIN_REQUIRED)
    @Pattern(regexp = "^.{2,70}$", message = ResourceBundleErrorMessage.LOGIN_FORMAT)
    private String login;

    @NotBlank(message = ResourceBundleErrorMessage.PASSWORD_REQUIRED)
    @Pattern(regexp = "^.{2,30}$", message = ResourceBundleErrorMessage.PASSWORD_FORMAT)
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestUserDto that = (RequestUserDto) o;

        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RequestUserDto{");
        sb.append("login='").append(login).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
