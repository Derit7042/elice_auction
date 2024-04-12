document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registerProductForm');

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // 폼 기본 제출 행동 방지

        const formData = new FormData(); // 폼 데이터 생성

        // 폼 데이터에 필드 추가
        formData.append('title', document.getElementById('title').value);
        formData.append('brief', document.getElementById('brief').value);
        formData.append('price', parseInt(document.getElementById('price').value));

        const pictureInput = document.getElementById('pictureLink');
        if (pictureInput.files.length > 0) {
            formData.append('file', pictureInput.files[0]); // 이미지 파일 추가

            // 파일을 서버에 업로드하는 요청
            fetch('/api/products', {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        return response.json(); // 생성된 상품 정보 반환
                    }
                    throw new Error('Network response was not ok.');
                })
                .then(productData => {
                    console.log('Product added successfully:', productData);

                    // 생성된 상품의 이미지 URL을 받아옴
                    const pictureLink = productData.pictureLink;

                    // 받아온 이미지 URL을 사용하여 이미지를 표시
                    const productImage = document.createElement('img');
                    productImage.src = pictureLink;
                    productImage.alt = 'Product Image';
                    // 이미지를 표시할 요소에 이미지 추가
                    document.getElementById('productImageContainer').appendChild(productImage);

                    // 상품이 추가된 후에는 상품 목록 페이지로 이동
                    window.location.href = "/product-list/product-list.html";
                })
                .catch(error => {
                    console.error('Error adding product:', error.message);
                });
        }
    });

    // 이전에는 제출 버튼을 누르면 이동하는 로직을 가지고 있었는데
    // 버튼을 클릭할 때 페이지를 이동하도록 변경합니다.
    const submitButton = document.getElementById('submitButton');
    submitButton.addEventListener('click', function () {
        window.location.href = "/product-list/product-list.html";
    });
});