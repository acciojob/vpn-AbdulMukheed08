package com.driver.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private String username;

    private String password;

    private String originalIP;

    private String maskedIP;

    private boolean connected;

    private List<ServiceProvider> listOfServiceProviders = new ArrayList<>();

    private List<Connection> connectionList = new ArrayList<>();

    @ManyToMany
    @JoinColumn
    private ServiceProvider serviceProvider;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Connection connection;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Country country;

    public User() {
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

    public String getOriginalIP() {
        return originalIP;
    }

    public void setOriginalIP(String originalIP) {
        this.originalIP = originalIP;
    }

    public String getMaskedIP() {
        return maskedIP;
    }

    public void setMaskedIP(String maskedIP) {
        this.maskedIP = maskedIP;
    }

    public boolean getConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public List<ServiceProvider> getListOfServiceProviders() {
        return listOfServiceProviders;
    }

    public void setListOfServiceProviders(List<ServiceProvider> listOfServiceProviders) {
        this.listOfServiceProviders = listOfServiceProviders;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
