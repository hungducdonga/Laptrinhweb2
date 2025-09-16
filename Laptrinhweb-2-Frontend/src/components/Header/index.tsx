import { Link } from 'react-router';
import './index.css';
import { MenuUnfoldOutlined } from '@ant-design/icons';
import UserService from '../../api/services/UserService';
import { useEffect, useState } from 'react';
import UserInterface from '../../interface/user/userResponse';
import { handleError } from '../../utils/helper';

const items = [
  {
    id: 1,
    title: 'Home',
    url_path: '/'
  },
  {
    id: 2,
    title: 'Company',
    url_path: '/company'
  },
  {
    id: 3,
    title: 'User',
    url_path: '/user'
  },
  {
    id: 4,
    title: 'Logout',
    url_path: '/auth/sign-in'
  }
];

const logoutHandler = (value: number) => {
  if (value === 4) {
    // items.map((item) => {
    //   if (item.title.toLowerCase().toString() === items[value].title.toLocaleLowerCase().toString()) {
    //     UserService.logout();
    //   }
    // });
    UserService.logout();
  }
};

const menuItem = items.map((item) => (
  <li key={item.id} className='li_menu'>
    <Link onClick={() => logoutHandler(item.id)} to={`${item.url_path}`}>
      {item.title}
    </Link>
  </li>
));

interface HeaderCommonProps {
  showNavModal: boolean;
  setShowNavModal: (value: boolean) => void;
}

const HeaderCommon = ({ showNavModal, setShowNavModal }: HeaderCommonProps) => {
  const [userInformation, setUserInformation] = useState<UserInterface>();

  const handleMouseEnter = () => {
    setShowNavModal(!showNavModal);
  };
  const handleMouseLeave = () => {
    setShowNavModal(!showNavModal);
  };

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

  console.log('userInformation: ', userInformation);
  const role = localStorage.getItem('role');

  return (
    <nav className='w-full flex justify-between items-center bg-white py-3 px-3 shadow-md shadow-slate-500 fixed overflow-y-auto z-30'>
      <aside className='md:w-1/5 w-1/2 px-2 flex items-center gap-4'>
        <MenuUnfoldOutlined className='md:hidden text-xl font-bold cursor-pointer hover:text-slate-600 active:shadow-md active:shadow-slate-400' />
        <h2 className='font-bold text-2xl text-slate-900'>{role === 'ROLE_ADMIN' ? 'Welcome to Admin' : 'Welcome to website'}</h2>
      </aside>
      {UserService.isUser() && <ul className='hidden w-1/2 md:flex justify-start items-center gap-2'>{menuItem}</ul>}
      <div
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
        className={'relative z-40 md:w-1/6 flex flex-col justify-center items-center gap-1 cursor-pointer'}
      >
        <p className='text-[16px] text-slate-900 font-bold'>
          {userInformation?.firstName} {userInformation?.lastName}
        </p>
        <span className='text-xs text-slate-800 font-medium'>
          <Link to={'/user-profile'}>{userInformation?.email}</Link>
        </span>
      </div>
      {!UserService.isAuthenticated() ? (
        <div className='md:w-1/6 flex justify-start items-center gap-2'>
          <button type='submit' className='button_nav'>
            <Link to={'/auth/sign-in'}>Sign in</Link>
          </button>
          <button type='submit' className='button_nav'>
            <Link to={'/auth/sign-up'}>Sign up</Link>
          </button>
        </div>
      ) : null}
    </nav>
  );
};

export default HeaderCommon;
