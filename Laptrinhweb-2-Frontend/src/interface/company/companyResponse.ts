import UserInterface from '../user/userResponse';

interface CompanyInterface {
  id: number;
  companyName: string;
  users: UserInterface[];
}

export default CompanyInterface;
