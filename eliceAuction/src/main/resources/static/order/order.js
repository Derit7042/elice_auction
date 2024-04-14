document.addEventListener('DOMContentLoaded', function () {
  const searchAddressButton = document.getElementById('searchAddressButton');
  const checkoutButton = document.getElementById('checkoutButton');
  const urlParams = new URLSearchParams(window.location.search);
  const productName = urlParams.get('productName');
  const productPrice = urlParams.get('productPrice');

  // 상품 정보를 화면에 표시
  document.getElementById('productName').innerText = productName;
  document.getElementById('productPrice').innerText = productPrice;
  document.getElementById('orderTotal').innerText = productPrice;

  // 주소 찾기 버튼에 이벤트 리스너 등록
  searchAddressButton.addEventListener('click', function () {
    new daum.Postcode({
      oncomplete: function (data) {
        document.getElementById('postalCode').value = data.zonecode;
        document.getElementById('address1').value = data.address;
        document.getElementById('address2').focus();
      }
    }).open();
  });

  // 결제하기 버튼에 이벤트 리스너 등록
  checkoutButton.addEventListener('click', async function () {
    const postalCode = document.getElementById('postalCode').value;
    const address1 = document.getElementById('address1').value;
    const address2 = document.getElementById('address2').value;

    if (!postalCode || !address1 || !address2) {
      alert('모든 필드를 입력해주세요.');
      return;
    }

    const accessToken = sessionStorage.getItem('accessToken');
    if (!accessToken) {
      alert('로그인이 필요합니다.');
      window.location.href = '/login/login.html'; // 로그인 페이지로 리다이렉션
      return;
    }

    try {
      const memberAddressId = await createMemberAddress({
        name: '송호진', // 예시 이름
        address: `${postalCode} ${address1} ${address2}`
      }, accessToken);

      const orderData = {
        productId: urlParams.get('productId'),
        memberAddressId: memberAddressId
      };

      await createOrder(orderData, accessToken);
      window.location.href = '/order-complete/order-complete.html';
    } catch (error) {
      console.error('주문 처리 중 문제가 발생했습니다:', error);
      alert('주문 처리 중 문제가 발생했습니다. 다시 시도해 주세요.');
    }
  });
});

// 배송지 정보 생성 및 ID 반환 함수
async function createMemberAddress(addressData, token) {
  const response = await fetch('/api/orders/delivery/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(addressData)
  });

  if (!response.ok) {
    const errorData = await response.text();
    console.error('배송지 생성 실패:', errorData);
    throw new Error('배송지 생성 실패.');
  }

  const data = await response.json();
  return data.id; // 서버에서 ID 필드를 반환한다고 가정
}

// 주문 생성 함수
async function createOrder(orderData, accessToken) {
  const response = await fetch('/api/orders/member/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${accessToken}`
    },
    body: JSON.stringify(orderData)
  });

  if (!response.ok) {
    const errorData = await response.text();
    console.error('주문 생성 실패:', errorData);
    throw new Error('주문 생성에 실패하였습니다.');
  }

  return response.json();
}
