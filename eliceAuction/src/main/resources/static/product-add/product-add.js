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

                // 제품 목록 페이지로 이동
                window.location.href = "http://localhost:8080/product";
            })
            .catch(error => {
                console.error('Error adding product:', error.message);
            });
    }
});