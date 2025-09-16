import CommonInput from '../../../components/atoms/Input';
import { handleBlurChecking, handleError } from '../../../utils/helper';
import useCombinedState from '../../../hooks/useCombinedState';
import { BaseSyntheticEvent, useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router';
import { CheckCircleFilled, FacebookOutlined, GooglePlusOutlined } from '@ant-design/icons';
import { roles } from '../../../faker/company';
import UserService from '../../../api/services/UserService';
import Notification from '../../../components/Notification';
import CompanyInterface from '../../../interface/company/companyResponse';
import CompanyService from '../../../api/services/CompanyService';

const SignUp = () => {
  const [passHidden, setPassHidden] = useState<boolean>(false);
  const [notification, setNotification] = useState<boolean>(false);
  const [companyResponse, setCompanyResponse] = useState<CompanyInterface[]>();
  const navigate = useNavigate();
  const [error, setError] = useState<string>('');

  const [state, setField] = useCombinedState({
    email: '',
    password: '',
    passConfirm: '',
    firstName: '',
    lastName: '',
    company: '',
    role: '',
    emailError: '',
    passwordError: '',
    errorPassConfirm: '',
    firstNameError: '',
    lastNameError: '',
    companyError: '',
    roleError: ''
  });

  useEffect(() => {
    fetchCompany();
  }, []);

  const fetchCompany = () => {
    try {
      const response = CompanyService.getAllCompanies();
      response.then((obj) => {
        setCompanyResponse(obj);
        console.log('companyResponse: ', companyResponse);
      });
    } catch (error) {
      const message = handleError(error);
      throw new Error(message);
    }
  };

  const registerSubmit = async (e: BaseSyntheticEvent<Event, EventTarget & HTMLFormElement>) => {
    e.preventDefault();
    const formValues = Object.fromEntries(new FormData(e.target));
    console.log('formValues: ', formValues);
    const userData = {
      firstName: formValues.firstName.toString(),
      lastName: formValues.lastName.toString(),
      email: formValues.email.toString(),
      password: formValues.password.toString(),
      company: Number(formValues.company.toString()),
      authorities: [formValues.role.toString()]
    };
    console.log('userData: ', userData);
    try {
      if (formValues.passConfirm.toString() === formValues.password.toString()) {
        const response = await UserService.register(userData);
        console.log('response: ', response);
        setNotification(true);
        navigate('/auth/sign-in');
        return response;
      } else {
        alert('Confirm password is not match with password, please type password again');
        console.log('Confirm password: \n', formValues.passConfirm.toString());
        console.log('Password: \n', formValues.password.toString());
      }
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
    <div className='w-full h-full pt-20 px-2 pb-3 flex flex-col items-center justify-center'>
      <div className='md:w-1/3 sm:w-1/2 w-3/4 border rounded-md shadow-md shadow-slate-500 py-3 px-2 flex flex-col items-center justify-around'>
        <h2 className='text-2xl font-bold'>Sign Up</h2>
        <form onSubmit={registerSubmit} className='w-full'>
          <CommonInput
            onblur={() => handleBlurChecking('firstNameError', state.firstName, setField)}
            inputValue={state.firstName}
            typeInput='text'
            setField={setField}
            field='firstName'
            error={state.firstNameError}
            hidden={false}
            nameInput={'firstName'}
            label_title='First Name'
            placeholder='Please, enter first name'
            labelTileClassName='text-white'
          />
          <CommonInput
            onblur={() => handleBlurChecking('lastNameError', state.lastName, setField)}
            inputValue={state.lastName}
            typeInput='text'
            setField={setField}
            field='lastName'
            error={state.lastNameError}
            hidden={false}
            nameInput={'lastName'}
            label_title='Last Name'
            placeholder='Please, enter last name'
            labelTileClassName='text-white'
          />
          <CommonInput
            onblur={() => handleBlurChecking('emailError', state.email, setField)}
            inputValue={state.email}
            typeInput='email'
            setField={setField}
            field='email'
            error={state.emailError}
            hidden={false}
            nameInput={'email'}
            label_title='Email'
            placeholder='Please, enter email'
            labelTileClassName='text-white'
          />
          <CommonInput
            onblur={() => handleBlurChecking('passwordError', state.password, setField)}
            inputValue={state.password}
            typeInput='password'
            setField={setField}
            field='password'
            error={state.passwordError}
            hidden={false}
            iconPass={true}
            passHidden={passHidden}
            setPassHidden={setPassHidden}
            nameInput={'password'}
            label_title='Password'
            placeholder='Please, enter password'
            labelTileClassName='text-white'
          />
          <CommonInput
            onblur={() => handleBlurChecking('passConfirmError', state.passConfirm, setField)}
            inputValue={state.passConfirm}
            typeInput='password'
            setField={setField}
            field='passConfirm'
            error={state.passConfirmError}
            hidden={false}
            iconPass={true}
            passHidden={passHidden}
            setPassHidden={setPassHidden}
            nameInput={'passConfirm'}
            label_title='passConfirm'
            placeholder='Please, enter password confirm'
            labelTileClassName='text-white'
          />
          <CommonInput
            onblur={() => handleBlurChecking('companyError', state.company, setField)}
            inputValue={state.company}
            typeInput='text'
            setField={setField}
            field='company'
            error={state.companyError}
            hidden={false}
            nameInput={'company'}
            label_title='Company'
            placeholder='Please, enter company'
            labelTileClassName='text-white'
          />
          <CommonInput
            onblur={() => handleBlurChecking('roleError', state.role, setField)}
            inputValue={state.role}
            typeInput='text'
            setField={setField}
            field='role'
            error={state.roleError}
            hidden={false}
            optionList={roles}
            nameSelect='role'
            label_title='Role'
            placeholder='Please, enter role'
          />
          <button
            className='w-full py-2 my-2 bg-blue-700 border border-transparent text-slate-50 text-xl font-bold rounded-md shadow-md shadow-slate-600 hover:border-blue-700 hover:bg-blue-600 active:shadow-slate-800'
            type='submit'
          >
            Sign up
          </button>
        </form>
        <span className='w-full text-2xl py-3 flex justify-center gap-3'>
          <Link to={'/'}>
            <FacebookOutlined className='text-blue-700' />
          </Link>
          <Link to={'/'}>
            <GooglePlusOutlined className='text-red-700' />
          </Link>
        </span>
        <span className='text-xs text-slate-800 py-2 px-4 flex justify-end cursor-pointer'>
          You have been an account, right?
          <Link className='text-blue-700 px-1 font-bold' to={'/auth/sign-in'}>
            Sign in
          </Link>
        </span>
      </div>
      {notification && (
        <Notification
          icon={<CheckCircleFilled className='text-4xl text-green-600' />}
          message='Sign in successfully!'
          description='Completed!!!'
          notification={notification}
        />
      )}
    </div>
  );
};

export default SignUp;
