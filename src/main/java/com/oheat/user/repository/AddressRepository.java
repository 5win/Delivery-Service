package com.oheat.user.repository;

import com.oheat.user.entity.Address;
import com.oheat.user.entity.UserJpaEntity;
import java.util.List;
import java.util.Optional;

public interface AddressRepository {

    void save(Address address);

    Optional<Address> findById(Long addressId);

    List<Address> findAllByUser(UserJpaEntity user);

    void updateSelectedByUser(UserJpaEntity user, boolean selected);
}
