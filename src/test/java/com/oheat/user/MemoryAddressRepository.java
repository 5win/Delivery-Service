package com.oheat.user;

import com.oheat.user.entity.Address;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.repository.AddressRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemoryAddressRepository implements AddressRepository {

    private final Map<Long, Address> addressMap = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(Address address) {
        addressMap.put(autoId++, address);
    }

    @Override
    public Optional<Address> findById(Long addressId) {
        return Optional.ofNullable(addressMap.get(addressId));
    }

    @Override
    public List<Address> findAllByUser(UserJpaEntity user) {
        return addressMap.values().stream()
            .filter(address -> address.getUser().getUsername().equals(user.getUsername()))
            .toList();
    }

    @Override
    public void updateSelectedByUser(UserJpaEntity user, boolean selected) {
        for (Address address : addressMap.values()) {
            address.setSelected(selected);
        }
    }
}
