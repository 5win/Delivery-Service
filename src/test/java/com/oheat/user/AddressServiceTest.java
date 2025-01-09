package com.oheat.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.oheat.user.constant.Role;
import com.oheat.user.dto.AddressSaveRequest;
import com.oheat.user.entity.Address;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.repository.AddressRepository;
import com.oheat.user.repository.UserRepository;
import com.oheat.user.service.AddressService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AddressServiceTest {

    private final UserRepository memoryUserRepository = new MemoryUserRepository();
    private final AddressRepository memoryAddressRepository = new MemoryAddressRepository();
    private AddressService addressService;

    @BeforeEach
    void setUp() {
        addressService = new AddressService(memoryUserRepository, memoryAddressRepository);
    }

    @Test
    @DisplayName("배달시킬 주소를 새로 등록한다")
    void whenRegisterAddress_thenSuccess() {
        UserJpaEntity user = UserJpaEntity.builder()
            .username("sgoh")
            .password("pw")
            .role(Role.ADMIN)
            .build();
        memoryUserRepository.save(user);

        AddressSaveRequest addressSaveRequest = AddressSaveRequest.builder()
            .address("서울특별시 광진구")
            .detailAddress("000동 000호")
            .latitude(37.547889)
            .longitude(126.997128)
            .build();
        addressService.registerAddress(addressSaveRequest, "sgoh");

        Optional<Address> result = memoryAddressRepository.findById(1L);
        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("주소지 목록을 조회한다")
    void whenFindAllAddress_thenReturnAddressList() {
        UserJpaEntity user = UserJpaEntity.builder()
            .username("sgoh")
            .password("pw")
            .role(Role.ADMIN)
            .build();
        memoryUserRepository.save(user);

        AddressSaveRequest addressSaveRequest = AddressSaveRequest.builder()
            .address("서울특별시 광진구")
            .detailAddress("000동 000호")
            .latitude(37.547889)
            .longitude(126.997128)
            .build();
        addressService.registerAddress(addressSaveRequest, "sgoh");
        addressService.registerAddress(addressSaveRequest, "sgoh");

        List<Address> result = addressService.findAllByUsername("sgoh");
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("기존에 선택된 주소를 선택 해제하고, 요청된 주소를 현재 주소로 선택한다.")
    void whenChangeSelectedAddress_thenDeselectAllAddressAndSelectRequestAddress() {
        UserJpaEntity user = UserJpaEntity.builder()
            .username("sgoh")
            .password("pw")
            .role(Role.ADMIN)
            .build();
        memoryUserRepository.save(user);

        AddressSaveRequest addressSaveRequest = AddressSaveRequest.builder()
            .address("서울특별시 광진구")
            .detailAddress("000동 000호")
            .latitude(37.547889)
            .longitude(126.997128)
            .selected(true)
            .build();
        addressService.registerAddress(addressSaveRequest, "sgoh");
        addressService.registerAddress(addressSaveRequest, "sgoh");

        addressService.changeSelectedAddress(2L, "sgoh");

        List<Address> result = addressService.findAllByUsername("sgoh");
        assertThat(result.get(0).isSelected()).isFalse();
        assertThat(result.get(1).isSelected()).isTrue();
    }
}
