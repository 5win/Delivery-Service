package com.oheat.user.repository;

import com.oheat.user.entity.Address;
import com.oheat.user.entity.UserJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressJpaRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByUser(UserJpaEntity user);

    @Modifying
    @Query("UPDATE Address a SET a.selected = :selected WHERE a.user = :user")
    void updateSelectedByUser(@Param("user") UserJpaEntity user, @Param("selected") boolean selected);
}
