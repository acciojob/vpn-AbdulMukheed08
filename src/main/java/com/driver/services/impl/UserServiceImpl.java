package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{

        HashMap<String,String> countryCodeMap = new HashMap<>();
        countryCodeMap.put("ind","001");
        countryCodeMap.put("usa","002");
        countryCodeMap.put("aus","003");
        countryCodeMap.put("chi","004");
        countryCodeMap.put("jpn","005");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        countryName = countryName.toLowerCase();

        Country country = new Country();
        country.setCountryName(countryName);
        country.setCode(countryCodeMap.get(countryName));
        country.setUser(user);

        user.setOriginalIP(countryCodeMap.get(countryName)+"."+user.getId());

        userRepository3.save(user);

        return user;

    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {

        User user = userRepository3.findById(userId).get();

        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();

        user.setServiceProvider(serviceProvider);
        List<ServiceProvider> serviceProviderList = user.getListOfServiceProviders();
        serviceProviderList.add(serviceProvider);
        user.setListOfServiceProviders(serviceProviderList);

        List<User> userList = serviceProvider.getUsers();
        userList.add(user);
        serviceProvider.setUsers(userList);

        serviceProviderRepository3.save(serviceProvider);

        return user;
    }
}
