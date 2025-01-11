package com.oheat.user.repository;

import com.oheat.user.entity.Address;
import com.oheat.user.entity.UserJpaEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AddressRepositoryImpl implements AddressRepository {

    private final AddressJpaRepository addressJpaRepository;

    @Override
    public void save(Address address) {
        addressJpaRepository.save(address);
    }

    @Override
    public Optional<Address> findById(Long addressId) {
        return addressJpaRepository.findById(addressId);
    }

    @Override
    public List<Address> findAllByUser(UserJpaEntity user) {
        return addressJpaRepository.findAllByUser(user);
    }

    @Override
    public void updateSelectedByUser(UserJpaEntity user, boolean selected) {
        addressJpaRepository.updateSelectedByUser(user, selected);
    }
}
