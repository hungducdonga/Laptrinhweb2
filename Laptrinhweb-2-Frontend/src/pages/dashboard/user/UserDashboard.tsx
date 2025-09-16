import { Link } from 'react-router';
import useCombinedState from '../../../hooks/useCombinedState';
import { BaseSyntheticEvent, useEffect, useState } from 'react';
import CommonInput from '../../../components/atoms/Input';
import { handleBlurChecking, handleError } from '../../../utils/helper';
import { companies, roles } from '../../../faker/company';
import UserInterface from '../../../interface/user/userResponse';
import UserService from '../../../api/services/UserService';
import { UserUpdateDto } from '../../../interface/user/userRegisterDto';

const UserDashboard = () => {
  const [passHidden, setPassHidden] = useState<boolean>(false);
  const [userInformation, setUserInformation] = useState<UserInterface>();

  const [state, setField] = useCombinedState({
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    company: '',
    role: '',
    emailError: '',
    passwordError: '',
    firstNameError: '',
    lastNameError: '',
    companyError: '',
    roleError: ''
  });

  useEffect(() => {
    fetchUserData();
  }, []);

  const fetchUserData = () => {
    try {
      if (UserService.isAuthenticated() && localStorage.getItem('token')) {
        const emailRes: string | null = localStorage.getItem('email');
        const token: string | null = localStorage.getItem('token');
        const response = UserService.getUserByEmail(token!, emailRes!);
        response.then((obj) => {
          console.log(obj.data);
          setUserInformation(obj.data);
          return obj.data;
        });
      }
    } catch (error) {
      const message = handleError(error)
      throw new Error(message);
    }
  };

  const loginSubmit = async (e: BaseSyntheticEvent<Event, EventTarget & HTMLFormElement>) => {
    e.preventDefault();
    const formValues = Object.fromEntries(new FormData(e.target));
    console.log('Form values: ', formValues);
    const token: string | null = localStorage.getItem('token');
    const userData: UserUpdateDto = {
      firstName: formValues.firstName.toString(),
      lastName: formValues.lastName.toString(),
      email: formValues.email.toString(),
      password: formValues.password.toString(),
      company: Number(formValues.company.toString()),
      authorities: [formValues.role.toString()]
    };
    if (userInformation?.email === localStorage.getItem('email') && UserService.isAuthenticated()) {
      const response = await UserService.updateUser(token!, userInformation.id, userData);
      console.log('response: ', response.data);
      return response;
    }
  };

  return (
    <div className='w-full h-full pt-20 flex flex-col justify-start items-center'>
      {/* <h2 className='text-2xl font-bold text-slate-900'>User Profile</h2> */}
      <div className={'relative md:w-1/6 flex flex-col justify-center items-center gap-1 cursor-pointer'}>
        <p className='text-2xl text-slate-900 font-bold'>
          {userInformation?.firstName} {userInformation?.lastName}
        </p>
        <span className='text-xl text-slate-800 font-medium'>
          <Link to={'/user-profile'}>{userInformation?.email}</Link>
        </span>
      </div>
      <div className='w-3/4 flex flex-col justify-start items-center py-4 gap-2'>
        <form onSubmit={loginSubmit} className='w-full'>
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
            placeholder={userInformation?.firstName}
            labelTileClassName='text-white'
            inputClassName='border-none bg-[#ffffff1d] text-slate-200 placeholder:text-slate-200 dark:placeholder:text-slate-200 outline-slate-800 focus:outline-green-500 focus:bg-transparent'
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
            placeholder={userInformation?.lastName}
            labelTileClassName='text-white'
            inputClassName='border-none bg-[#ffffff1d] text-slate-200 placeholder:text-slate-200 dark:placeholder:text-slate-200 outline-slate-800 focus:outline-green-500 focus:bg-transparent'
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
            placeholder={userInformation?.email}
            labelTileClassName='text-white'
            inputClassName='border-none bg-[#ffffff1d] text-slate-200 placeholder:text-slate-200 dark:placeholder:text-slate-200 outline-slate-800 focus:outline-green-500 focus:bg-transparent'
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
            placeholder={userInformation?.password}
            labelTileClassName='text-white'
            inputClassName='border-none bg-[#ffffff1d] text-slate-200 placeholder:text-slate-200 dark:placeholder:text-slate-200 outline-slate-800 focus:outline-green-500 focus:bg-transparent'
          />
          <CommonInput
            onblur={() => handleBlurChecking('companyError', state.company, setField)}
            inputValue={state.company}
            typeInput='text'
            setField={setField}
            field='company'
            error={state.companyError}
            hidden={false}
            optionList={companies}
            nameSelect='company'
            nameInput={'company'}
            label_title='Company'
            placeholder={'None'}
          />
          {userInformation?.authorities.map((item) => (
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
              placeholder={item.authority}
            />
          ))}

          <button
            className='w-full py-2 my-2 bg-blue-700 border border-transparent text-slate-50 text-xl font-bold rounded-md shadow-md shadow-slate-600 hover:border-blue-700 hover:bg-blue-600 active:shadow-slate-800'
            type='submit'
          >
            Save
          </button>
        </form>
      </div>
    </div>
  );
};

export default UserDashboard;
