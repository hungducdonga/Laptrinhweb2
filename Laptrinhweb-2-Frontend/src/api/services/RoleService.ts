import axios, { AxiosError } from 'axios';

interface LoginError {
  message: string;
}

class RoleService {
  static BASE_URL = 'http://localhost:8080';

  static async getAllRoles() {
    try {
        const response = await axios.get(`${this.BASE_URL}/api/roles`)
        return response.data
    } catch (error) {
      if (axios.isAxiosError(error)) {
        const axiosError = error as AxiosError<LoginError>;
        if (axiosError.response) {
          // Lỗi từ server
          throw new Error(axiosError.response.data.message);
        } else if (axiosError.request) {
          // Yêu cầu đã được thực hiện nhưng không nhận được phản hồi
          throw new Error('No response from server.');
        } else {
          // Lỗi khác
          throw new Error(error.message);
        }
      }
      throw new Error('An unexpected error occurred.');
    }
  }
}

export default RoleService;
