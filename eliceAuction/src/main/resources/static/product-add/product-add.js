let isPictureValid = true; // 사진 유효성을 나타내는 변수

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
        const pictureFile = pictureInput.files[0];
        if (pictureFile.size > 10 * 1024 * 1024) { // 10MB 제한
            alert('사진은 최대 10MB 크기까지 가능합니다.');
            isPictureValid = false; // 사진 유효성을 false로 설정
            return; // 업로드 중단
        }

        formData.append('file', pictureFile); // 이미지 파일 추가
        isPictureValid = true; // 사진 유효성을 true로 설정

        // 파일을 서버에 업로드하는 요청
        fetch('http://34.64.166.147:8080/api', {
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
                window.location.href = "/";
            })
            .catch(error => {
                console.error('Error adding product:', error.message);
            });
    }
});

// 파일 입력란의 변경을 감지하여 사진 유효성을 재설정하는 이벤트 리스너
pictureInput.addEventListener('change', function() {
    if (isPictureValid === false) {
        const pictureFile = pictureInput.files[0];
        if (pictureFile.size <= 10 * 1024 * 1024) {
            isPictureValid = true; // 사진 유효성을 true로 설정
        }
    }
});