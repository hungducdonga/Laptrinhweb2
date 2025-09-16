import { useParams } from 'react-router';
import CompanyManager from './CompanyManager';

const AdminDashboard = () => {
  const params = useParams<{
    companyManager: string;
    userCompany: string;
  }>();
  const isCompanyManager = params.companyManager;
  // const isUserManager = params.userCompany;
  return (
    <>
      {!isCompanyManager ? (
        <div className='w-full h-full flex flex-col justify-start items-center pt-28'>
          <h2>AdminDashboard Page</h2>
        </div>
      ) : (
        <CompanyManager />
      )}
    </>
  );
};

export default AdminDashboard;
