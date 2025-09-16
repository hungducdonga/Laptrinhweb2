import HeaderCommon from './components/Header';
import FooterCommon from './components/Footer';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import HomePage from './pages/home/HomePage';
import { Link } from 'react-router';
import './components/Header/index.css';
import UserService from './api/services/UserService';
import UserInterface from './interface/user/userResponse';
import { handleError } from './utils/helper';

const items = [
  {
    id: 1,
    title: 'Company manager',
    url_path: '/admin/company-manager'
  },
  {
    id: 2,
    title: 'User manager',
    url_path: '/admin/user-manager'
  },
  {
    id: 3,
    title: 'Logout',
    url_path: '/auth/sign-in'
  }
];

const logoutHandler = (value: number) => {
  if (value === 3) {
    // items.map((item) => {
    //   if (item.title.toLowerCase().toString() === items[value].title.toLocaleLowerCase().toString()) {
    //     UserService.logout();
    //   }
    // });
    UserService.logout();
  }
};

const menuItem = items.map((item) => (
  <li key={item.id} className='li_menu bg-slate-200 w-full'>
    <Link onClick={() => logoutHandler(item.id)} to={`${item.url_path}`}>
      {item.title}
    </Link>
  </li>
));

const RootLayout = () => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    if (location.pathname === '/') {
      navigate('/');
    }
    fetchUserData();
  }, []);

  const [showNavModal, setShowNavModal] = useState<boolean>(false);
  const [userInformation, setUserInformation] = useState<UserInterface>();
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
      const message = handleError(error);
      throw new Error(message);
    }
  };

  console.log('userInformation: ', userInformation)

  // const role = 'admin';
  let layout;

  switch (localStorage.getItem('role')) {
    case 'ROLE_ADMIN':
      layout = (
        <div className='w-full flex justify-between items-start'>
          <div className='w-1/4 min-h-screen flex flex-col justify-start items-center bg-slate-50 shadow-md shadow-slate-700 gap-4 px-4'>
            <div className={'relative pt-28 w-full flex flex-col justify-center items-center gap-1 cursor-pointer'}>
              <p className='text-[16px] text-slate-900 font-bold'>{userInformation?.firstName} {userInformation?.lastName}</p>
              <span className='text-xs text-slate-800 font-medium'>
                <Link to={'/user-profile'}>{userInformation?.email}</Link>
              </span>
            </div>
            <ul className='flex w-full md:flex-col justify-start items-start gap-4 py-4'>{menuItem}</ul>
          </div>
          {location.pathname !== '/' ? <Outlet /> : <HomePage />}
        </div>
      );
      break;
    case 'ROLE_USER':
      layout = <>{location.pathname !== '/' ? <Outlet /> : <HomePage />}</>;
      break;

    default:
      layout = <>{location.pathname !== '/' ? <Outlet /> : <HomePage />}</>;
      break;
  }

  return (
    <main className='relative w-full min-h-screen flex flex-col items-center justify-between'>
      <HeaderCommon showNavModal={showNavModal} setShowNavModal={setShowNavModal} />
      {layout}
      <FooterCommon />
    </main>
  );
};

export default RootLayout;
