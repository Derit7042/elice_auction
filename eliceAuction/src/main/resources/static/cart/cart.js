import { getImageUrl } from "../aws-s3.js";
import {
  addCommas,
  convertToNumber,
  navigate,
  compressString,
  createNavbar,
} from "../useful-functions.js";
import { deleteFromDb, getFromDb, putToDb } from "../indexed-db.js";

import * as Api from "../api.js";
import * as API from "../api.js";

// 요소(element), input 혹은 상수
const cartProductsContainer = document.querySelector("#cartProductsContainer");
const allSelectCheckbox = document.querySelector("#allSelectCheckbox");
const partialDeleteLabel = document.querySelector("#partialDeleteLabel");
const productsCountElem = document.querySelector("#productsCount");
const productsTotalElem = document.querySelector("#productsTotal");
const deliveryFeeElem = document.querySelector("#deliveryFee");
const orderTotalElem = document.querySelector("#orderTotal");
const purchaseButton = document.querySelector("#purchaseButton");

addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  createNavbar();
  insertProductsfromCart();
  insertOrderSummary();
  updateAllSelectCheckbox();
}

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  allSelectCheckbox.addEventListener("change", toggleAll);
  partialDeleteLabel.addEventListener("click", deleteSelectedItems);
  purchaseButton.addEventListener("click", navigate("/order"));
}

// indexedDB의 cart와 order에서 필요한 정보를 가져온 후
// 요소(컴포넌트)를 만들어 html에 삽입함.
async function insertProductsfromCart() {

  const products = await API.get("http://localhost:8080/api/cart/1");

  for (const product of products) {
    // 객체 destructuring
    const {productId, title, price, pictureLink } = product;
    console.log("product ID: " + productId)
    console.log("title: " + title);
    console.log("price: " + price);
    console.log("pictureLink: " + pictureLink);

    cartProductsContainer.insertAdjacentHTML(
        "beforeend",

`
    <div class="card mb-3">
      
      <div class="card-body">
        
        <div class="d-flex justify-content-between">
          
          <div class="d-flex flex-row align-items-center">
            
            <div>
               <img src=${product.pictureLink} class="img-fluid
              rounded-3" alt="Shopping item" style="width: 65px;">
            </div>
            
            <div class="ms-3">
              
              <p id="title-${productId}">${title}</p>
              
              <p class="small mb-0">27세, INTJ</p>
              
            </div>
            
          </div>
          
          <div class="d-flex flex-row align-items-center">
            
            <div style="width: 50px">
              
              <h5 class="fw-normal mb-0">1</h5>
              
            </div>
            
            <div style="width: 80px">
              
              <h5 class="mb-0" id="unitPrice-${productId}">${price} 원</h5>
              
            </div>
            
            <a href="#!" style="color: #cecece" id="delete-${productId}"><i class="fas fa-trash-alt"></i></a
            >
          </div>
          
        </div>
        
      </div>
      
    </div>
    `
    );

    // 각종 이벤트 추가
    document
      .querySelector(`#delete-${productId}`)
      .addEventListener("click", () => deleteItem(productId));
    // document
    //   .querySelector(`#image-${productId}`)
    //   .addEventListener("click", navigate(`/product/detail?id=${productId}`));
    //
    // document
    //   .querySelector(`#title-${productId}`)
    //   .addEventListener("click", navigate(`/product/detail?id=${productId}`));
    //
  }
}

// TODO: 상품 삭제 기능 연결
async function deleteItem(id) {
  // indexedDB의 cart 목록에서 id를 key로 가지는 데이터를 삭제함.
  console.log(id);
  let url = "http://localhost:8080/api/cart"
  let param = "1?productId="+id;
  console.log(url);
  const products = await API.delete(url, param);
  // 페이지를 새로고침함
  window.location.reload();

  // 결제정보를 업데이트함.
  await updateOrderSummary(id, "removePermanent-deleteButton");


}

// 결제정보 카드 업데이트 및, indexedDB 업데이트를 진행함.
async function updateOrderSummary(id, type) {
  // 업데이트 방식 결정을 위한 변수들
  const isCheckbox = type.includes("checkbox");
  const isInput = type.includes("input");
  const isDeleteButton = type.includes("deleteButton");
  const isMinusButton = type.includes("minusButton");
  const isPlusButton = type.includes("plusButton");
  const isAdd = type.includes("add");
  const isRemoveTemp = type.includes("removeTemp");
  const isRemovePermanent = type.includes("removePermanent");
  const isRemove = isRemoveTemp || isRemovePermanent;
  const isItemChecked = document.querySelector(`#checkbox-${id}`).checked;
  const isDeleteWithoutChecked = isDeleteButton && !isItemChecked;

  // 업데이트에 사용될 변수
  let price;
  let quantity;

  // 체크박스 혹은 삭제 버튼 클릭으로 인한 업데이트임.
  if (isCheckbox || isDeleteButton) {
    const priceElem = document.querySelector(`#total-${id}`);
    price = convertToNumber(priceElem.innerText);

    quantity = 1;
  }

  // - + 버튼 클릭으로 인한 업데이트임.
  if (isMinusButton || isPlusButton) {
    const unitPriceElem = document.querySelector(`#unitPrice-${id}`);
    price = convertToNumber(unitPriceElem.innerText);

    quantity = 0;
  }

  // input 박스 입력으로 인한 업데이트임
  if (isInput) {
    const unitPriceElem = document.querySelector(`#unitPrice-${id}`);
    const unitPrice = convertToNumber(unitPriceElem.innerText);

    const inputElem = document.querySelector(`#quantityInput-${id}`);
    const inputQuantity = convertToNumber(inputElem.value);

    const quantityElem = document.querySelector(`#quantity-${id}`);
    const currentQuantity = convertToNumber(quantityElem.innerText);

    price = unitPrice * (inputQuantity - currentQuantity);

    quantity = 0;
  }

  // 업데이트 방식
  const priceUpdate = isAdd ? +price : -price;
  const countUpdate = isAdd ? +quantity : -quantity;

  // 현재 결제정보의 값들을 가져오고 숫자로 바꿈.
  const currentCount = convertToNumber(productsCountElem.innerText);
  const currentProductsTotal = convertToNumber(productsTotalElem.innerText);
  const currentFee = convertToNumber(deliveryFeeElem.innerText);
  const currentOrderTotal = convertToNumber(orderTotalElem.innerText);

  // 결제정보 관련 요소들 업데이트
  if (!isDeleteWithoutChecked) {
    productsCountElem.innerText = `${currentCount + countUpdate}개`;
    productsTotalElem.innerText = `${addCommas(
      currentProductsTotal + priceUpdate
    )}원`;
  }

  // 기존 결제정보가 비어있었어서, 배송비 또한 0인 상태였던 경우
  const isFeeAddRequired = isAdd && currentFee === 0;

  if (isFeeAddRequired) {
    deliveryFeeElem.innerText = `3000원`;
    orderTotalElem.innerText = `${addCommas(
      currentOrderTotal + priceUpdate + 3000
    )}원`;
  }

  if (!isFeeAddRequired && !isDeleteWithoutChecked) {
    orderTotalElem.innerText = `${addCommas(
      currentOrderTotal + priceUpdate
    )}원`;
  }

  // 이 업데이트로 인해 결제정보가 비게 되는 경우
  const isCartNowEmpty = currentCount === 1 && isRemove;

  if (!isDeleteWithoutChecked && isCartNowEmpty) {
    deliveryFeeElem.innerText = `0원`;

    // 다시 한 번, 현재 값을 가져와서 3000을 빼 줌
    const currentOrderTotal = convertToNumber(orderTotalElem.innerText);
    orderTotalElem.innerText = `${addCommas(currentOrderTotal - 3000)}원`;

    // 전체선택도 언체크되도록 함.
    updateAllSelectCheckbox();
  }

  // indexedDB의 order.summary 업데이트
  await putToDb("order", "summary", (data) => {
    const hasId = data.selectedIds.includes(id);

    if (isAdd && !hasId) {
      data.selectedIds.push(id);
    }

    if (isRemoveTemp) {
      data.selectedIds = data.selectedIds.filter((productId) => productId !== id);
    }

    if (isRemovePermanent) {
      data.ids = data.ids.filter((productId) => productId !== id);
      data.selectedIds = data.selectedIds.filter((productId) => productId !== id);
    }

    if (!isDeleteWithoutChecked) {
      data.productsCount += countUpdate;
      data.productsTotal += priceUpdate;
    }
  });

}

// 아이템(제품)카드의 수량, 금액 등을 업데이트함
async function updateProductItem(id, type) {
  // 업데이트 방식을 결정하는 변수들
  const isInput = type.includes("input");
  const isIncrease = type.includes("increase");

  // 업데이트에 필요한 요소 및 값들을 가져오고 숫자로 바꿈.
  const unitPriceElem = document.querySelector(`#unitPrice-${id}`);
  const unitPrice = convertToNumber(unitPriceElem.innerText);

  const quantityElem = document.querySelector(`#quantity-${id}`);
  const currentQuantity = convertToNumber(quantityElem.innerText);

  const totalElem = document.querySelector(`#total-${id}`);
  const currentTotal = convertToNumber(totalElem.innerText);

  const inputElem = document.querySelector(`#quantityInput-${id}`);
  const inputQuantity = convertToNumber(inputElem.value);

  // 업데이트 진행
  if (isInput) {
    quantityElem.innerText = `${inputQuantity}개`;
    totalElem.innerText = `${addCommas(unitPrice * inputQuantity)}원`;
    return;
  }

  const quantityUpdate = isIncrease ? +1 : -1;
  const priceUpdate = isIncrease ? +unitPrice : -unitPrice;

  quantityElem.innerText = `${currentQuantity + quantityUpdate}개`;
  totalElem.innerText = `${addCommas(currentTotal + priceUpdate)}원`;
}

// 페이지 로드 시 실행되며, 결제정보 카드에 값을 삽입함.
async function insertOrderSummary() {
  const { productsCount, productsTotal } = await getFromDb("order", "summary");

  const hasItems = productsCount !== 0;

  productsCountElem.innerText = `${productsCount}개`;
  productsTotalElem.innerText = `${addCommas(productsTotal)}원`;

  if (hasItems) {
    deliveryFeeElem.innerText = `3,000원`;
    orderTotalElem.innerText = `${addCommas(productsTotal + 3000)}원`;
  } else {
    deliveryFeeElem.innerText = `0원`;
    orderTotalElem.innerText = `0원`;
  }
}
