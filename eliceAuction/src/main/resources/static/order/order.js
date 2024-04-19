import * as Api from "../api.js";
import {
  addCommas,
  navigate,
  createNavbar,
} from "../useful-functions.js";

// 페이지 요소 선택
const subtitleCart = document.querySelector("#subtitleCart");
const receiverNameInput = document.querySelector("#receiverName");
const postalCodeInput = document.querySelector("#postalCode");
const searchAddressButton = document.querySelector("#searchAddressButton");
const address1Input = document.querySelector("#address1");
const address2Input = document.querySelector("#address2");
const productsTitleElem = document.querySelector("#productName");
const productsTotalElem = document.querySelector("#productPrice");
const orderTotalElem = document.querySelector("#orderTotal");
const checkoutButton = document.querySelector("#checkoutButton");

createNavbar();
addAllEvents();

function addAllEvents() {
  subtitleCart.addEventListener("click", () => navigate("/cart"));
  searchAddressButton.addEventListener("click", searchAddress);
  checkoutButton.addEventListener("click", doCheckout);
}

function searchAddress() {
  new daum.Postcode({
    oncomplete: data => {
      const addr = data.userSelectedType === "R" ? data.roadAddress : data.jibunAddress;
      let extraAddr = data.userSelectedType === "R" && data.bname ? `${data.bname}, ${data.buildingName || ''}`.trim() : '';
      postalCodeInput.value = data.zonecode;
      address1Input.value = `${addr}${extraAddr ? ` (${extraAddr})` : ''}`;
      address2Input.placeholder = "상세 주소를 입력해 주세요.";
      address2Input.focus();
    }
  }).open();
}


async function doCheckout() {
  const receiverName = receiverNameInput.value.trim();
  const address = `${address1Input.value.trim()} ${address2Input.value.trim()}`;
  const postalCode = postalCodeInput.value.trim();

  if (!receiverName || !postalCode || !address) {
    alert("모든 배송 정보를 입력해 주세요.");
    return;
  }

  const memberId = 1;
  if (!memberId) {
    alert("사용자 인증에 실패했습니다. 다시 로그인해주세요.");
    return; // 회원 ID가 없으면 함수 종료
  }
// 음성 테스트
  try {
    // 배송지 정보 생성 요청
    const memberAddressResponse = await Api.post("/orders/delivery/create", {
      name: receiverName,
      address: address,
      memberId: memberId  // 로그인된 사용자의 ID 사용
    });

    if (!memberAddressResponse || !memberAddressResponse.id) throw new Error("배송지 정보 생성 실패");

    const memberAddressId = memberAddressResponse.id;  // 생성된 배송지 ID
    const URLSearch = new URLSearchParams(window.location.search);
    const productId = URLSearch.get("id");

    // 로그인 한 사용자만 주문할 수 있게 함
    let isLogin = false;// let, var, const 변수 선언
    const token = sessionStorage.getItem("token");
    if (!token) {// 로그인 안됬을때
      alert("로그인 한 사용자만 주문할 수 있습니다");
    } else{// 로그인 됬을때
      isLogin = true;
    }

    let orderResponse;
    if(isLogin){
      orderResponse = await Api.post("/orders/member/create", {
        productId: productId,
        memberId: memberId,
        memberAddressId: memberAddressId  // 배송지 ID 사용
      });
    } else{
      orderResponse = null;
    }

    // const orderResponse = await Api.post("/orders/member/create", {
    //   productId: productId,
    //   memberId: memberId,
    //   memberAddressId: memberAddressId  // 배송지 ID 사용
    // });

    if (!orderResponse) throw new Error("주문 생성 실패");

    alert("주문이 성공적으로 처리되었습니다.");
    navigate("/order/complete")();  // 주문 완료 페이지로 이동
  } catch (err) {
    console.error("주문 처리 중 오류 발생", err);
    alert(`오류: ${err.message}`);
  }
}

document.addEventListener('DOMContentLoaded', insertOrderSummary);

async function insertOrderSummary() {
  const URLSearch = new URLSearchParams(window.location.search);
  const productId = URLSearch.get("id");
  const product = await fetchProductDetails(productId);

  if (product && product.price !== undefined) {
    const totalPrice = product.price;
    productsTitleElem.textContent = product.title;
    productsTotalElem.textContent = `${addCommas(totalPrice)}원`;
    orderTotalElem.textContent = `${addCommas(totalPrice)}원`;
  } else {
    console.error('상품 정보를 불러올 수 없습니다.', product);
    alert('상품 정보를 불러올 수 없습니다.');
  }
}

async function fetchProductDetails(id) {
  try {
    const response = await Api.get(`/products/${id}`);
    if (response) {
      return response;
    } else {
      console.error('서버로부터 상품 정보를 받지 못했습니다.', response);
      return null;
    }
  } catch (err) {
    console.error("상품 정보 로딩 중 오류 발생", err);
    alert("상품 정보를 불러오는 중 오류가 발생했습니다.");
    return null;
  }
}



const token = sessionStorage.getItem("token");
if (!token) {
  alert("로그인 한 사용자만 주문할 수 있습니다");
} else{
  // 주문하는 코드
}
