export interface UserRegisterDto {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  company: number;
  authorities: string[];
}

export interface UserUpdateDto {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  company: number;
  authorities: string[];
}
