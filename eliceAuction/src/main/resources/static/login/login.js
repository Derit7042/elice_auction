import * as API from "../api.js";
import { blockIfLogin, getUrlParams, createNavbar, navigate } from "../useful-functions.js";

const usernameInput = document.querySelector("#usernameInput");
const passwordInput = document.querySelector("#passwordInput");
const submitButton = document.querySelector("#submitButton");

blockIfLogin();
addAllElements();
addAllEvents();

function addAllElements() {
    createNavbar();
}

function addAllEvents() {
    submitButton.addEventListener("click", handleSubmit);
}

async function handleSubmit(e) {
    e.preventDefault();

    const username = usernameInput.value;
    const password = passwordInput.value;

    if (username.length < 2 || password.length < 4) {
        alert("아이디와 비밀번호를 정확히 입력해주세요.");
        return;
    }

    const data = { username, password };

    await API.post("/members/login", { username, password }, true)
        .then(result=>{
            if (result) {
                const storeToken = cleanToken(result.split(" ")[1]); // Bearer 토큰에서 실제 토큰 부분만 저장 후 필요없는 문자 제거
                sessionStorage.setItem("token", storeToken);
                alert("로그인 성공!");
                navigate("/");
            } else {
                console.error("서버 응답:", result);
                alert("로그인 실패: 유효한 토큰을 받지 못했습니다. 서버 응답을 확인해야 할 수 있습니다.");
            }
        })
        .catch(error => {
            if (error instanceof TypeError) {
                console.error("네트워크 오류 발생:", error);
                alert("서버에 접속할 수 없습니다. 네트워크 연결을 확인해 주세요.");
            } else {
                console.error("알 수 없는 오류 발생:", error);
                alert("알 수 없는 오류가 발생했습니다. 관리자에게 문의하세요.");
            }
        });



    // try {
    //     const data = { username, password };
    //     const response = await fetch("http://localhost:8080/api/members/login", {
    //         method: "POST",
    //         headers: {
    //             "Content-Type": "application/json"
    //         },
    //         body: JSON.stringify(data)
    //     });

    //     if (response.ok) {
    //         const result = await response.json();
    //         const token = response.headers.get("Authorization");
    //         if (token) {
    //             const cleanedToken = cleanToken(token.split(" ")[1]); // Bearer 토큰에서 실제 토큰 부분만 저장 후 필요없는 문자 제거
    //             sessionStorage.setItem("token", cleanedToken);
    //             alert("로그인 성공!");
    //             const { previouspage } = getUrlParams();
    //             window.location.href = previouspage || "/home/home.html";
    //         } else {
    //             console.error("서버 응답:", result);
    //             alert("로그인 실패: 유효한 토큰을 받지 못했습니다. 서버 응답을 확인해주세요.");
    //         }
    //     } else {
    //         const errorResult = await response.json();
    //         console.error("로그인 실패:", errorResult);
    //         alert(`로그인 실패: ${errorResult.message}`);
    //     }
    // } catch (error) {
    //     if (error instanceof TypeError) {
    //         console.error("네트워크 오류 발생:", error);
    //         alert("서버에 접속할 수 없습니다. 네트워크 연결을 확인해 주세요.");
    //     } else {
    //         console.error("알 수 없는 오류 발생:", error);
    //         alert("알 수 없는 오류가 발생했습니다. 관리자에게 문의하세요.");
    //     }
    // }
}

// 클라이언트 측에서 토큰을 정리하는 함수
function cleanToken(token) {
    // 정규 표현식을 사용하여 JWT 형식 확인 및 필요없는 문자 제거
    const cleaned = token.replace(/[^A-Za-z0-9\-_\.]/g, '');
    return cleaned;
}

