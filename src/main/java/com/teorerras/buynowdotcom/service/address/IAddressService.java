package com.teorerras.buynowdotcom.service.address;

import com.teorerras.buynowdotcom.dtos.AddressDto;
import com.teorerras.buynowdotcom.model.Address;

import java.util.List;

public interface IAddressService {
    List<Address> createAddresses(List<Address> addressList);
    List<Address> getUserAddresses(Long userId);
    Address getAddressById(Long addressId);
    void deleteAddress(Long addressId);
    Address updateUserAddress(Long id, Address address);

    List<AddressDto> convertToDto(List<Address> addresses);

    AddressDto convertToDto(Address address);
}
