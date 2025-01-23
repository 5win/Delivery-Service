import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
  scenarios: {
    order_smoke_test: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        {duration: '10s', target: 20},
        {duration: '20s', target: 20}
      ]
    },
  },
};

export default function () {
  const baseUrl = 'http://3.37.32.242:8080/api/v1';

  // 1. 회원가입
  const registerUrl = baseUrl + '/users/register';
  const registerPayload = JSON.stringify({
    username: `user${__VU}`,
    password: 'pw',
    phone: '010-1234-1234',
    role: 'ADMIN'
  });

  const registerParams = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  http.post(registerUrl, registerPayload, registerParams);

  sleep(1);

  // 2. login -> JWT 저장
  const loginUrl = baseUrl + '/users/login'
  const loginPayload = JSON.stringify({
    username: `user${__VU}`,
    password: 'pw',
  });

  const loginParams = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const loginRes = http.post(loginUrl, loginPayload, loginParams);

  check(loginRes, {
    '로그인 성공': (res) => res.status === 200,
    'JWT 토큰 수신': (res) => !!res.headers['Token'], // Ensure token exists
  });

  const jwt = loginRes.headers['Token'];
  if (!jwt) {
    console.error(`VU ${__VU} 로그인 실패`);
    return;
  }

  sleep(1);

  // 3. 주소 등록
  const addrUrl = baseUrl + '/users/addresses'

  // 홀수는 서울, 짝수는 부산
  let latitude, longitude;
  const sido = (__VU % 2 === 1) ? "서울특별시" : "부산광역시";
  const sigungu = (__VU % 2 === 1) ? "종로구" : "해운대구";

  if (sido === "서울") {
    latitude = (Math.random() * (37.7 - 37.4)) + 37.4; // 37.4 ~ 37.7
    longitude = (Math.random() * (127.2 - 126.8)) + 126.8; // 126.8 ~ 127.2
  } else {
    latitude = (Math.random() * (35.3 - 35.0)) + 35.0; // 35.0 ~ 35.3
    longitude = (Math.random() * (129.2 - 128.8)) + 128.8; // 128.8 ~ 129.2
  }

  const addrPayload = JSON.stringify({
    address: `${sido} ${sigungu}`,
    detailAddress: "상세 주소",
    sido: sido,
    sigungu: sigungu,
    latitude: latitude,
    longitude: longitude,
    nickname: `${__VU}'s Home`,
    selected: true
  });

  const addrRegParams = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + jwt
    }
  }

  const addrRes = http.post(addrUrl, addrPayload, addrRegParams)

  sleep(1);

  // 4. 카테고리 조회
  const categoryUrl = baseUrl + '/foods/categories';
  const categoryRes = http.get(categoryUrl);

  check(categoryRes, {
    '카테고리 조회 성공': (res) => res.status === 200 && JSON.parse(res.body)[0]?.name === '치킨',
  });

  sleep(1);

  // 5. 카테고리로 최신 등록 순 매장 조회
  let category = encodeURIComponent('치킨');
  let sort = encodeURIComponent('id');
  let page = encodeURIComponent(0);
  let size = encodeURIComponent(10);
  const findShopOrderByIdRes = http.get(
      baseUrl + `/foods/shops?category=${category}&sort=${sort}&page=${page}&size=${size}`);

  check(findShopOrderByIdRes, {
    '최신 등록순 매장 조회 성공': (res) => res.status === 200,
  });

  sleep(1);

  // 6. 가까운 거리 순 매장 조회
  sort = encodeURIComponent('distance,id');
  const findShopOrderByDistRes = http.get(baseUrl
      + `/foods/shops?category=${category}&sort=${sort}&latitude=${latitude}&longitude=${longitude}&page=${page}&size=${size}`);

  check(findShopOrderByDistRes, {
    '가까운 거리 순 매장 조회 성공': (res) => res.status === 200,
  });

  sleep(1);

  // 7. VU 번호에 해당하는 id 매장의 메뉴 그룹 및 메뉴 조회
  const menuGroupRes = http.get(baseUrl + `/foods/shops/menu-groups/${__VU}`);

  check(menuGroupRes, {
    '메뉴 그룹 조회 성공': (res) => res.status === 200,
  })
}
