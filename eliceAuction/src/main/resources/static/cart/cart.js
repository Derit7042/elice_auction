// import { getImageUrl } from "../aws-s3.js";
import {
  addCommas,
  convertToNumber,
  navigate,
  compressString,
  createNavbar,
} from "../useful-functions.js";
import {addToDb, deleteFromDb, getFromDb, putToDb} from "../indexed-db.js";

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

let cart_baseUrl = "/cart";

addAllElements();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  insertProductsfromCart();

}

// indexedDB의 cart와 order에서 필요한 정보를 가져온 후
// 요소(컴포넌트)를 만들어 html에 삽입함.
async function insertProductsfromCart() {
  let products;

  // 로그인 상태 확인
  await API.get(cart_baseUrl)
      .then(async result => {
        products = result;
      })
      .catch(async error => {

        if (error.status === 404) {// 잘못된 접근
          alert("잘못된 접근입니다.");
        } else{
          const token = sessionStorage.getItem("token");
          if (!token) {
            products = await getFromDb("cart");
            console.log("로그인 실패. 쿠키에서 장바구니를 가져옵니다.");
          }
        }
        console.error(`HTTP 상태 코드: ${error.status}, 에러 메시지: ${error.reason}`);

      })

  totalCount = 0;
  totalPrice = 0;
  let purchaseId;

  for (const product of products) {
    totalCount+=1;
    // 객체 destructuring
    const {id, title, price, pictureLink } = product;
    console.log("product ID: " + id)
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
               <img src=${pictureLink} id="image-${id}" class="img-fluid
              rounded-3" alt="Shopping item" style="width: 65px;">
            </div>
            
            <div class="ms-3">
              
              <p id="title-${id}">${title}</p>
              
<!--              <p class="small mb-0">27세, INTJ</p>-->
              
            </div>
            
          </div>
          
          <div class="d-flex flex-row align-items-center">
            
            <div style="width: 50px">
              
              <h5 class="fw-normal mb-0">1</h5>
              
            </div>
            
            <div style="width: 80px">
              
              <h5 class="mb-0" id="unitPrice-${id}">${price} 원</h5>
              
            </div>
            
            <a href="#!" style="color: #cecece" id="delete-${id}"><i class="fas fa-trash-alt"></i></a
            >
          </div>
          
        </div>
        
      </div>
      
    </div>
    `
    );

    // 각종 이벤트 추가
    document
        .querySelector(`#delete-${id}`)
        .addEventListener("click", () => deleteItem(id));
    document
        .querySelector(`#image-${id}`)
        .addEventListener("click", navigate(`/product/${id}`));

    document
        .querySelector(`#title-${id}`)
        .addEventListener("click", navigate(`/product/${id}`));

    purchaseId = id;
  }

  document
      .querySelector("#purchaseButton")
      .addEventListener("click", () => perchase(purchaseId));

  insertOrderSummary();

}
async function perchase(id) {
  console.log(`purchase product id: ${id}`);//주문할 상품 id
  if(totalCount!==1){
    alert("상품 주문은 한번에 한개씩만 주문할 수 있습니다.");
  } else{
    console.log("xx");
    window.location.href = `/order?id=${id}`
  }
}

async function deleteItem(id) {
  console.log(`delete product id: ${id}`);// 삭제할 상품 id

  let url = cart_baseUrl+`/${id}`;
  console.log(`API request: ${url}`);
  await API.delete(url)
      .then(result => {
        console.log("회원 장바구니 삭제");
        console.log(`삭제 id: ${id}`);
      })
      .catch(async error => {
        console.log("비회원 장바구니 삭제");
        console.log(`삭제 id: ${id}`);
        await deleteFromDb("cart", id);
      })
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


async function addCart(product){
  const {id, title, price, pictureLink } = product;
  let url = cart_baseUrl+`/${id}`;

  API.post(url)
      .then(result => {
        console.log("회원 장바구니 추가");
        console.log(`상품 id: ${id}`);
      })
      .catch(async error => {
        console.log("비회원 장바구니 추가");
        console.log(`상품 id: ${id}`);
        await addToDb("cart", product, id);
      })
}

export { addCart };