package com.teorerras.buynowdotcom.controller;

import com.teorerras.buynowdotcom.dtos.AddressDto;
import com.teorerras.buynowdotcom.model.Address;
import com.teorerras.buynowdotcom.response.ApiResponse;
import com.teorerras.buynowdotcom.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/addresses")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/new")
    public ResponseEntity<ApiResponse> createAddresses(@RequestBody List<Address> addresses) {
        List<Address> addressList = addressService.createAddresses(addresses);
        List<AddressDto> addressDtos = addressService.convertToDto(addressList);
        return ResponseEntity.ok(new ApiResponse("Success", addressDtos));
    }

    @GetMapping("/{userId}/address")
    public ResponseEntity<ApiResponse> getUserAddresses(@PathVariable Long userId) {
        List<Address> addressList = addressService.getUserAddresses(userId);
        List<AddressDto> addressDtos = addressService.convertToDto(addressList);
        return ResponseEntity.ok(new ApiResponse("Found", addressDtos));
    }

    @GetMapping("/{addressId}/address")
    public ResponseEntity<ApiResponse> getAddressById(@PathVariable Long addressId) {
        Address address = addressService.getAddressById(addressId);
        AddressDto addressDto = addressService.convertToDto(address);
        return ResponseEntity.ok(new ApiResponse("Found", addressDto));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> getAddressById(@PathVariable Long id, @RequestBody Address address) {
        Address newAddress = addressService.updateUserAddress(id, address);
        AddressDto addressDto = addressService.convertToDto(newAddress);
        return ResponseEntity.ok(new ApiResponse("Updated", addressDto));
    }

    @DeleteMapping("/{addressId}/delete")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok(new ApiResponse("Address deleted", null));
    }
}
