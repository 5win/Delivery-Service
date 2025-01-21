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

  // 3. 카테고리 조회
  const categoryUrl = baseUrl + '/foods/categories';
  const categoryRes = http.get(categoryUrl);

  check(categoryRes, {
    '카테고리 조회 성공': (res) => res.status === 200,
    '치킨 카테고리 존재': (res) => JSON.parse(res.body)[0]?.name === '치킨'
  });

  sleep(1);

  // 4. 카테고리로 최신 등록 순 매장 조회
  let category = encodeURIComponent('치킨');
  let sort = encodeURIComponent('id');
  let page = encodeURIComponent(0);
  let size = encodeURIComponent(10);
  const findShopOrderByIdRes = http.get(
      baseUrl + `/foods/shops?category=${category}&sort=${sort}&page=${page}&size=${size}`);

  check(findShopOrderByIdRes, {
    '최신 등록순 매장 조회 성공': (res) => res.status === 200,
    '조회된 페이지 사이즈 10': (res) => JSON.parse(res.body)?.content?.length === 10
  });

  sleep(1);

  // 5. 가까운 거리 순 매장 조회
  sort = encodeURIComponent('distance,id');
  const latitude = (Math.random() * (38.6 - 33.0)) + 33.0;
  const longitude = (Math.random() * (131.0 - 124.0)) + 124.0;
  const findShopOrderByDistRes = http.get(baseUrl
      + `/foods/shops?category=${category}&sort=${sort}&latitude=${latitude}&longitude=${longitude}&page=${page}&size=${size}`);

  check(findShopOrderByDistRes, {
    '가까운 거리 순 매장 조회 성공': (res) => res.status === 200,
    '조회된 페이지 사이즈 10': (res) => JSON.parse(res.body)?.content?.length === 10
  });
}
