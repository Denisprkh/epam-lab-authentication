package com.epam.esm.controller.util;


public class AuthenticationResponse {

    private String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public AuthenticationResponse() {
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthenticationResponse that = (AuthenticationResponse) o;

        return jwt != null ? jwt.equals(that.jwt) : that.jwt == null;
    }

    @Override
    public int hashCode() {
        return jwt != null ? jwt.hashCode() : 0;
    }
}
