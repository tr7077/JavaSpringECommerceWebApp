package com.teorerras.buynowdotcom.service.address;

import com.teorerras.buynowdotcom.dtos.AddressDto;
import com.teorerras.buynowdotcom.model.Address;
import com.teorerras.buynowdotcom.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService{

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Address> createAddresses(List<Address> addressList) {
        return addressRepository.saveAll(addressList);
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    public Address getAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.findById(addressId)
                .ifPresentOrElse(addressRepository::delete, () -> {
                    throw new EntityNotFoundException("Address not found");
                });
    }

    @Override
    public Address updateUserAddress(Long id, Address address) {
        return addressRepository.findById(id).map(oldAddress -> {
            oldAddress.setCountry(address.getCountry());
            oldAddress.setState(address.getState());
            oldAddress.setCity(address.getCity());
            oldAddress.setStreet(address.getStreet());
            oldAddress.setAddressType(address.getAddressType());
            return addressRepository.save(oldAddress);
        }).orElseThrow(
                () -> new EntityNotFoundException("Address not found")
        );
    }

    @Override
    public List<AddressDto> convertToDto(List<Address> addresses) {
        return addresses.stream().map(this::convertToDto).toList();
    }

    @Override
    public AddressDto convertToDto(Address address) {
        AddressDto addressDto = modelMapper.map(address, AddressDto.class);
        return addressDto;
    }
}
