// fetch('/api/products') // 상세 정보를 가져오는 API 엔드포인트
//     .then(response => response.json()) // JSON 데이터로 변환
//     .then(products => {
//       // 각 상품에 대한 HTML 생성
//       const productDetailHTML = products.map(product => `
//             <div class="product-item">
//                 <h2><a href="http://localhost:8080/product/product-detail?id=${product.id}">${product.title}</a></h2>
//                 <p>${product.brief}</p>
//                 <p>가격: ${product.price}원</p>
//                 <p>조회수: ${product.watchCount}회</p>
//                 <img src="${product.pictureLink}" alt="Product Image">
//             </div>
//         `).join(''); // 배열을 문자열로 결합
//
//       // 생성한 HTML을 페이지에 추가
//       document.getElementById('producItemContainer').innerHTML = productDetailHTML;
//     })
//     .catch(error => console.error('Error fetching product detail:', error));

// 제품 목록을 가져오고 화면에 렌더링하는 함수
// 제품 목록을 가져오고 화면에 렌더링하는 함수
// 페이지가 로드될 때 실행되는 함수
window.addEventListener('DOMContentLoaded', function () {
    // 제품 목록을 가져와서 화면에 렌더링하는 함수 호출
    fetchAndRenderProductList();
});

// 제품 목록을 가져오고 화면에 렌더링하는 함수
function fetchAndRenderProductList() {
    // 서버로부터 제품 목록을 가져오는 fetch 요청
    fetch('/api/products')
        .then(response => {
            // 응답을 JSON 형식으로 변환
            return response.json();
        })
        .then(products => {
            // 제품 목록을 렌더링하는 함수 호출
            renderProductList(products);
        })
        .catch(error => {
            // 오류 처리
            console.error('Error fetching product list:', error);
        });
}

// 제품 목록을 화면에 렌더링하는 함수
function renderProductList(products) {
    // 제품 목록 컨테이너 요소
    const productListContainer = document.getElementById('producItemContainer');

    // 기존의 제품 목록을 비움
    productListContainer.innerHTML = '';

    // 제품 목록을 순회하면서 각 제품을 화면에 추가
    products.forEach(product => {
        // 각 제품의 HTML 요소 생성
        const productDetailHTML = `
            <div class="product-item">
                <h2><a href="http://localhost:8080/product/product-detail?id=${product.id}">${product.title}</a></h2>
                <p>${product.brief}</p>
                <p>가격: ${product.price}원</p>
                <p>조회수: ${product.watchCount}회</p>
                <img src="${product.pictureLink}" alt="Product Image">
            </div>
        `;

        // 제품 목록 컨테이너에 제품 HTML 추가
        productListContainer.insertAdjacentHTML('beforeend', productDetailHTML);
    });
}
