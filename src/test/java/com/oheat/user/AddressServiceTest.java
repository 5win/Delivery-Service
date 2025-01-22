package com.oheat.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.oheat.common.sido.Sido;
import com.oheat.common.sido.SidoNotExistsException;
import com.oheat.common.sido.SidoRepository;
import com.oheat.common.sigungu.Sigungu;
import com.oheat.common.sigungu.SigunguNotExistsException;
import com.oheat.common.sigungu.SigunguRepository;
import com.oheat.user.constant.Role;
import com.oheat.user.dto.AddressFindResponse;
import com.oheat.user.dto.AddressSaveRequest;
import com.oheat.user.entity.Address;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.repository.AddressRepository;
import com.oheat.user.repository.UserRepository;
import com.oheat.user.service.AddressService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AddressServiceTest {

    private final UserRepository memoryUserRepository = new MemoryUserRepository();
    private final AddressRepository memoryAddressRepository = new MemoryAddressRepository();

    private final SidoRepository sidoRepository = Mockito.mock(SidoRepository.class);
    private final SigunguRepository sigunguRepository = Mockito.mock(SigunguRepository.class);

    private AddressService addressService;

    @BeforeEach
    void setUp() {
        addressService = new AddressService(memoryUserRepository, memoryAddressRepository,
            sidoRepository, sigunguRepository);

        when(sidoRepository.findByCtpKorNm("서울특별시"))
            .thenReturn(Optional.of(generateSido()));
        when(sigunguRepository.findBySigKorNm("종로구"))
            .thenReturn(Optional.of(generateSigungu()));
    }

    @Test
    @DisplayName("존재하지 않는 시도 지역이면 SidoNotExistsException")
    void whenSidoNotExists_thenThrowSidoNotExistsException() {
        UserJpaEntity user = UserJpaEntity.builder()
            .username("sgoh")
            .password("pw")
            .role(Role.ADMIN)
            .build();
        memoryUserRepository.save(user);

        AddressSaveRequest addressSaveRequest = AddressSaveRequest.builder()
            .address("강원특별자치도 원주시")
            .detailAddress("000동 000호")
            .sido("강원특별자치도")
            .sigungu("원주시")
            .latitude(37.547889)
            .longitude(126.997128)
            .build();

        Assertions.assertThrows(SidoNotExistsException.class, () -> {
            addressService.registerAddress(addressSaveRequest, "sgoh");
        });
    }

    @Test
    @DisplayName("존재하지 않는 시군구 지역이면 SigunguNotExistsException")
    void whenSigunguNotExists_thenThrowSigunguNotExistsException() {
        UserJpaEntity user = UserJpaEntity.builder()
            .username("sgoh")
            .password("pw")
            .role(Role.ADMIN)
            .build();
        memoryUserRepository.save(user);

        AddressSaveRequest addressSaveRequest = AddressSaveRequest.builder()
            .address("서울특별시 광진구")
            .detailAddress("000동 000호")
            .sido("서울특별시")
            .sigungu("광진구")
            .latitude(37.547889)
            .longitude(126.997128)
            .build();

        Assertions.assertThrows(SigunguNotExistsException.class, () -> {
            addressService.registerAddress(addressSaveRequest, "sgoh");
        });
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
            .address("서울특별시 종로구")
            .detailAddress("000동 000호")
            .sido("서울특별시")
            .sigungu("종로구")
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
            .address("서울특별시 종로구")
            .detailAddress("000동 000호")
            .sido("서울특별시")
            .sigungu("종로구")
            .latitude(37.547889)
            .longitude(126.997128)
            .build();
        addressService.registerAddress(addressSaveRequest, "sgoh");
        addressService.registerAddress(addressSaveRequest, "sgoh");

        List<AddressFindResponse> result = addressService.findAllByUsername("sgoh");
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
            .address("서울특별시 종로구")
            .detailAddress("000동 000호")
            .sido("서울특별시")
            .sigungu("종로구")
            .latitude(37.547889)
            .longitude(126.997128)
            .selected(true)
            .build();
        addressService.registerAddress(addressSaveRequest, "sgoh");
        addressService.registerAddress(addressSaveRequest, "sgoh");

        addressService.changeSelectedAddress(2L, "sgoh");

        List<AddressFindResponse> result = addressService.findAllByUsername("sgoh");
        assertThat(result.get(0).isSelected()).isFalse();
        assertThat(result.get(1).isSelected()).isTrue();
    }

    Sido generateSido() {
        return Sido.builder()
            .ogrFid(1)
            .geometry(null)
            .ctprvnCd("11")
            .ctpKorNm("서울특별시")
            .ctpEngNm("Seoul")
            .build();
    }

    Sigungu generateSigungu() {
        return Sigungu.builder()
            .ogrFid(1)
            .geometry(null)
            .sigCd("11110")
            .sigKorNm("종로구")
            .sigEngNm("Jongno-gu")
            .build();
    }
}
