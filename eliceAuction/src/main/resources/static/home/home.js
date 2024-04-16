import * as API from "../api.js";
import { navigate, createNavbar } from "../useful-functions.js";
import {addToDb} from "../indexed-db.js";

// 요소(element), input 혹은 상수
const sliderDiv = document.querySelector("#slider");
const sliderArrowLeft = document.querySelector("#sliderArrowLeft");
const sliderArrowRight = document.querySelector("#sliderArrowRight");

addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
async function addAllElements() {
  await getProducts();
}

// 여러 개의 addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {}

// api에서 카테고리 정보 및 사진 가져와서 슬라이드 카드로 사용
async function getProducts() {
  // 상품 정보 가져옴
  const products = await API.get("/products");

  // 가져온 상품 정보를 출력함
  for (const product of products) {
    const {id, title, price, pictureLink, watchCount} = product;

    mainProductsContainer.insertAdjacentHTML(
        "beforeend",
        `
        <div class="col mb-5">
          <div class="card h-100">
            <!-- Product image-->
            <div id="product-${id}">
            <img class="card-img-top" 
            width="450px" height="300px" 
            src=${pictureLink} id="product-image-${id}" alt="..." 
            />
            <!-- Product details-->
            <div class="card-body p-4">
              <div class="text-center">
                <!-- Product name-->
                <h5 class="fw-bolder" id="product-title-${id}">${title}</h5>
                <!-- Product reviews-->
                <div class="d-flex justify-content-center small text-warning mb-2">
                </div>
                <!-- Product price-->
                <p id="product-price-${id}">${price} 원</p>
              </div>
            </div>
            </div>
            
            <!-- Product actions-->
            <div class="card-footer p-4 pt-0 border-top-0 bg-transparent" id="add-cart-${id}">
              <div class="text-center"><a class="btn btn-outline-dark mt-auto" href="#">Add to cart</a></div>
            </div>
          </div>
        </div>
        `
    );

    document
        .querySelector(`#add-cart-${id}`)
        .addEventListener("click", () => addCart(product));

    document
        .querySelector(`#product-${id}`)
        .addEventListener("click",  navigate(`/product/${id}`));

    document
        .querySelector(`#move-to-cart`)
        .addEventListener("click", navigate(`/cart`));

    document
        .querySelector(`#move-to-login`)
        .addEventListener("click", navigate(`/members/login`));
  //   TODO: 상품 클릭시 상세페이지로 이동기능 구현!


  }
}

function attachSlider() {
  // 페이지 로드 완료 후 bulmaCarousel 라이브러리의 attach 함수를 사용합니다.
  document.addEventListener('DOMContentLoaded', () => {
    const imageSlider = bulmaCarousel.attach("#slider", {
      autoplay: true,
      autoplaySpeed: 6000,
      infinite: true,
      duration: 500,
      pauseOnHover: false,
      navigation: false,
    });

    sliderArrowLeft.addEventListener("click", () => {
      imageSlider[0].previous();
    });

    sliderArrowRight.addEventListener("click", () => {
      imageSlider[0].next();
    });
  });
}

async function addCart(product){
  const {id, title, price, pictureLink } = product;
  let param = "?productId="+id;

  API.post("/cart/1"+param)
      .then(result => {
        console.log("회원 장바구니 추가");
        console.log(`상품 id: ${id}`);
      })
      .catch(async error => {
        console.log("비회원 장바구니 추가");
        console.log(`상품 id: ${id}`);
        await addToDb("cart", product, product.id);
      })

  alert("상품이 장바구니에 추가됬습니다.");
}