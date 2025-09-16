import { Link, useParams } from 'react-router';
import CompanyDetail from './CompanyDetail';
import { useEffect, useState } from 'react';
import UserService from '../../api/services/UserService';
import CompanyService from '../../api/services/CompanyService';
import CompanyInterface from '../../interface/company/companyResponse';
import { handleError } from '../../utils/helper';

const CompanyPage = () => {
  const params = useParams<{ id: string }>();
  const isDetailCompany = params.id;
  const [companyResponse, setCompanyResponse] = useState<CompanyInterface[]>();

  useEffect(() => {
    fetchCompany();
  }, []);

  const fetchCompany = () => {
    try {
      if (UserService.isAuthenticated() && localStorage.getItem('token')) {
        const response = CompanyService.getAllCompanies();
        response.then((obj) => {
          setCompanyResponse(obj);
        });
      }
    } catch (error) {
      const message = handleError(error);
      throw new Error(message);
    }
  };
  return (
    <>
      {!isDetailCompany ? (
        <div className='w-full min-h-screen pt-24 flex flex-col items-center justify-start'>
          <h2 className='text-3xl font-bold py-3'>Company List</h2>
          <div className='w-[80%] h-[60vh] m-auto overflow-y-auto overflow-hidden'>
            <table cellPadding={2} cellSpacing={2} className='w-full py-2 px-2 table-fixed'>
              <thead className='md:w-full py-2 text-slate-100'>
                <th className='sticky top-0 bg-blue-500 w-1/6 truncate py-2 border border-blue-500'>#Id</th>
                <th className='sticky top-0 bg-blue-500 w-1/3 truncate py-2 border border-blue-500'>Company Name</th>
                <th className='sticky top-0 bg-blue-500 w-1/5 truncate py-2 border border-blue-500'>Number of User</th>
                <th className='sticky top-0 bg-blue-500 w-1/4 truncate py-2 border border-blue-500'>Action</th>
              </thead>
              <tbody className='md:w-full bg-slate-100 text-slate-800'>
                {companyResponse?.map((item) => (
                  <tr key={item.id} className='w-full text-center'>
                    <td className='py-2 border border-blue-500'>{item.id}</td>
                    <td className='py-2 border border-blue-500'>{item.companyName}</td>
                    <td className='py-2 border border-blue-500'>{item.users.length}</td>
                    <td className='py-2 border border-blue-500 flex items-center justify-center gap-3'>
                      <Link
                        className='bg-green-500 text-xs text-slate-100 font-bold px-2 py-1 rounded-md'
                        to={`/company/${item.id}`}
                      >
                        View detail
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <CompanyDetail />
      )}
    </>
  );
};

export default CompanyPage;
