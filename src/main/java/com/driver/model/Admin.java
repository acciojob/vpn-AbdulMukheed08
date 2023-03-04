package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private String username;
    private String password;
    List<ServiceProvider> listOfServiceProviders = new ArrayList<>();

    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    private ServiceProvider serviceProvider;


    public Admin() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ServiceProvider> getListOfServiceProviders() {
        return listOfServiceProviders;
    }

    public void setListOfServiceProviders(List<ServiceProvider> listOfServiceProviders) {
        this.listOfServiceProviders = listOfServiceProviders;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
