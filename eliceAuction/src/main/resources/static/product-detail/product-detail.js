function getProductIdFromUrl() {
    let urlParams = window.location.pathname.split('/').pop();
    console.log(urlParams);
    return urlParams;
}

const productId = getProductIdFromUrl();

if (productId) {
    fetch(`/api/products/${productId}`)
        .then(response => response.json())
        .then(data => {
            const productImageHTML = `
                <img id="productImageTag" src="${data.pictureLink}" alt="Product Image">
            `;
            if(document.getElementById('image')) document.getElementById('image').innerHTML = productImageHTML;

            const productDetailHTML = `
                <p class="subtitle is-3 is-family-monospace" id="titleTag">${data.title}</p>
                <p class="detail-description" id="detailDescriptionTag">${data.brief}</p>
                <h1 id="priceTag">가격: ${data.price}원</h1>
                <p>조회수: ${data.watchCount}회</p>
            `;
            if(document.getElementById('content')) document.getElementById('content').innerHTML = productDetailHTML;
        })
        .catch(error => console.error('Error fetching product detail:', error));
}