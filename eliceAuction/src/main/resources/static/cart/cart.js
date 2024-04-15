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
const productsCountElem = document.querySelector("#productsCount");
const productsTotalElem = document.querySelector("#productsTotal");
const deliveryFeeElem = document.querySelector("#deliveryFee");
const orderTotalElem = document.querySelector("#orderTotal");
const purchaseButton = document.querySelector("#purchaseButton");


let totalCount = 0;// 장바구니에 담긴 상품 수
let totalPrice = 0;// 장바구니에 담긴 상품 금액

let userId = 1;
addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  createNavbar();
  insertProductsfromCart();

}

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  purchaseButton.addEventListener("click", navigate("/order"));
}

// indexedDB의 cart와 order에서 필요한 정보를 가져온 후
// 요소(컴포넌트)를 만들어 html에 삽입함.
async function insertProductsfromCart() {
  let products;

  // 로그인 상태 확인
  await API.get("/cart/1")
      .then(result => {
        products = result;
      })
      .catch(error => {

        if(error.status === 404){
          alert("잘못된 접근입니다.");
        } else if(error.status === 403){
          console.log("로그인 실패. 쿠키에서 장바구니를 가져옵니다.");
          alert("무엄하도다!!");
          alert(`으~딜 비회원따리가 감히 고귀하고 위대하신 우리 "CART" 에 접근하려 하나!!`);
          alert("비회원 장바구니 기능은 아직 구현이 안되있습니다. 첫 충전 3,300원을 하시면 득.별.히 구현해드리겠습니다^^");
          alert("100% 안심계좌: 국민 626402-01-725xxx 임xx");
        }

        console.error(`HTTP 상태 코드: ${error.status}, 에러 메시지: ${error.reason}`);
        
      })



  totalCount = 0;
  totalPrice = 0;
  // userId = window.location.pathname.replace("/", '');
  // console.log(userId)


  for (const product of products) {
    totalCount+=1;
    // 객체 destructuring
    const {productId, title, price, pictureLink } = product;
    console.log("product ID: " + productId)
    console.log("title: " + title);
    console.log("price: " + price);
    console.log("pictureLink: " + pictureLink);
    totalPrice += price;

    cartProductsContainer.insertAdjacentHTML(
        "beforeend",

`
    <div class="card mb-3">
      
      <div class="card-body">
        
        <div class="d-flex justify-content-between">
          
          <div class="d-flex flex-row align-items-center">
            
            <div>
               <img src=${pictureLink} id="image-${productId}" class="img-fluid
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
    document
      .querySelector(`#image-${productId}`)
      .addEventListener("click", navigate(`/products/${productId}`));

    document
      .querySelector(`#title-${productId}`)
      .addEventListener("click", navigate(`/products/${productId}`));

  }

  insertOrderSummary();

}

async function deleteItem(id) {
  // indexedDB의 cart 목록에서 id를 key로 가지는 데이터를 삭제함.
  console.log(id);
  let url = "http://localhost:8080/api/cart"
  let param = "1?productId="+id;
  console.log(url);
  await API.delete(url, param);
  // 페이지를 새로고침함
  window.location.reload();
}

// 페이지 로드 시 실행되며, 결제정보 카드에 값을 삽입함.
async function insertOrderSummary() {

  productsCountElem.innerText = `${totalCount}개`;
  productsTotalElem.innerText = `${addCommas(totalPrice)}원`;

  if (totalCount > 0) {
    deliveryFeeElem.innerText = `3,000원`;
    orderTotalElem.innerText = `${addCommas(totalPrice + 3000)}원`;
  } else {
    deliveryFeeElem.innerText = `0원`;
    orderTotalElem.innerText = `0원`;
  }
}
