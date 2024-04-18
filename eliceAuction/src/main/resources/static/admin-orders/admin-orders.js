import { addCommas, createNavbar } from "../useful-functions.js";
import * as Api from "../api.js";

// 요소 참조
const ordersCount = document.querySelector("#ordersCount");
const prepareCount = document.querySelector("#prepareCount");
const deliveryCount = document.querySelector("#deliveryCount");
const completeCount = document.querySelector("#completeCount");
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

function cancelDelete() {
  orderIdToDelete = null;
  closeModal();
}

let orderIdToDelete;
async function insertOrders() {
  try {
    const response = await Api.get("/admin/orders");
    const orders = response.data || [];
    const summary = {
      ordersCount: orders.length,
      prepareCount: 0,
      deliveryCount: 0,
      completeCount: 0,
    };

    ordersContainer.innerHTML = '';

    orders.forEach(order => {
      const { id, product, createdAt, status, memberAddress } = order;
      const date = new Date(createdAt).toLocaleDateString('ko-KR');
      const title = product?.title ?? '제목 없음';
      const price = product?.price?.toLocaleString('ko-KR') ?? '가격 정보 없음';
      const address = memberAddress?.address ?? '주소 정보 없음';

      if (status === "상품 준비중") summary.prepareCount++;
      else if (status === "상품 배송중") summary.deliveryCount++;
      else if (status === "배송완료") summary.completeCount++;

      const orderHTML = `
        <div class="columns orders-item" id="order-${id}">
          <div class="column is-2">${date}</div>
          <div class="column is-3 order-summary">${title}</div>
          <div class="column is-2">${addCommas(price)}원</div>
          <div class="column is-3">${address}</div>
          <div class="column is-2">
            <button class="button is-danger" id="deleteButton-${id}">주문 취소</button>
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

    ordersCount.innerText = addCommas(summary.ordersCount);
    prepareCount.innerText = addCommas(summary.prepareCount);
    deliveryCount.innerText = addCommas(summary.deliveryCount);
    completeCount.innerText = addCommas(summary.completeCount);
  } catch (err) {
    console.error('오류가 발생했습니다:', err);
    alert('주문 정보를 불러오는데 실패했습니다.');
  }
}

async function deleteOrderData(e) {
  e.preventDefault();
  if (!orderIdToDelete) return;

  try {
    await Api.delete(`/admin/orders/${orderIdToDelete}`);
    alert("주문 정보가 삭제되었습니다.");
    document.querySelector(`#order-${orderIdToDelete}`).remove();
    orderIdToDelete = null;
    closeModal();
  } catch (err) {
    alert(`주문정보 삭제 과정에서 오류가 발생하였습니다: ${err}`);
  }
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

