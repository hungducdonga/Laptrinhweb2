import axios, { AxiosError } from 'axios';
import { CompanyCreateDto } from '../../interface/company/compnayCreateDto';

interface LoginError {
  message: string;
}

class CompanyService {
  static BASE_URL = 'http://localhost:8080';

  static async create(token: string, companyDto: CompanyCreateDto) {
    try {
      const response = await axios.post(`${this.BASE_URL}/api/company`, companyDto, {
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

  static async getAllCompanies() {
    try {
      const response = await axios.get(`${this.BASE_URL}/api/companies`);
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

  static async getACompany(token: string, companyId: number) {
    try {
      const response = await axios.get(`${this.BASE_URL}/api/company/${companyId}`, {
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

  static async updateCompany(token: string, companyDto: CompanyCreateDto, companyId: number) {
    try {
      const response = await axios.put(`${this.BASE_URL}/api/company/${companyId}`, companyDto, {
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

  static async deleteCompany(token: string, companyId: number) {
    try {
      const response = await axios.delete(`${this.BASE_URL}/api/company/${companyId}`, {
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
}

export default CompanyService;
