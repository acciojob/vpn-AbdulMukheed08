package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;
    @Autowired
    CountryRepository countryRepository2;


    @Override
    public User connect(int userId, String countryName) throws Exception{

        HashMap<String,String> countryCodeMap = new HashMap<>();
        countryCodeMap.put("ind","001");
        countryCodeMap.put("usa","002");
        countryCodeMap.put("aus","003");
        countryCodeMap.put("chi","004");
        countryCodeMap.put("jpn","005");



        User user = userRepository2.findById(userId).get();


        //if user is already connected
        if(user.getConnected()){
            throw new Exception("Already connected");
        }

        //if user country is same as connection country
        Country userCountry = user.getCountry();
        countryName = countryName.toLowerCase();
        if(countryName.equals(userCountry.getCountryName())){
            return user;
        }


        List<ServiceProvider> listOfServiceProvidersInCountry = new ArrayList<>();

        //
        List<ServiceProvider> listOfServiceProviders = user.getListOfServiceProviders();
        for(ServiceProvider serviceProvider : listOfServiceProviders){
            //getting list of countries for given service provider
            List<Country> countryListOfServiceProvider = serviceProvider.getCountryList();
            for(Country serviceCountry : countryListOfServiceProvider){
                if(serviceCountry.getCountryName().equals(countryName)){
                    listOfServiceProvidersInCountry.add(serviceProvider);
                    break;
                }
            }
        }

        if(listOfServiceProvidersInCountry.size() == 0){
            throw new Exception("Unable to connect");
        }

        int serviceProviderId = Integer.MAX_VALUE;
        for(ServiceProvider serviceProvider : listOfServiceProvidersInCountry){
            serviceProviderId = Math.min(serviceProviderId,serviceProvider.getId());
        }

        user.setConnected(true);
        user.setMaskedIP(countryCodeMap.get(countryName)+"."+serviceProviderId+"."+userId);

        ServiceProvider serviceProvider = serviceProviderRepository2.findById(serviceProviderId).get();

        Connection connection = new Connection();
        connection.setUser(user);
        connection.setServiceProvider(serviceProvider);

        user.setServiceProvider(serviceProvider);
        user.setConnection(connection);

        serviceProviderRepository2.save(serviceProvider);

        return user;
    }
    @Override
    public User disconnect(int userId) throws Exception {

        User user = userRepository2.findById(userId).get();
        if(user.getConnected() == false){
            throw new Exception("already disconnected");
        }
        Connection connection = user.getConnection();
        connectionRepository2.delete(connection);

        user.setMaskedIP(null);
        user.setConnected(false);

        userRepository2.save(user);

        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {

        HashMap<String,String> countryCodeMap = new HashMap<>();
        countryCodeMap.put("001","ind");
        countryCodeMap.put("002","usa");
        countryCodeMap.put("003","aus");
        countryCodeMap.put("004","chi");
        countryCodeMap.put("005","jpn");

        HashMap<String,String> countryCodeMap1 = new HashMap<>();
        countryCodeMap1.put("ind","001");
        countryCodeMap1.put("usa","002");
        countryCodeMap1.put("aus","003");
        countryCodeMap1.put("chi","004");
        countryCodeMap1.put("jpn","005");


        User sender = userRepository2.findById(senderId).get();
        User receiver = userRepository2.findById(receiverId).get();

        String receiverCountry ="";
        if(!receiver.getConnected()){
            receiverCountry = receiver.getCountry().getCountryName();
        }
        else{
            String countryCode = receiver.getMaskedIP().substring(0,3);
            receiverCountry = countryCodeMap.get(countryCode);
        }

        String senderCountry = sender.getCountry().getCountryName();

        if(senderCountry.equals(receiverCountry)) return sender;

        List<ServiceProvider> senderServiceProviders = new ArrayList<>();

        List<ServiceProvider> serviceProviderList = new ArrayList<>();
        serviceProviderList = sender.getListOfServiceProviders();
        for(ServiceProvider serviceProvider : serviceProviderList){
            List<Country> countryList = new ArrayList<>();
            countryList = serviceProvider.getCountryList();
            for(Country country : countryList){
                if(country.getCountryName().equals(receiverCountry)){
                    senderServiceProviders.add(serviceProvider);
                    break;
                }
            }
        }
        if(senderServiceProviders.size() == 0) {
            throw new Exception("Cannot establish communication");
        }
        int senderServiceProviderId = Integer.MAX_VALUE;
        for(ServiceProvider serviceProvider : senderServiceProviders){
            senderServiceProviderId = Math.min(senderServiceProviderId,serviceProvider.getId());
        }

        ServiceProvider serviceProvider = serviceProviderRepository2.findById(senderServiceProviderId).get();

        Connection connection = new Connection();
        connection.setUser(sender);
        connection.setServiceProvider(serviceProvider);

        sender.setConnected(true);
        sender.setMaskedIP(countryCodeMap1.get(receiverCountry)+"."+senderServiceProviderId+"."+senderId);

        serviceProviderRepository2.save(serviceProvider);

        return sender;
    }
}
