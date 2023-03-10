package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);

        adminRepository1.save(admin);

        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {

        Admin admin = adminRepository1.findById(adminId).get();

        ServiceProvider serviceProvider = serviceProviderRepository1.findByName(providerName);

        serviceProvider.setAdmin(admin);

        //admin.setServiceProvider(serviceProvider);
        List<ServiceProvider> listOfServiceProviders = admin.getServiceProviders();
        listOfServiceProviders.add(serviceProvider);
        admin.setServiceProviders(listOfServiceProviders);

        adminRepository1.save(admin);

        return admin;

    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{

        countryName = countryName.toUpperCase();

        HashMap<String,String> countryCodeMap = new HashMap<>();
        countryCodeMap.put("IND","001");
        countryCodeMap.put("USA","002");
        countryCodeMap.put("AUS","003");
        countryCodeMap.put("CHI","004");
        countryCodeMap.put("JPN","005");


        if(countryCodeMap.containsKey(countryName)){
            Country country = new Country();

            ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();

            if(countryName.equalsIgnoreCase("IND")){
                country.setCountryName(CountryName.IND);
                country.setCode(CountryName.IND.toCode());
            }
            if(countryName.equalsIgnoreCase("USA")){
                country.setCountryName(CountryName.USA);
                country.setCode(CountryName.USA.toCode());
            }
            if(countryName.equalsIgnoreCase("JPN")){
                country.setCountryName(CountryName.JPN);
                country.setCode(CountryName.JPN.toCode());
            }
            if(countryName.equalsIgnoreCase("CHI")){
                country.setCountryName(CountryName.CHI);
                country.setCode(CountryName.CHI.toCode());
            }
            if(countryName.equalsIgnoreCase("AUS")){
                country.setCountryName(CountryName.AUS);
                country.setCode(CountryName.AUS.toCode());
            }
            country.setServiceProvider(serviceProvider);
            serviceProvider.getCountryList().add(country);
            serviceProviderRepository1.save(serviceProvider);

            return serviceProvider;
        }
        else{
            throw new Exception("Country not found");
        }
    }
}
