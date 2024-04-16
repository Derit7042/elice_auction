import * as API from "../api.js";
import { navigate, createNavbar } from "../useful-functions.js";
import {addToDb} from "../indexed-db.js";


function getProductIdFromUrl() {
    let urlParams = window.location.pathname.split('/').pop();
    console.log(urlParams);
    return urlParams;
}

const productId = getProductIdFromUrl();


addAllElementsAndEvents();

async function addAllElementsAndEvents() {
    await getProduct();
}

async function getProduct() {
    const product = await API.get(`/products/${productId}`)
        .catch(error => {
            alert("해당 상품이 존재하지 않습니다.");
            console.error('error status code: ', error.status);
        });
    const {id, title, brief, price, pictureLink, watchCount} = product;

    productDetailContainer.insertAdjacentHTML(
        "beforeend",
        `
        <div class="row gx-4 gx-lg-5 align-items-center">
                    <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" width="600px" height="700px"src="${pictureLink}" alt="..."/></div>
                    <div class="col-md-6">
                        <div class="small mb-1">Product ID: ${id}</div>
                        <h1 class="display-5 fw-bolder">${title}</h1>
                        <div class="fs-5 mb-5">
                            <span>${price} 원</span>
                            <i class="bi-person me-1" style="margin-left: 10%;">${watchCount}</i>
                        </div>
                        <p class="lead">${brief}</p>
                        <div class="d-flex">
                            <button class="btn btn-outline-dark flex-shrink-0" type="button" id="add-cart-button">
                                <i class="bi-cart-fill me-1"></i>
                                Add to cart
                            </button>

                            <button class="btn btn-outline-dark flex-shrink-0" type="button" style="margin-left: 5%;" id="buy-now-button">
                                <i class="bi-credit-card-fill me-1"></i>
                                Buy Now
                            </button>
                        </div>
                    </div>
                </div>
        `
    );

    document
        .querySelector(`#add-cart-button`)
        .addEventListener("click", () => addCart(product));

    document
        .querySelector(`#buy-now-button`)
        .addEventListener("click", navigate(`/order`));


    document
        .querySelector(`#move-to-cart`)
        .addEventListener("click", navigate(`/cart`));

    document
        .querySelector(`#move-to-login`)
        .addEventListener("click", navigate(`/login`));
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



// if (productId) {
//     fetch(`/api/products/${productId}`)
//         .then(response => response.json())
//         .then(data => {
//             const productImageHTML = `
//                 <img id="productImageTag" src="${data.pictureLink}" alt="Product Image">
//             `;
//             if(document.getElementById('image')) document.getElementById('image').innerHTML = productImageHTML;
//
//             const productDetailHTML = `
//                 <p class="subtitle is-3 is-family-monospace" id="titleTag">${data.title}</p>
//                 <p class="detail-description" id="detailDescriptionTag">${data.brief}</p>
//                 <h1 id="priceTag">가격: ${data.price}원</h1>
//                 <p>조회수: ${data.watchCount}회</p>
//             `;
//             if(document.getElementById('content')) document.getElementById('content').innerHTML = productDetailHTML;
//         })
//         .catch(error => console.error('Error fetching product detail:', error));
// }