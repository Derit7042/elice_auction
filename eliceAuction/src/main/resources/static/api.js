const baseUrl = "http://localhost:8080/api";  // 테스트 모드
// const baseUrl = "http://34.64.166.147:8080/api";  // 배포 모드

// 헤더를 구성하는 함수
function configureHeaders() {
    const token = sessionStorage.getItem("token");
    return token && token.trim() !== "" ? { "Authorization": `Bearer ${token}`, "Content-Type": "application/json" } : { "Content-Type": "application/json" };
}

// GET 요청
async function get(endpoint, params = "") {
    const apiUrl = `${baseUrl}${endpoint}${params ? `/${params}` : ""}`;
    console.log(`GET 요청: ${apiUrl}`);

    try {
        const response = await fetch(apiUrl, { headers: configureHeaders() });
        if (!response.ok) {
            const errorDetails = await response.json();
            throw new Error(`HTTP 상태 코드: ${response.status}, 에러 메시지: ${errorDetails.message}`);
        }
        return await response.json();
    } catch (error) {
        console.error("GET 요청 실패:", error);
        throw error;
    }
}

// POST 요청
async function post(endpoint, data) {
    const apiUrl = `${baseUrl}${endpoint}`;
    console.log(`POST 요청: ${apiUrl}`);

    try {
        const response = await fetch(apiUrl, {
            method: "POST",
            headers: configureHeaders(),
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            const errorDetails = await response.json();
            throw new Error(`HTTP 상태 코드: ${response.status}, 오류 메시지: ${errorDetails.message}`);
        }
        return response.headers.get("Content-Length") !== "0" ? await response.json() : { message: "성공적으로 처리되었습니다." };
    } catch (error) {
        console.error("POST 요청 실패:", error);
        throw error;
    }
}

// PATCH 요청
async function patch(endpoint, params = "", data) {
    const apiUrl = `${baseUrl}${endpoint}${params ? `/${params}` : ""}`;
    console.log(`PATCH 요청: ${apiUrl}`);

    try {
        const response = await fetch(apiUrl, {
            method: "PATCH",
            headers: configureHeaders(),
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            const errorDetails = await response.json();
            throw new Error(`HTTP 상태 코드: ${response.status}, 오류 메시지: ${errorDetails.message}`);
        }
        return await response.json();
    } catch (error) {
        console.error("PATCH 요청 실패:", error);
        throw error;
    }
}

// DELETE 요청
async function del(endpoint, params = "", data = {}) {
    const apiUrl = `${baseUrl}${endpoint}${params ? `/${params}` : ""}`;
    console.log(`DELETE 요청: ${apiUrl}`);

    try {
        const response = await fetch(apiUrl, {
            method: "DELETE",
            headers: configureHeaders(),
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            const errorDetails = await response.json();
            throw new Error(`HTTP 상태 코드: ${response.status}, 오류 메시지: ${errorDetails.message}`);
        }
        return await response.json();
    } catch (error) {
        console.error("DELETE 요청 실패:", error);
        throw error;
    }
}

export { get, post, patch, del as delete };