interface Authority {
  roleId: number;
  authority: string;
}

interface UserInterface {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  company: number;
  authorities: Authority[];
}

export default UserInterface
