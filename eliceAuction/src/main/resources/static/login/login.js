import * as Api from "../api.js";
import { blockIfLogin, getUrlParams, createNavbar } from "../useful-functions.js";

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

    try {
        const data = { username, password };
        const response = await fetch("http://localhost:8080/api/members/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const result = await response.json();
            const token = response.headers.get("Authorization");
            if (token) {
                sessionStorage.setItem("token", token.split(" ")[1]); // Bearer 토큰에서 실제 토큰 부분만 저장
                alert("로그인 성공!");
                const { previouspage } = getUrlParams();
                window.location.href = previouspage || "/home/home.html";
            } else {
                console.error("서버 응답:", result);
                alert("로그인 실패: 유효한 토큰을 받지 못했습니다. 서버 응답을 확인해주세요.");
            }
        } else {
            const errorResult = await response.json();
            console.error("로그인 실패:", errorResult);
            alert(`로그인 실패: ${errorResult.message}`);
        }
    } catch (error) {
        if (error instanceof TypeError) {
            console.error("네트워크 오류 발생:", error);
            alert("서버에 접속할 수 없습니다. 네트워크 연결을 확인해 주세요.");
        } else {
            console.error("알 수 없는 오류 발생:", error);
            alert("알 수 없는 오류가 발생했습니다. 관리자에게 문의하세요.");
        }
    }
}
