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

document.addEventListener("DOMContentLoaded", () => {
  setupLogout();
 toggleLoginLogoutButtons();
  enableDropdowns();
});


////////////로그아웃
function setupLogout() {
  const logoutButton = document.querySelector("#move-to-logout");
  logoutButton.addEventListener("click", () => {
    if (confirm("로그아웃 하시겠습니까?")) { // 사용자에게 로그아웃 의사를 확인
      sessionStorage.removeItem("token");
    }
  });
}

////////////로그인 상태에서 로그인 버튼 숨김
function toggleLoginLogoutButtons() {
  const loginButton = document.querySelector("#move-to-login");
  const logoutButton = document.querySelector("#move-to-logout");
  const signUpButton = document.querySelector("#move-to-signup");

  // sessionStorage에서 'token'을 체크하여 로그인 상태 확인
  const isLoggedIn = sessionStorage.getItem("token");

  // 로그인 상태에 따라 로그인 버튼과 로그아웃 버튼을 숨기거나 보여줌
  if (isLoggedIn) {
    loginButton.style.display = 'none'; // 로그인 상태면 로그인 버튼 숨김
    signUpButton.style.display = 'none'; // 로그인 상태면 회원가입 버튼도 숨김
    logoutButton.style.display = 'block'; // 로그인 상태면 로그아웃 버튼 보여줌
  } else {
    loginButton.style.display = 'block'; // 로그아웃 상태면 로그인 버튼 보여줌
    signUpButton.style.display = 'block'; // 로그아웃 상태면 회원가입 버튼 보여줌
    logoutButton.style.display = 'none'; // 로그아웃 상태면 로그아웃 버튼 숨김
  }
}


////////////account 버튼 클릭시 드롭다운 메뉴
function enableDropdowns() {
  const accountDropdown = document.querySelector('#move-to-account');
  const dropdownMenu = accountDropdown.nextElementSibling;

  accountDropdown.addEventListener('click', function(event) {
    event.stopPropagation();
    dropdownMenu.classList.toggle('show');
  });

  // 다른 영역을 클릭할 때 드롭다운 메뉴 숨기기
  document.body.addEventListener('click', function(event) {
    if (!accountDropdown.contains(event.target)) {
      dropdownMenu.classList.remove('show');
    }
  });

}




document.addEventListener("DOMContentLoaded", setupLogout);

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

    document
        .querySelector('#move-to-logout')
        .addEventListener("click", navigate('/'));

    document
        .querySelector('#manage-account')
        .addEventListener("click", navigate('/members/account'));

    document
        .querySelector('#view-orders')
        .addEventListener("click", navigate('/members/account/orders'));

    document
        .querySelector('#manage-profile')
        .addEventListener("click", navigate('/members/account/security'));

        document
                .querySelector('#move-to-signup')
                .addEventListener("click", navigate('/members/register'));

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


  API.post(`/cart/${id}`)
      .then(result => {
        console.log("회원 장바구니 추가");
        console.log(`상품 id: ${id}`);
      })
      .catch(async error => {
        console.log("비회원 장바구니 추가");
        console.log(`상품 id: ${id}`);
        await addToDb("cart", product, product.id);
      })

  alert("상품이 장바구니에 추가됐습니다.");
}