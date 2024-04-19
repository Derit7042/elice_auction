import * as Api from "../api.js";
import { blockIfLogin, validateEmail, createNavbar } from "../useful-functions.js";

// 요소(element), input 혹은 상수
const nameInput = document.querySelector("#nameInput");
const usernameInput = document.querySelector("#usernameInput");
const passwordInput = document.querySelector("#passwordInput");
const passwordConfirmInput = document.querySelector("#passwordConfirmInput");
const submitButton = document.querySelector("#submitButton");
blockIfLogin();
addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
async function addAllElements() {
  createNavbar();
}

// 여러 개의 addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  submitButton.addEventListener("click", handleSubmit);
}

// 회원가입 진행
async function handleSubmit(e) {
  e.preventDefault();

  const name = nameInput.value;
  const username = usernameInput.value;
  const password = passwordInput.value;
  const passwordConfirm = passwordConfirmInput.value;

  // 잘 입력했는지 확인
  const isNameValid = name.length >= 2;
  const isUsernameValid = username.length >= 2;
  const isPasswordValid = password.length >= 4;
  const isPasswordSame = password === passwordConfirm;

  let errorMessages = [];
  if (!isNameValid) errorMessages.push("이름은 2글자 이상이어야 합니다.");
  if (!isUsernameValid) errorMessages.push("아이디는 2글자 이상이어야 합니다.");
  if (!isPasswordValid) errorMessages.push("비밀번호는 4글자 이상이어야 합니다.");
  if (!isPasswordSame) errorMessages.push("비밀번호가 일치하지 않습니다.");

  if (errorMessages.length > 0) {
    alert(errorMessages.join("\n"));
    return;
  }

  // 회원가입 api 요청
  try {
    const data = { username, password, name };
    await Api.post("/members/register", data);
    alert("정상적으로 회원가입되었습니다.");
    window.location.href = "/login/login.html"; // 로그인 페이지 이동
  } catch (err) {
    console.error(err.stack);
    alert(`문제가 발생하였습니다. 확인 후 다시 시도해 주세요: ${err.message}`);
  }
}