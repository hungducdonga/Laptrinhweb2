package com.example.spring_first_project.dto;

/**
 * Lớp phản hồi chung cho các API.
 * --------------------------------
 * - Dùng để đóng gói (wrap) dữ liệu trả về từ REST API.
 * - Giúp client nhận được thông tin chuẩn gồm:
 *      + statusCode: Mã trạng thái (HTTP code hoặc custom code)
 *      + message: Thông báo đi kèm
 *      + data: Dữ liệu chính (generic, có thể là bất kỳ kiểu nào)
 *
 * @param <T> Kiểu dữ liệu payload (VD: UserDemo, List<Company>, …)
 */
public class ApiResponse<T> {

    // Mã trạng thái trả về, ví dụ 200, 403, 500
    private int statusCode;

    // Thông báo mô tả kết quả, ví dụ: "Success", "Access Denied", ...
    private String message;

    // Dữ liệu thực tế trả về, có thể là đối tượng, danh sách hoặc null
    private T data;

    /**
     * Constructor đầy đủ
     * @param statusCode mã trạng thái
     * @param message thông báo
     * @param data dữ liệu trả về
     */
    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    /**
     * Constructor rỗng (cần cho quá trình serialize/deserialize JSON)
     */
    public ApiResponse() {
    }

    // Getter & Setter cho từng thuộc tính
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
