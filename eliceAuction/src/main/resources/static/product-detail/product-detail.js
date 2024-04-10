// import { getImageUrl } from "../../aws-s3.js";
// import * as Api from "../../api.js";
// import {
//   getUrlParams,
//   addCommas,
//   checkUrlParams,
//   createNavbar,
// } from "../../useful-functions.js";
// import { addToDb, putToDb } from "../../indexed-db.js";
//
// // 요소(element), input 혹은 상수
// const productImageTag = document.querySelector("#productImageTag");
// const manufacturerTag = document.querySelector("#manufacturerTag");
// const titleTag = document.querySelector("#titleTag");
// const detailDescriptionTag = document.querySelector("#detailDescriptionTag");
// const addToCartButton = document.querySelector("#addToCartButton");
// const purchaseButton = document.querySelector("#purchaseButton");
//
// checkUrlParams("id");
// addAllElements();
// addAllEvents();
//
// // html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
// function addAllElements() {
//   createNavbar();
//   insertProductData();
// }
//
// // addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
// function addAllEvents() {}
//
// async function insertProductData() {
//   const { id } = getUrlParams();
//   const product = await Api.get(`/products/${id}`);
//
//   // 객체 destructuring
//   const {
//     title,
//     detailDescription,
//     menufacturer,
//     imageKey,
//     isRecommended,
//     price,
//   } = product;
//   const imageUrl = await getImageUrl(imageKey);
//
//   productImageTag.src = imageUrl;
//   titleTag.innerText = title;
//   detailDescriptionTag.innerText = detailDescription;
//   manufacturerTag.innerText = menufacturer;
//   priceTag.innerText = `${addCommas(price)}원`;
//
//   if (isRecommended) {
//     titleTag.insertAdjacentHTML(
//       "beforeend",
//       '<span class="tag is-success is-rounded">추천</span>'
//     );
//   }
//
//   addToCartButton.addEventListener("click", async () => {
//     try {
//       await insertDb(product);
//
//       alert("장바구니에 추가되었습니다.");
//     } catch (err) {
//       // Key already exists 에러면 아래와 같이 alert함
//       if (err.message.includes("Key")) {
//         alert("이미 장바구니에 추가되어 있습니다.");
//       }
//
//       console.log(err);
//     }
//   });
//
//   purchaseButton.addEventListener("click", async () => {
//     try {
//       await insertDb(product);
//
//       window.location.href = "/order";
//     } catch (err) {
//       console.log(err);
//
//       //insertDb가 에러가 되는 경우는 이미 제품이 장바구니에 있던 경우임
//       //따라서 다시 추가 안 하고 바로 order 페이지로 이동함
//       window.location.href = "/order";
//     }
//   });
// }
//
// async function insertDb(product) {
//   // 객체 destructuring
//   const { id: id, price } = product;
//
//   // 장바구니 추가 시, indexedDB에 제품 데이터 및
//   // 주문수량 (기본값 1)을 저장함.
//   await addToDb("cart", { ...product, quantity: 1 }, id);
//
//   // 장바구니 요약(=전체 총합)을 업데이트함.
//   await putToDb("order", "summary", (data) => {
//     // 기존 데이터를 가져옴
//     const count = data.productsCount;
//     const total = data.productsTotal;
//     const ids = data.ids;
//     const selectedIds = data.selectedIds;
//
//     // 기존 데이터가 있다면 1을 추가하고, 없다면 초기값 1을 줌
//     data.productsCount = count ? count + 1 : 1;
//
//     // 기존 데이터가 있다면 가격만큼 추가하고, 없다면 초기값으로 해당 가격을 줌
//     data.productsTotal = total ? total + price : price;
//
//     // 기존 데이터(배열)가 있다면 id만 추가하고, 없다면 배열 새로 만듦
//     data.ids = ids ? [...ids, id] : [id];
//
//     // 위와 마찬가지 방식
//     data.selectedIds = selectedIds ? [...selectedIds, id] : [id];
//   });
// }

// 페이지 로드 시 실행될 함수
// window.onload = function() {
//   // 제품 ID를 가져오는 방법은 원하는 방식으로 설정하셔야 합니다.
//   var productId = window.location.pathname.split('/').pop();
//
//   // Ajax 요청을 보냅니다.
//   $.ajax({
//     type: 'GET',
//     url: '/api/products/' + productId, // 해당 URL은 실제 서버의 엔드포인트에 맞게 수정해야 합니다.
//     success: function(response) {
//       // 성공적으로 응답을 받으면 데이터를 처리합니다.
//       // 여기서 response는 서버로부터 받은 JSON 형식의 데이터입니다.
//
//       // 데이터를 테이블에 채웁니다.
//       $('#productTable').append(
//           '<tr>' +
//           '<td>' + response.title + '</td>' +
//           '<td>' + response.brief + '</td>' +
//           '<td>' + response.price + '</td>' +
//           '<td>' + response.pictureLink + '</td>' +
//           '</tr>'
//       );
//
//       // 기타 필요한 작업을 수행합니다.
//     },
//     error: function(xhr, status, error) {
//       // 오류가 발생한 경우 처리합니다.
//       console.error('Error:', error);
//     }
//   });
// };

// 페이지 로드 시 실행될 함수
$(document).ready(function() {
    // 제품 ID를 가져옵니다.
    var productId = window.location.pathname.split('/').pop();

    // AJAX 요청을 보냅니다.
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/products/' + productId,
        dataType: 'json', // 응답 데이터 타입을 JSON으로 지정합니다.
        success: function(response) {
            // 받은 JSON 데이터를 콘솔에 출력합니다.
            console.log(response);

            // JSON 데이터를 HTML로 변환하여 화면에 표시합니다.
            var productDetail = $('#productDetail');
            productDetail.html(`
                <div>
                    <p>ID: ${response.id}</p>
                    <p>Title: ${response.title}</p>
                    <p>Brief: ${response.brief}</p>
                    <p>Price: ${response.price}</p>
                    <p>Watch Count: ${response.watchCount}</p>
                    <p>Date: ${response.date}</p>
                    <p>Picture Link: <img src="${response.pictureLink}" alt="Product Image"></p>
                </div>
            `);
        },
        error: function(xhr, status, error) {
            // 오류가 발생한 경우 콘솔에 오류를 출력합니다.
            console.error('Error:', error);
        }
    });
});