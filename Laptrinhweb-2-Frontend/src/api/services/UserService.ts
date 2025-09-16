import axios, { AxiosError } from 'axios';
import { UserRegisterDto, UserUpdateDto } from '../../interface/user/userRegisterDto';
import { DecentralizationDto } from '../../interface/user/decentralizationDto';
interface LoginError {
  message: string;
}
class UserService {
  static BASE_URL = 'http://localhost:8080';

  static async register(userData: UserRegisterDto) {
    try {
      const response = await axios.post(`${this.BASE_URL}/api/auth/register`, userData);
      return response.data;
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

  static async login(email: string, password: string) {
    try {
      const response = await axios.post(`${this.BASE_URL}/api/auth/login`, { email, password });
      return response.data;
    } catch (error) {
      // Kiểm tra nếu lỗi là lỗi Axios để lấy thông tin lỗi
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

  static async getAllUsers(token: string) {
    try {
      const response = await axios.get(`${this.BASE_URL}/api/users`, {
        headers: { Authorization: `Bearer${token}` }
      });
      return response.data;
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

  static async getUserByEmail(token: string, email: string) {
    try {
      const response = await axios.get(`${this.BASE_URL}/api/users/string/${email}`, {
        headers: { Authorization: `Bearer${token}` }
      });
      return response.data;
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

  static async deleteUser(token: string, userId: number) {
    try {
      const response = await axios.delete(`${this.BASE_URL}/api/user/${userId}`, {
        headers: {
          Authorization: `Bearer${token}`
        }
      });
      return response.data;
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

  static async updateUser(token: string, userId: number, userData: UserUpdateDto) {
    try {
      const response = await axios.put(`${this.BASE_URL}/api/user/${userId}`, userData, {
        headers: {
          Authorization: `Bearer${token}`
        }
      });
      return response.data;
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

  static async decentralization(token: string, decentralizationDto: DecentralizationDto, userId: number) {
    try {
      const response = await axios.put(`${this.BASE_URL}/api/decentralization/${userId}`, decentralizationDto, {
        headers: {
          Authorization: `Bearer${token}`
        }
      });
      return response.data;
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

  static async logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
  }

  static isAuthenticated() {
    const token = localStorage.getItem('token');
    return !!token;
  }

  static isAdmin() {
    const role = localStorage.getItem('role');
    return role === 'ROLE_ADMIN';
  }

  static isUser() {
    const role = localStorage.getItem('role');
    return role === 'ROLE_USER';
  }

  static adminOnly() {
    return this.isAuthenticated() && this.isAdmin();
  }
}

export default UserService;
