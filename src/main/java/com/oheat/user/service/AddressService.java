package com.oheat.user.service;

import com.oheat.common.sido.Sido;
import com.oheat.common.sido.SidoNotExistsException;
import com.oheat.common.sido.SidoRepository;
import com.oheat.common.sigungu.Sigungu;
import com.oheat.common.sigungu.SigunguNotExistsException;
import com.oheat.common.sigungu.SigunguRepository;
import com.oheat.user.dto.AddressFindResponse;
import com.oheat.user.dto.AddressSaveRequest;
import com.oheat.user.entity.Address;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.exception.AddressNotExistsException;
import com.oheat.user.exception.UserNotExistsException;
import com.oheat.user.repository.AddressRepository;
import com.oheat.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final SidoRepository sidoRepository;
    private final SigunguRepository sigunguRepository;

    public void registerAddress(AddressSaveRequest saveRequest, String username) {
        UserJpaEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotExistsException::new);

        Sido sido = sidoRepository.findByCtpKorNm(saveRequest.getSido())
            .orElseThrow(() -> new SidoNotExistsException(HttpStatus.BAD_REQUEST, "존재하지 않는 시도 지역입니다."));
        Sigungu sigungu = sigunguRepository.findBySigKorNm(saveRequest.getSigungu())
            .orElseThrow(() -> new SigunguNotExistsException(HttpStatus.BAD_REQUEST, "존재하지 않는 시군구 지역입니다."));

        addressRepository.save(saveRequest.toEntity(user, sido, sigungu));
    }

    public List<AddressFindResponse> findAllByUsername(String username) {
        UserJpaEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotExistsException::new);

        return addressRepository.findAllByUser(user)
            .stream().map(AddressFindResponse::from)
            .toList();
    }

    @Transactional
    public void changeSelectedAddress(Long addressId, String username) {
        UserJpaEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotExistsException::new);

        addressRepository.updateSelectedByUser(user, false);
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new AddressNotExistsException(HttpStatus.BAD_REQUEST, "존재하지 않는 주소입니다."));
        address.setSelected(true);
    }
}
