

const baseUrl = "http://localhost:8080/api/";


async function get(endpoint, params = "") {
  endpoint = baseUrl + endpoint;

  const apiUrl = params ? `${endpoint}/${params}` : endpoint;
  console.log(`%cGET 요청: ${apiUrl} `, "color: #a25cd1;");

  // 토큰이 있으면 Authorization 헤더를 포함, 없으면 포함하지 않음
  const token = sessionStorage.getItem("token");
  const headers = token ? { Authorization: `Bearer ${token}` } : {};

  const res = await fetch(apiUrl, { headers });

  // 응답 코드가 4XX 계열일 때 (400, 403 등)
  if (!res.ok) {
    const errorContent = await res.json();
    const { reason } = errorContent;

    throw new Error(reason);
  }

  const result = await res.json();
  return result;
}

async function post(endpoint, data) {
  const apiUrl = baseUrl+endpoint;

  const bodyData = JSON.stringify(data);
  console.log(`%cPOST 요청: ${apiUrl}`, "color: #296aba;");
  console.log(`%cPOST 요청 데이터: ${bodyData}`, "color: #296aba;");

  // 토큰이 있으면 Authorization 헤더를 포함, 없으면 포함하지 않음
  const token = sessionStorage.getItem("token");
  const headers = {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };

  try {
    const res = await fetch(apiUrl, {
      method: "POST",
      headers,
      body: bodyData,
    });

    // 응답 상태 코드 검사
    if (!res.ok) {
      try {
        const errorResponse = await res.json(); // 응답 본문 파싱 시도
        const reason = errorResponse.message || "Unknown error";
        throw new Error(reason);
      } catch (parseError) {
        // 응답 본문 파싱 실패
        throw new Error("응답 파싱 중 오류 발생");
      }
    }

    // 응답 본문이 비어 있지 않은 경우 JSON 파싱 시도
    if (res.headers.get("Content-Length") !== "0") {
      const responseData = await res.json();
      return responseData;
    }

    // 본문이 비어 있는 경우 간단한 성공 메시지 반환
    return { message: "성공적으로 처리되었습니다." };
  } catch (error) {
    console.error("API 요청 실패:", error);
    throw error; // 에러를 다시 throw하여 호출 측에서 처리할 수 있게 함
  }
}





// api 로 PATCH 요청 (/endpoint/params 로, JSON 데이터 형태로 요청함)
async function patch(endpoint, params = "", data) {
  endpoint = baseUrl + endpoint;

  const apiUrl = params ? `${endpoint}/${params}` : endpoint;

  const bodyData = JSON.stringify(data);
  console.log(`%cPATCH 요청: ${apiUrl}`, "color: #059c4b;");
  console.log(`%cPATCH 요청 데이터: ${bodyData}`, "color: #059c4b;");

  const res = await fetch(apiUrl, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${sessionStorage.getItem("token")}`,
    },
    body: bodyData,
  });

  // 응답 코드가 4XX 계열일 때 (400, 403 등)
  if (!res.ok) {
    const errorContent = await res.json();
    const { reason } = errorContent;

    throw new Error(reason);
  }

  const result = await res.json();

  return result;
}

// 아래 함수명에 관해, delete 단어는 자바스크립트의 reserved 단어이기에,
// 여기서는 우선 delete 대신 del로 쓰고 아래 export 시에 delete로 alias 함.
async function del(endpoint, params = "", data = {}) {
  endpoint = baseUrl + endpoint;

  const apiUrl = `${endpoint}/${params}`;
  const bodyData = JSON.stringify(data);

  console.log(`DELETE 요청 ${apiUrl}`, "color: #059c4b;");
  console.log(`DELETE 요청 데이터: ${bodyData}`, "color: #059c4b;");

  const res = await fetch(apiUrl, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${sessionStorage.getItem("token")}`,
    },
    body: bodyData,
  });

  // 응답 코드가 4XX 계열일 때 (400, 403 등)
  if (!res.ok) {
    const errorContent = await res.json();
    const { reason } = errorContent;

    throw new Error(reason);
  }

  const result = await res.json();

  return result;
}

// 아래처럼 export하면, import * as Api 로 할 시 Api.get, Api.post 등으로 쓸 수 있음.
export { get, post, patch, del as delete };
