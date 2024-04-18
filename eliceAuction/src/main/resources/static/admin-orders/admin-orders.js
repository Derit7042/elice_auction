import { addCommas, createNavbar } from "../useful-functions.js";
import * as Api from "../api.js";

const selectors = {
  ordersCount: document.querySelector("#ordersCount"),
  prepareCount: document.querySelector("#prepareCount"),
  deliveryCount: document.querySelector("#deliveryCount"),
  completeCount: document.querySelector("#completeCount"),
  ordersContainer: document.querySelector("#ordersContainer"),
  modal: document.querySelector("#modal"),
  modalBackground: document.querySelector("#modalBackground"),
  modalCloseButton: document.querySelector("#modalCloseButton"),
  deleteCompleteButton: document.querySelector("#deleteCompleteButton"),
  deleteCancelButton: document.querySelector("#deleteCancelButton")
};

let orderIdToDelete;

function initialize() {
  createNavbar();
  loadOrders();
  addEventListeners();
}

function addEventListeners() {
  selectors.modalBackground.addEventListener("click", closeModal);
  selectors.modalCloseButton.addEventListener("click", closeModal);
  document.addEventListener("keydown", keyDownCloseModal);
  selectors.deleteCompleteButton.addEventListener("click", deleteOrderData);
  selectors.deleteCancelButton.addEventListener("click", cancelDelete);
}

async function loadOrders() {
  try {
    const orders = await Api.get('/admin/orders');
    console.log("주문 데이터:", orders);

    if (!orders.length) {
      selectors.ordersContainer.innerHTML = '<p>주문이 없습니다.</p>';
      return;
    }

    displayOrders(orders);
    updateSummary(orders);
  } catch (error) {
    console.error('주문 데이터를 불러오는데 실패했습니다:', error);
    selectors.ordersContainer.innerHTML = '<p>주문 데이터를 불러오는데 실패했습니다.</p>';
  }
}

function displayOrders(orders) {
  selectors.ordersContainer.innerHTML = '';
  orders.forEach(order => {
    // 초기 상태가 null인 경우 '상품 준비중'으로 설정합니다.
    const initialStatus = order.status || "상품 준비중";
    const orderHTML = generateOrderHTML(order, initialStatus);
    selectors.ordersContainer.insertAdjacentHTML("beforeend", orderHTML);
    document.querySelector(`#deleteButton-${order.id}`).addEventListener("click", () => handleDelete(order.id));
    document.querySelector(`#status-${order.id}`).addEventListener("change", (event) => updateOrderStatus(order.id, event.target.value));
  });
}

function generateOrderHTML(order, status) {
  const { id, product, date } = order;
  const formattedDate = date ? new Date(date).toLocaleDateString('ko-KR') : '날짜 없음';
  const title = product.title || '제목 없음';
  const price = product.price > 0 ? addCommas(product.price) + '원' : '가격 정보 없음';

  return `
    <div class="columns orders-item" id="order-${id}">
      <div class="column is-2">${formattedDate}</div>
      <div class="column is-4">${title}</div>
      <div class="column is-2">${price}</div>
      <div class="column is-2">
        <select id="status-${id}" class="status-select">
          <option value="상품 준비중" ${status === "상품 준비중" ? "selected" : ""}>상품 준비중</option>
          <option value="상품 배송중" ${status === "상품 배송중" ? "selected" : ""}>상품 배송중</option>
          <option value="배송완료" ${status === "배송완료" ? "selected" : ""}>배송완료</option>
        </select>
      </div>
      <div class="column is-2">
        <button class="button is-danger" id="deleteButton-${id}">취소</button>
      </div>
    </div>
  `;
}

async function updateOrderStatus(orderId, newStatus) {
  const orderElement = document.querySelector(`#order-${orderId}`);
  const oldStatus = orderElement.querySelector("select").value;

  if (newStatus === oldStatus) return;

  try {
    const response = await Api.patch(`/admin/orders/${orderId}`, { status: newStatus });
    if (response.success) {
      alert('주문 상태가 성공적으로 업데이트 되었습니다.');
      orderElement.querySelector("select").value = newStatus;
      updateSummaryAfterStatusChange(oldStatus, newStatus);
    } else {
      throw new Error('업데이트 실패: ' + response.message);
    }
  } catch (error) {
    console.error('주문 상태 업데이트 실패:', error);
    alert('주문 상태 업데이트 실패: ' + error.message);
  }
}

function updateSummary() {
  let totalCount = 0, prepareCount = 0, deliveryCount = 0, completeCount = 0;
  document.querySelectorAll('.status-select').forEach(select => {
    totalCount++;
    switch (select.value) {
      case "상품 준비중":
        prepareCount++;
        break;
      case "상품 배송중":
        deliveryCount++;
        break;
      case "배송완료":
        completeCount++;
        break;
    }
  });
  selectors.ordersCount.textContent = addCommas(totalCount);
  selectors.prepareCount.textContent = addCommas(prepareCount);
  selectors.deliveryCount.textContent = addCommas(deliveryCount);
  selectors.completeCount.textContent = addCommas(completeCount);
}

async function deleteOrderData() {
  if (!orderIdToDelete) return;

  try {
    const response = await Api.delete(`/admin/orders/${orderIdToDelete}`);
    if (response.success) {
      const orderElement = document.querySelector(`#order-${orderIdToDelete}`);
      if (orderElement) {
        const status = orderElement.querySelector('.status-select').value;
        orderElement.remove();
        updateSummaryAfterDelete(status);
      }
      alert("주문이 성공적으로 삭제되었습니다.");
      orderIdToDelete = null;
      closeModal();
    } else {
      throw new Error('삭제 실패: ' + response.message);
    }
  } catch (err) {
    console.error('주문 삭제 실패:', err);
    alert('주문 삭제 실패.');
  }
}

function cancelDelete() {
  closeModal();
  orderIdToDelete = null;
}

function handleDelete(orderId) {
  orderIdToDelete = orderId;
  openModal();
}

function openModal() {
  selectors.modal.classList.add("is-active");
}

function closeModal() {
  selectors.modal.classList.remove("is-active");
}

function keyDownCloseModal(e) {
  if (e.keyCode === 27) closeModal();
}

initialize();
