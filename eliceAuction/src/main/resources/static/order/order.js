import * as Api from "../api.js";
import {
  addCommas,
  convertToNumber,
  navigate,
  createNavbar,
} from "../useful-functions.js";

// 요소(element), input 혹은 상수
const subtitleCart = document.querySelector("#subtitleCart");
const receiverNameInput = document.querySelector("#receiverName");
const postalCodeInput = document.querySelector("#postalCode");
const searchAddressButton = document.querySelector("#searchAddressButton");
const address1Input = document.querySelector("#address1");
const address2Input = document.querySelector("#address2");
const requestSelectBox = document.querySelector("#requestSelectBox");
const customRequestInput = document.querySelector("#customRequest");
const productsTitleElem = document.querySelector("#productName");
const productsTotalElem = document.querySelector("#productPrice");
const deliveryFeeElem = document.querySelector("#deliveryFee");
const orderTotalElem = document.querySelector("#orderTotal");
const checkoutButton = document.querySelector("#checkoutButton");

let selectedProductId; // 사용자가 선택한 상품의 ID를 저장할 변수

createNavbar();
addAllEvents();

async function addAllEvents() {
  subtitleCart.addEventListener("click", () => navigate("/cart"));
  searchAddressButton.addEventListener("click", searchAddress);
  checkoutButton.addEventListener("click", () => doCheckout(selectedProductId));
}

function searchAddress() {
  new daum.Postcode({
    oncomplete: function (data) {
      const addr = data.userSelectedType === "R" ? data.roadAddress : data.jibunAddress;
      let extraAddr = "";

      if (data.userSelectedType === "R") {
        if (data.bname && /[동|로|가]$/g.test(data.bname)) {
          extraAddr += data.bname;
        }
        if (data.buildingName && data.apartment === "Y") {
          extraAddr += extraAddr ? `, ${data.buildingName}` : data.buildingName;
        }
      }
      postalCodeInput.value = data.zonecode;
      address1Input.value = `${addr} ${extraAddr ? `(${extraAddr})` : ''}`;
      address2Input.placeholder = "상세 주소를 입력해 주세요.";
      address2Input.focus();
    }
  }).open();
}

async function doCheckout(productId) {
  const receiverName = receiverNameInput.value.trim();
  const address = address1Input.value.trim() + ' ' + address2Input.value.trim();
  const postalCode = postalCodeInput.value.trim();

  if (!receiverName || !postalCode || !address) {
    alert("모든 배송 정보를 입력해 주세요.");
    return;
  }

  const currentUserId = 1; // 로그인된 사용자의 ID를 여기에 할당

  try {
    const memberAddressResponse = await Api.post("/orders/delivery/create", {
      name: receiverName,
      address: address,
      memberId: currentUserId
    });

    const memberAddressId = memberAddressResponse.id;

    const orderResponse = await Api.post("/orders/create", {
      productId: productId,
      memberId: currentUserId,
      memberAddressId: memberAddressId
    });

    alert("주문이 성공적으로 처리되었습니다.");
    navigate("/order/complete")();

  } catch (err) {
    console.error("주문 처리 중 오류 발생", err);
    alert(`오류: ${err.message}`);
  }
}

async function fetchProductDetails(id) {
  try {
    const response = await Api.get(`/products/${id}`);
    console.log(response); // 서버 응답 로그로 기록

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

async function insertOrderSummary() {
  // selectedProductId 변수에 할당된 값을 사용하여 상품 정보를 가져옵니다.
  const product = await fetchProductDetails(selectedProductId);

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

document.addEventListener('DOMContentLoaded', () => {
  // buy-now-button를 클릭할 때 실행되는 이벤트 핸들러
  document.querySelector(`#buy-now-button`).addEventListener("click", async () => {
    // 선택한 상품의 ID를 얻어옵니다.
    selectedProductId = product.id;

    // insertOrderSummary 함수를 호출하여 주문 요약 정보를 삽입합니다.
    await insertOrderSummary();
  });
});
