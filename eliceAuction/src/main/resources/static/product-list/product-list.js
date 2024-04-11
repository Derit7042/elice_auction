fetch('/api/products') // 상세 정보를 가져오는 API 엔드포인트
    .then(response => response.json()) // JSON 데이터로 변환
    .then(products => {
      // 각 상품에 대한 HTML 생성
      const productDetailHTML = products.map(product => `
            <div class="product-item">
                <h2><a href="/product-detail/product-detail.html?id=${product.id}">${product.title}</a></h2>
                <p>${product.brief}</p>
                <p>가격: ${product.price}원</p>
                <p>조회수: ${product.watchCount}회</p>
                <img src="${product.pictureLink}" alt="Product Image">
            </div>
        `).join(''); // 배열을 문자열로 결합

      // 생성한 HTML을 페이지에 추가
      document.getElementById('producItemContainer').innerHTML = productDetailHTML;
    })
    .catch(error => console.error('Error fetching product detail:', error));
