import { BaseSyntheticEvent, useState } from 'react';
import useCombinedState from '../../hooks/useCombinedState';
import CommonInput from '../atoms/Input';
import { handleBlurChecking, handleError } from '../../utils/helper';
import { useNavigate } from 'react-router';
import UserService from '../../api/services/UserService';
import CompanyService from '../../api/services/CompanyService';
import { CompanyCreateDto } from '../../interface/company/compnayCreateDto';

const AddCompany = () => {
  const navigate = useNavigate();
  const [error, setError] = useState<string>('');
  const [state, setField] = useCombinedState({
    company: '',
    companyError: ''
  });

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
        const response = await CompanyService.create(token, companyData);
        return response;
      }
      navigate('/admin/company-manager');
    } catch (err) {
      const message = handleError(err);
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
        <h2 className='text-2xl text-center font-bold text-slate-800'>Add Company</h2>
      </div>
      <form className='w-full' onSubmit={submitHandler}>
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
          placeholder='Please, enter company'
        />
        <button
          className='w-full py-2 my-2 bg-blue-700 border border-transparent text-slate-50 text-xl font-bold rounded-md shadow-md shadow-slate-600 hover:border-blue-700 hover:bg-blue-600 active:shadow-slate-800'
          type='submit'
        >
          Save
        </button>
      </form>
    </div>
  );
};

export default AddCompany;
