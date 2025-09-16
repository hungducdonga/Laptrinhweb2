import { BaseSyntheticEvent, useEffect, useState } from 'react';
import useCombinedState from '../../hooks/useCombinedState';
import CommonInput from '../atoms/Input';
import { handleBlurChecking, handleError } from '../../utils/helper';
import { useNavigate } from 'react-router';
import UserService from '../../api/services/UserService';
import CompanyService from '../../api/services/CompanyService';
import { CompanyCreateDto } from '../../interface/company/compnayCreateDto';
import CompanyInterface from '../../interface/company/companyResponse';

interface UpdateCompanyProps {
  selectedCompanyId: number | null;
}

const UpdateCompany = ({ selectedCompanyId }: UpdateCompanyProps) => {
  const navigate = useNavigate();
  const [error, setError] = useState<string>('');
  const [companyObject, setCompanyObject] = useState<CompanyInterface>();
  const [state, setField] = useCombinedState({
    company: '',
    id: '',
    companyError: '',
    idError: ''
  });

  useEffect(() => {
    fetchCompany();
  }, []);

  const fetchCompany = () => {
    try {
      const token: string | null = localStorage.getItem('token');
      const response = CompanyService.getACompany(token!, selectedCompanyId!);
      console.log(response);
      response.then((obj) => {
        console.log(obj.data);
        setCompanyObject(obj.data);
        return obj.data;
      });
    } catch (error) {
      const message = handleError(error)
      throw new Error(message);
    }
  };

  const submitHandler = async (e: BaseSyntheticEvent<Event, EventTarget & HTMLFormElement>) => {
    e.preventDefault();
    const formValues = Object.fromEntries(new FormData(e.target));
    console.log('Form values: ', formValues);
    try {
      const token = localStorage.getItem('token');
      const companyData: CompanyCreateDto = {
        companyName: formValues.company.toString()
      };
      console.log('companyData: ', companyData);
      if (token && UserService.isAuthenticated()) {
        const response = await CompanyService.updateCompany(token, companyData, selectedCompanyId!);
        navigate('/admin/company-manager');
        return response;
      }
    } catch (err) {

      const message = handleError(err)
      setError(message!);
      console.log(error);
      setTimeout(() => {
        setError('');
      }, 5000);
    }
  };

  return (
    <div className='w-1/3 flex flex-col justify-start items-center bg-slate-100 rounded-md shadow-md shadow-slate-600 py-2 px-3'>
      <div className='w-full py-3'>
        <h2 className='text-2xl text-center font-bold text-slate-800'>Update Company {selectedCompanyId}</h2>
      </div>
      <form className='w-full' onSubmit={submitHandler}>
        <CommonInput
          onblur={() => handleBlurChecking('idError', state.id, setField)}
          inputValue={companyObject?.id.toString()}
          typeInput='text'
          setField={setField}
          field='id'
          error={state.idError}
          hidden={false}
          nameSelect='id'
          nameInput={'id'}
          label_title='Id'
          placeholder={`${companyObject?.id}`}
        />
        <CommonInput
          onblur={() => handleBlurChecking('companyError', state.company, setField)}
          inputValue={state.company}
          typeInput='text'
          setField={setField}
          field='company'
          error={state.companyError}
          hidden={false}
          nameSelect='company'
          nameInput={'company'}
          label_title='Company'
          placeholder={`${companyObject?.companyName}`}
        />
        <button
          className='w-full py-2 my-2 bg-blue-700 border border-transparent text-slate-50 text-xl font-bold rounded-md shadow-md shadow-slate-600 hover:border-blue-700 hover:bg-blue-600 active:shadow-slate-800'
          type='submit'
        >
          Update
        </button>
      </form>
    </div>
  );
};

export default UpdateCompany;
