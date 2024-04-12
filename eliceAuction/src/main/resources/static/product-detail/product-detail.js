function getProductIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
}

// 현재 페이지의 URL에서 id 매개변수 값을 가져옴
const productId = getProductIdFromUrl();

// 페이지 로드 시 실행될 함수
fetch(`/api/products/${productId}`) // 상세 정보를 가져오는 API 엔드포인트
    .then(response => response.json()) // JSON 데이터로 변환
    .then(data => {
        // JSON 데이터를 사용하여 HTML 요소 생성
        const productImageHTML = `
      <img id="productImageTag" src="${data.pictureLink}" alt="Product Image">
    `;
        // 생성한 HTML을 페이지에 추가
        document.getElementById('image').innerHTML = productImageHTML;

        const productDetailHTML = `
      <p class="subtitle is-3 is-family-monospace" id="titleTag">${data.title}</p>
            <p class="detail-description" id="detailDescriptionTag">${data.brief}</p>
            <h1 id="priceTag">가격: ${data.price}원</h1>
            <p>조회수: ${data.watchCount}회</p>
    `;
        // 생성한 HTML을 페이지에 추가
        document.getElementById('content').innerHTML = productDetailHTML;
    })
    .catch(error => console.error('Error fetching product detail:', error));