package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<ServiceProvider> listOfServiceProviders = admin.getListOfServiceProviders();
        listOfServiceProviders.add(serviceProvider);
        admin.setListOfServiceProviders(listOfServiceProviders);

        adminRepository1.save(admin);

        return admin;

    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{

        ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();
        List<Country> countryList = serviceProvider.getCountryList();

        String[] countryArr = new String[] {"ind", "aus", "usa", "chi", "jpn"};

        countryName = countryName.toLowerCase();

        for(String countryFromList : countryArr){
            if(countryName.equals(countryFromList)){
                Country country = new Country();
                country.setCountryName(countryName);

                countryList.add(country);
                serviceProvider.setCountryList(countryList);
                serviceProviderRepository1.save(serviceProvider);
            }
        }

        throw new Exception("Country not found");
    }
}
