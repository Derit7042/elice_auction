import { createNavbar } from "../../useful-functions.js";
import * as Api from "../../api.js";

// 요소(element), input 혹은 상수
const ordersContainer = document.querySelector("#ordersContainer");
const modal = document.querySelector("#modal");
const modalBackground = document.querySelector("#modalBackground");
const modalCloseButton = document.querySelector("#modalCloseButton");
const deleteCompleteButton = document.querySelector("#deleteCompleteButton");
const deleteCancelButton = document.querySelector("#deleteCancelButton");

addAllElements();
addAllEvents();

function addAllElements() {
  createNavbar();
  insertOrders();
}

function addAllEvents() {
  modalBackground.addEventListener("click", closeModal);
  modalCloseButton.addEventListener("click", closeModal);
  document.addEventListener("keydown", keyDownCloseModal);
  deleteCompleteButton.addEventListener("click", deleteOrderData);
  deleteCancelButton.addEventListener("click", cancelDelete);
}

let orderIdToDelete;
async function insertOrders() {
  const memberId = 1;
  try {
    const response = await Api.get(`/orders/member/${memberId}`);
    console.log(response);

    if (!Array.isArray(response)) {
      throw new Error('서버 응답 데이터가 아닙니다.');
    }

    // 기존 주문 항목들을 제거
    ordersContainer.innerHTML = "";

    response.forEach(order => {
      const { id, date, product } = order;
      const formattedDate = date ? new Date(date).toLocaleDateString('ko-KR') : '날짜 정보 없음';
      const status = "배송중";
      const title = product && product.title ? product.title : '제목 없음';
      const price = product && product.price ? product.price.toLocaleString('ko-KR') : '가격 정보 없음';

      const orderHTML = `
        <div class="columns orders-item" id="order-${id}">
          <div class="column is-2 date-column">${formattedDate}</div>
          <div class="column is-6 order-summary">${title} - ₩${price}</div>
          <div class="column is-2 status-column">${status}</div>
          <div class="column is-2 actions-column">
            <button class="button is-danger delete-order-btn" id="deleteButton-${id}">주문 취소</button>
          </div>
        </div>
      `;

      ordersContainer.insertAdjacentHTML("beforeend", orderHTML);

      const deleteButton = document.querySelector(`#deleteButton-${id}`);
      deleteButton.addEventListener("click", () => {
        orderIdToDelete = id;
        openModal();
      });
    });
  } catch (err) {
    console.error("주문 정보를 불러오는 데 실패했습니다:", err);
  }
}


async function deleteOrderData(e) {
  e.preventDefault();

  try {
    const response = await Api.delete(`/orders/cancel/${orderIdToDelete}`);

    if (!response.ok) {
      throw new Error(`Server responded with status: ${response.status}`);
    }

    const contentType = response.headers.get('Content-Type');
    if (contentType && contentType.includes('application/json')) {
      const result = await response.json();
      console.log("Deletion response:", result);
    } else {
      console.error("Received non-JSON response");
    }
  } catch (err) {
    //console.error(`주문정보 삭제 과정에서 오류가 발생하였습니다: ${err}`);
    //alert(`주문정보 삭제 과정에서 오류가 발생하였습니다: ${err}`);
  }

  const deletedItem = document.querySelector(`#order-${orderIdToDelete}`);
  if (deletedItem) {
    deletedItem.remove();
  }

  orderIdToDelete = "";
  closeModal();
}

function cancelDelete() {
  orderIdToDelete = "";
  closeModal();
}

function openModal() {
  modal.classList.add("is-active");
}

function closeModal() {
  modal.classList.remove("is-active");
}

function keyDownCloseModal(e) {
  if (e.keyCode === 27) {
    closeModal();
  }
}
